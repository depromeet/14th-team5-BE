package com.oing.service;

import com.oing.repository.PostRepository;
import com.oing.util.IdentityGenerator;
import com.oing.util.PreSignedUrlGenerator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

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
}
