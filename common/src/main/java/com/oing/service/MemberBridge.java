package com.oing.service;

import java.util.List;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/11/24
 * Time: 10:18 AM
 */
public interface MemberBridge {
    /**
     * Return associated member's family id.
     * @throws com.oing.exception.FamilyNotFoundException if member don't have family.
     * @throws com.oing.exception.MemberNotFoundException if member not found.
     * @param memberId member id
     * @return family id
     */
    String getFamilyIdByMemberId(String memberId);

    /**
     * 같은 가족에 속해있는지 확인합니다
     * @param memberIdFirst 첫 번쨰 사용자 아이디
     * @param memberIdSecond 두 번째 사용자 아이디
     * @return 가족 같은지 여부 (한쪽이라도 null이면 false)
     * @throws com.oing.exception.MemberNotFoundException 사용자가 존재하지 않을 경우
     */
    boolean isInSameFamily(String memberIdFirst, String memberIdSecond);

    /**
     * 사용자가 삭제된 사용자인지 확인합니다
     * @param memberId 사용자 아이디
     * @return 삭제된 사용자인지 여부
     */
    boolean isDeletedMember(String memberId);

    boolean isBirthDayMember(String memberId);

    List<String> getFamilyMembersIdsByFamilyId(String familyId);

    String getMemberNameByMemberId(String memberId);

    List<String> getFamilyMemberNamesByFamilyId(String familyId);

    List<String> getFamilyMemberProfileImgUrlsByFamilyId(String familyId);

    String getMemberProfileImgUrlByMemberId(String memberId);

    int getFamilyMemberCountByFamilyId(String familyId);
}
