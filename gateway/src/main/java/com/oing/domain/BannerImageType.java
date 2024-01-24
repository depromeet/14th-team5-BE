package com.oing.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BannerImageType {
    SKULL_FLAG("SKULL_FLAG"),
    ALONE_WALING("ALONE_WALING"),
    WE_ARE_FRIENDS("WE_ARE_FRIENDS"),
    JEWELRY_TREASURE("JEWELRY_TREASURE"),
    ;

    private final String imageCode;
}
