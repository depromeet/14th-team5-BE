package com.oing.service;

import com.oing.domain.AiPostType;
import com.oing.domain.Post;
import com.oing.domain.PostType;
import com.oing.repository.PostRepository;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class PostServiceTest {

    @InjectMocks
    private PostService postService;

    @Mock
    private PostRepository postRepository;
    @Mock
    private ApplicationEventPublisher applicationEventPublisher;
    @Mock
    private PreSignedUrlGenerator preSignedUrlGenerator;
    @Mock
    private MissionBridge missionBridge;
    @Mock
    private IdentityGenerator identityGenerator;

    @Test
    void 미션_키_획득까지_남은_생존신고_게시글_업로드_수_조회_테스트() {
        //given
        String familyId = "1";

        //when
        when(postRepository.countFamilyMembersByFamilyIdAtYesterday(familyId)).thenReturn(1);
        when(postRepository.countTodaySurvivalPostsByFamilyId(familyId)).thenReturn(0);

        //then
        assertEquals(0, postService.calculateRemainingSurvivalPostCountUntilMissionUnlocked(familyId));
    }

    @Test
    void 미션_키_획득_여부_조회_테스트() {
        //given
        String familyId = "1";

        //when
        when(postRepository.countFamilyMembersByFamilyIdAtYesterday(familyId)).thenReturn(4);
        when(postRepository.countTodaySurvivalPostsByFamilyId(familyId)).thenReturn(3);

        //then
        assertTrue(postService.isCreatedSurvivalPostByMajority(familyId));
    }

    @Test
    void AI_이미지_게시물_타입별_그룹화_조회_테스트() {
        //given
        String familyId = "testFamily";
        Post christmasPost1 = new Post("1", "member1", familyId, null, PostType.AI_IMAGE,
                AiPostType.CHRISTMAS_2025, "image1.jpg", "key1", null);

        Post christmasPost2 = new Post("2", "member2", familyId, null, PostType.AI_IMAGE,
                AiPostType.CHRISTMAS_2025, "image2.jpg", "key2", null);

        Post chuseokPost = new Post("3", "member1", familyId, null, PostType.AI_IMAGE,
                AiPostType.CHUSEOK_2025, "image3.jpg", "key3", null);

        List<Post> mockPosts = List.of(christmasPost1, christmasPost2, chuseokPost);

        //when
        when(postRepository.findAllByFamilyIdAndTypeOrderByCreatedAtDesc(familyId, PostType.AI_IMAGE))
                .thenReturn(mockPosts);

        Map<AiPostType, List<Post>> result = postService.findAllAiImagePostsByFamilyGroupedByType(familyId);

        //then
        assertEquals(2, result.size());
        assertTrue(result.containsKey(AiPostType.CHRISTMAS_2025));
        assertTrue(result.containsKey(AiPostType.CHUSEOK_2025));
        assertEquals(2, result.get(AiPostType.CHRISTMAS_2025).size());
        assertEquals(1, result.get(AiPostType.CHUSEOK_2025).size());
    }

    @Test
    void AI_이미지_게시물_타입별_그룹화_조회_빈_결과_테스트() {
        //given
        String familyId = "testFamily";
        List<Post> emptyPosts = List.of();

        //when
        when(postRepository.findAllByFamilyIdAndTypeOrderByCreatedAtDesc(familyId, PostType.AI_IMAGE))
                .thenReturn(emptyPosts);

        Map<AiPostType, List<Post>> result = postService.findAllAiImagePostsByFamilyGroupedByType(familyId);

        //then
        assertTrue(result.isEmpty());
    }
}
