package com.oing.service;


import com.oing.domain.SerializableDeepLink;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 1/3/24
 * Time: 10:23 AM
 */
public interface DeepLinkDetailService<T extends SerializableDeepLink> {
    T storeDeepLinkDetails(T details);
    T findPriorDeepLinkDetails(T details);
    T retrieveDeepLinkDetails(String linkId);
}
