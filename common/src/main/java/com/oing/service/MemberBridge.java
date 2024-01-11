package com.oing.service;

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
}
