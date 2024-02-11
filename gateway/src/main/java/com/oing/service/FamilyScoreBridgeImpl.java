package com.oing.service;

import com.oing.domain.CreateNewFamilyTopPercentageHistoryDTO;
import com.oing.domain.Family;
import com.oing.domain.MemberPost;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

import static com.oing.domain.Family.*;

@Service
@RequiredArgsConstructor
public class FamilyScoreBridgeImpl implements FamilyScoreBridge {

    private final FamilyService familyService;
    private final FamilyTopPercentageHistoryService familyTopPercentageHistoryService;
    private final MemberPostService memberPostService;

    @Override
    @Transactional
    public void setAllFamilyScoresByPostDateBetween(LocalDate startDate, LocalDate endDate) {
        List<Family> families = familyService.findAll();
        for (Family family : families) {
            family.resetScore();
            family.addScore(calculateFamilyScoreByPostDateBetween(family.getId(), startDate, endDate));
        }
    }

    private int calculateFamilyScoreByPostDateBetween(String familyId, LocalDate startDate, LocalDate endDate) {
        int familyScore = 0;

        List<MemberPost> familyPosts = memberPostService.findAllByFamilyIdAndCreatedAtBetween(familyId, startDate, endDate);
        familyScore += familyPosts.size() * NEW_POST_SCORE;
        familyScore += familyPosts.stream().mapToInt(MemberPost::getCommentCnt).sum() * NEW_COMMENT_SCORE;
        familyScore += familyPosts.stream().mapToInt(MemberPost::getReactionCnt).sum() * NEW_REACTION_SCORE;
        familyScore += familyPosts.stream().mapToInt(MemberPost::getReactionCnt).sum() * NEW_REAL_EMOJI_SCORE;

        return familyScore;
    }

    @Override
    @Transactional
    public void updateAllFamilyTopPercentageHistories(LocalDate historyDate) {
        List<Family> families = familyService.findAll();
        int familiesCount = familyService.getFamiliesScoreDistinctCount();

        for (Family family : families) {
            int rank = familyService.getFamiliesScoreDistinctCountByScoreGoe(family.getScore());
            int topPercentage = calculateFamilyTopPercentage(rank, familiesCount);

            CreateNewFamilyTopPercentageHistoryDTO dto = new CreateNewFamilyTopPercentageHistoryDTO(
                    family.getId(),
                    historyDate,
                    family,
                    topPercentage
            );
            familyTopPercentageHistoryService.create(dto);

            family.resetScore();
        }
    }

    @Override
    public int calculateFamilyTopPercentage(int rank, int familiesCount) {
        // divide by zero error 핸들링
        if (familiesCount == 0) {
            return 0;
        }

        return (int) Math.ceil((double) rank / familiesCount * 100);
    }
}
