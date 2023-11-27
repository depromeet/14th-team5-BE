package com.oing.dto.response;

import java.util.Arrays;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 12:23 PM
 */
public record AppleKeyListResponse(
        AppleKeyResponse[] keys
) {
    @Override
    public boolean equals(Object obj) {
        return obj instanceof AppleKeyListResponse applekeylistresponse
                && Arrays.equals(applekeylistresponse.keys, keys);
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(keys);
    }

    @Override
    public String toString() {
        return "AppleKeyListResponse{" +
                "keys=" + Arrays.toString(keys) +
                '}';
    }
}
