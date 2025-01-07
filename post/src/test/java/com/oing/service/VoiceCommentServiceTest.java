package com.oing.service;

import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.domain.VoiceComment;
import com.oing.dto.request.CreatePostVoiceCommentRequest;
import com.oing.repository.VoiceCommentRepository;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class VoiceCommentServiceTest {
    @InjectMocks
    private VoiceCommentService voiceCommentService;

    @Mock
    private VoiceCommentRepository voiceCommentRepository;
    @Mock
    private IdentityGenerator identityGenerator;
    @Mock
    private PreSignedUrlGenerator preSignedUrlGenerator;
    @Mock
    private MemberBridge memberBridge;


    @Test
    void 음성_댓글_저장_테스트() {
        // given
        String memberId = "1";
        String familyId = "1";
        Post post = new Post("1", memberId, familyId, PostType.SURVIVAL, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        CreatePostVoiceCommentRequest request = new CreatePostVoiceCommentRequest("1");
        VoiceComment comment = new VoiceComment("1", null, "1", "https://oing.com/audio.mp3");

        when(memberBridge.isInSameFamily(memberId, post.getMemberId())).thenReturn(true);
        when(identityGenerator.generateIdentity()).thenReturn("1");
        when(voiceCommentRepository.save(any())).thenReturn(comment);

        //when
        VoiceComment comment1 = voiceCommentService.saveVoiceComment(post, request, memberId);

        //then
        assertEquals(comment, comment1);
    }

    @Test
    void 음성_댓글_삭제_테스트() {
        //given
        String memberId = "1";
        Post post = new Post("1", memberId, "1", PostType.SURVIVAL, "1", "1", "1");
        when(voiceCommentRepository.findById("1")).thenReturn(Optional.of(new VoiceComment("1", post, "1", "https://oing.com/audio.mp3")));

        //when
        voiceCommentService.deleteVoiceComment(post, "1", memberId);

        //then
        //ignore if no exception
    }

    @Test
    void 게시물_음성_댓글_리스트_조회_테스트() {
        // given
        String memberId = "1";
        Post post = new Post("1", memberId, "1", PostType.SURVIVAL, "https://oing.com/post.jpg", "post.jpg",
                "안녕.오잉.");
        VoiceComment comment = new VoiceComment("1", post, "1", "https://oing.com/audio.mp3");
        when(voiceCommentRepository.findByPostId("1")).thenReturn(List.of(comment));

        // when
        List<VoiceComment> voiceComments = voiceCommentService.getPostVoiceComments("1");

        // then
        assertEquals(1, voiceComments.size());
    }
}
