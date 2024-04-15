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
import static java.util.concurrent.TimeUnit.*;
import static org.awaitility.Awaitility.await;

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(DatabaseCleanerExtension.class)
class FamilyScoreEventListenerTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FamilyRepository familyRepository;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private ReactionRepository reactionRepository;

    @Autowired
    private MemberRealEmojiRepository memberRealEmojiRepository;

    @Autowired
    private RealEmojiRepository realEmojiRepository;

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
    void 새로운_글이_업로드되면_가족_점수를_더한다() {
        // given & when
        postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        postRepository.save(new Post(
                "2",
                testMember2.getId(),
                testMember2.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/2",
                "images/2",
                "2"
        ));

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(NEW_POST_SCORE * 2);
        });
    }

    @Test
    void 글이_삭제되면_가족_점수를_뺀다() {
        // given
        postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        postRepository.save(new Post(
                "2",
                testMember2.getId(),
                testMember2.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/2",
                "images/2",
                "2"
        ));

        // when
        postRepository.deleteById("1");
        postRepository.deleteById("2");

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(0);
        });
    }

    @Test
    void 새로운_댓글이_달리면_가족_점수를_더한다() {
        // given
        Post post = postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));

        int originScore = NEW_POST_SCORE;

        // when
        commentRepository.save(new Comment(
                "1",
                post,
                testMember1.getId(),
                "1"
        ));
        commentRepository.save(new Comment(
                "2",
                post,
                testMember2.getId(),
                "1"
        ));

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(originScore + NEW_COMMENT_SCORE * 2);
        });
    }

    @Test
    void 댓글이_지워지면_가족_점수를_뺀다() {
        // given
        Post post = postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        commentRepository.save(new Comment(
                "1",
                post,
                testMember1.getId(),
                "1"
        ));
        commentRepository.save(new Comment(
                "2",
                post,
                testMember2.getId(),
                "1"
        ));

        int originScore = NEW_POST_SCORE + (NEW_COMMENT_SCORE * 2);

        // when
        commentRepository.deleteById("1");
        commentRepository.deleteById("2");

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(originScore - (NEW_COMMENT_SCORE * 2));
        });
    }

    @Test
    void 새로운_리액션이_달리면_가족_점수를_더한다() {
        // given
        Post post = postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));

        int originScore = NEW_POST_SCORE;

        // when
        reactionRepository.save(new Reaction(
                "1",
                post,
                testMember1.getId(),
                Emoji.EMOJI_1
        ));
        reactionRepository.save(new Reaction(
                "2",
                post,
                testMember2.getId(),
                Emoji.EMOJI_3
        ));

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(originScore + (NEW_REACTION_SCORE * 2));
        });
    }

    @Test
    void 리액션이_지워지면_가족_점수를_뺀다() {
        // given
        Post post = postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
                "https://storage.com/images/1",
                "images/1",
                "1"
        ));
        reactionRepository.save(new Reaction(
                "1",
                post,
                testMember1.getId(),
                Emoji.EMOJI_1
        ));
        reactionRepository.save(new Reaction(
                "2",
                post,
                testMember2.getId(),
                Emoji.EMOJI_3
        ));

        int originScore = NEW_POST_SCORE + (NEW_REACTION_SCORE * 2);

        // when
        reactionRepository.deleteById("1");
        reactionRepository.deleteById("2");

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(originScore - (NEW_REACTION_SCORE * 2));
        });
    }

    @Test
    void 새로운_리얼이모지가_달리면_가족_점수를_더한다() {
        // given
        Post post = postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
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
        realEmojiRepository.save(new RealEmoji(
                "1",
                testEmoji1,
                post,
                testMember1.getId()
        ));
        realEmojiRepository.save(new RealEmoji(
                "2",
                testEmoji2,
                post,
                testMember2.getId()
        ));

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(originScore + (NEW_REAL_EMOJI_SCORE * 2));
        });
    }

    @Test
    void 리얼이모지가_지워지면_가족_점수를_뺀다() {
        // given
        Post post = postRepository.save(new Post(
                "1",
                testMember1.getId(),
                testMember1.getFamilyId(),
                Type.SURVIVAL,
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
        realEmojiRepository.save(new RealEmoji(
                "1",
                testEmoji1,
                post,
                testMember1.getId()
        ));
        realEmojiRepository.save(new RealEmoji(
                "2",
                testEmoji2,
                post,
                testMember2.getId()
        ));

        int originScore = NEW_POST_SCORE + NEW_REAL_EMOJI_SCORE * 2;

        // when
        realEmojiRepository.deleteById("1");
        realEmojiRepository.deleteById("2");

        // then
        await().atMost(5, SECONDS).until(() -> {
            Integer newScore = familyRepository.findById(testMember1.getFamilyId()).get().getScore();
            return newScore.equals(originScore - (NEW_REAL_EMOJI_SCORE * 2));
        });
    }
}