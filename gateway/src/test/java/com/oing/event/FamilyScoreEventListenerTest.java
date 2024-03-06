package com.oing.event;

import com.oing.domain.*;
import com.oing.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static com.oing.domain.Family.*;
import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test")
@SpringBootTest
@ExtendWith(DatabaseCleanerExtension.class)
class FamilyScoreEventListenerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberPostRepository memberPostRepository;

    @Autowired
    private MemberPostCommentRepository memberPostCommentRepository;

    @Autowired
    private MemberPostReactionRepository memberPostReactionRepository;

    @Autowired
    private MemberRealEmojiRepository memberRealEmojiRepository;

    @Autowired
    private MemberPostRealEmojiRepository memberPostRealEmojiRepository;

    private final Member testMember1 = new Member(
            "testMember1",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember1",
            "profile.com/1",
            "1",
            LocalDateTime.now()
    );

    private final Member testMember2 = new Member(
            "testMember2",
            "testFamily",
            LocalDate.of(1999, 10, 18),
            "testMember2",
            "profile.com/2",
            "2",
            LocalDateTime.now()
    );

    @BeforeEach
    void setUp() {
        familyRepository.save(new Family(testMember1.getFamilyId()));
        memberRepository.save(testMember1);
        memberRepository.save(testMember2);
    }



    @Test
    void 새로운_글이_업로드되면_가족_점수를_더한다() throws InterruptedException {
        // given & when
        memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        memberPostRepository.save(new MemberPost(
                "2",
                testMember2.getId(),
                testMember2.getFamilyId(),
                "https://storage.com/images/2",
                "images/2",
                "2"
        ));

        Thread.sleep(1000);

        // then
        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(NEW_POST_SCORE * 2);
    }

    @Test
    void 글이_삭제되면_가족_점수를_뺀다() throws InterruptedException {
        // given
        memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        memberPostRepository.save(new MemberPost(
                "2",
                testMember2.getId(),
                testMember2.getFamilyId(),
                "https://storage.com/images/2",
                "images/2",
                "2"
        ));

        // when
        memberPostRepository.deleteById("1");
        memberPostRepository.deleteById("2");

        Thread.sleep(1000);

        // then

        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(0);
    }

    @Test
    void 새로운_댓글이_달리면_가족_점수를_더한다() throws InterruptedException {
        // given
        MemberPost post = memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));

        int originScore = NEW_POST_SCORE;

        // when
        memberPostCommentRepository.save(new MemberPostComment(
                "1",
                post,
                testMember1.getId(),
                "1"
        ));
        memberPostCommentRepository.save(new MemberPostComment(
                "2",
                post,
                testMember2.getId(),
                "1"
        ));

        Thread.sleep(1000);

        // then

        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(originScore + NEW_COMMENT_SCORE * 2);
    }

    @Test
    void 댓글이_지워지면_가족_점수를_뺀다() throws InterruptedException {
        // given
        MemberPost post = memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        memberPostCommentRepository.save(new MemberPostComment(
                "1",
                post,
                testMember1.getId(),
                "1"
        ));
        memberPostCommentRepository.save(new MemberPostComment(
                "2",
                post,
                testMember2.getId(),
                "1"
        ));

        int originScore = NEW_POST_SCORE + (NEW_COMMENT_SCORE * 2);

        // when
        memberPostCommentRepository.deleteById("1");
        memberPostCommentRepository.deleteById("2");

        Thread.sleep(1000);

        // then

        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(originScore - (NEW_COMMENT_SCORE * 2));
    }

    @Test
    void 새로운_리액션이_달리면_가족_점수를_더한다() throws InterruptedException {
        // given
        MemberPost post = memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));

        int originScore = NEW_POST_SCORE;

        // when
        memberPostReactionRepository.save(new MemberPostReaction(
                "1",
                post,
                testMember1.getId(),
                Emoji.EMOJI_1
        ));
        memberPostReactionRepository.save(new MemberPostReaction(
                "2",
                post,
                testMember2.getId(),
                Emoji.EMOJI_3
        ));

        Thread.sleep(1000);

        // then
        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(originScore + (NEW_REACTION_SCORE * 2));
    }

    @Test
    void 리액션이_지워지면_가족_점수를_뺀다() throws InterruptedException {
        // given
        MemberPost post = memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        memberPostReactionRepository.save(new MemberPostReaction(
                "1",
                post,
                testMember1.getId(),
                Emoji.EMOJI_1
        ));
        memberPostReactionRepository.save(new MemberPostReaction(
                "2",
                post,
                testMember2.getId(),
                Emoji.EMOJI_3
        ));

        int originScore = NEW_POST_SCORE + (NEW_REACTION_SCORE * 2);

        // when
        memberPostReactionRepository.deleteById("1");
        memberPostReactionRepository.deleteById("2");

        Thread.sleep(1000);

        // then

        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(originScore - (NEW_REACTION_SCORE * 2));
    }

    @Test
    void 새로운_리얼이모지가_달리면_가족_점수를_더한다() throws InterruptedException {
        // given
        MemberPost post = memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        MemberRealEmoji testEmoji1 = memberRealEmojiRepository.save(new MemberRealEmoji(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Emoji.EMOJI_1,
                "1",
                "1"

        ));
        MemberRealEmoji testEmoji2 = memberRealEmojiRepository.save(new MemberRealEmoji(
                "2",
                testMember2.getId(),
                testMember2.getFamilyId(),
                Emoji.EMOJI_2,
                "2",
                "2"
        ));

        int originScore = NEW_POST_SCORE;

        // when
        memberPostRealEmojiRepository.save(new MemberPostRealEmoji(
                "1",
                testEmoji1,
                post,
                testMember1.getId()
        ));
        memberPostRealEmojiRepository.save(new MemberPostRealEmoji(
                "2",
                testEmoji2,
                post,
                testMember2.getId()
        ));

        Thread.sleep(1000);

        // then
        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(originScore + (NEW_REAL_EMOJI_SCORE * 2));
    }

    @Test
    void 리얼이모지가_지워지면_가족_점수를_뺀다() throws InterruptedException {
        // given
        MemberPost post = memberPostRepository.save(new MemberPost(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        MemberRealEmoji testEmoji1 = memberRealEmojiRepository.save(new MemberRealEmoji(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Emoji.EMOJI_1,
                "1",
                "1"

        ));
        MemberRealEmoji testEmoji2 = memberRealEmojiRepository.save(new MemberRealEmoji(
                "2",
                testMember2.getId(),
                testMember2.getFamilyId(),
                Emoji.EMOJI_2,
                "2",
                "2"

        ));
        memberPostRealEmojiRepository.save(new MemberPostRealEmoji(
                "1",
                testEmoji1,
                post,
                testMember1.getId()
        ));
        memberPostRealEmojiRepository.save(new MemberPostRealEmoji(
                "2",
                testEmoji2,
                post,
                testMember2.getId()
        ));

        int originScore = NEW_POST_SCORE + NEW_REAL_EMOJI_SCORE * 2;


        // when
        memberPostRealEmojiRepository.deleteById("1");
        memberPostRealEmojiRepository.deleteById("2");

        Thread.sleep(1000);

        // then
        int newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
        assertThat(newScore).isEqualTo(originScore - (NEW_REAL_EMOJI_SCORE * 2));
    }
}