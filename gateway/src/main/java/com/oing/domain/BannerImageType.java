package com.oing.domain;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BannerImageType {
    SKULL_FLAG("SKULL_FLAG"),
    ALONE_WALKING("ALONE_WALKING"),
    WE_ARE_FRIENDS("WE_ARE_FRIENDS"),
    JEWELRY_TREASURE("JEWELRY_TREASURE"),
    ;

    private final String imageCode;
}
