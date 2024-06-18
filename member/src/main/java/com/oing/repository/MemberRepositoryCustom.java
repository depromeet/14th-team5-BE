package com.oing.repository;

import java.util.List;

public interface MemberRepositoryCustom {

    List<String> findFamilyMemberNamesByFamilyId(String familyId);

    List<String> findFamilyMemberProfileImgUrlsByFamilyId(String familyId);
}
