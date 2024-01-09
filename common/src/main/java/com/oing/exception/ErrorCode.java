package com.oing.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    /**
     * Common Errors
     */
    UNKNOWN_SERVER_ERROR("CM0001", "Unknown Server Error"),
    INVALID_INPUT_VALUE("CM0002", "Invalid Input Value"),
    METHOD_NOT_ALLOWED("CM0003", "Method Not Allowed"),

    /**
     * APP Version Related Error
     */
    APP_UPDATE_REQUIRED("AV0001", "Update Required"),
    APP_KEY_NOT_FOUND("AV0002", "App Key Not Found"),
    /**
     * Auth Related Errors
     */
    AUTHENTICATION_FAILED("AU0001", "Authentication failed"),
    AUTHORIZATION_FAILED("AU0002", "No Permission"),
    REFRESH_TOKEN_INVALID("AU0003", "Refresh Token is invalid"),
    /**
     * Member Related Errors
     */
    MEMBER_NOT_FOUND("MB0001", "Member not found"),
    MEMBER_ALREADY_EXISTS("MB0002", "Member With That Credentials Already Exists"),
    /**
     * MemberPost Related Errors
     */
    POST_NOT_FOUND("PO0001", "Post not found"),

    /**
     * MemberEmoji Related Errors
     */
    EMOJI_ALREADY_EXISTS("EM0001", "Emoji already exists"),
    EMOJI_NOT_FOUND("EM0002", "Emoji not found"),
    /**
     * Family Related Errors
     */
    FAMILY_NOT_FOUND("FM0001", "Family not found"),
    ALREADY_IN_FAMILY("FM0002", "Already in family"),
    /**
     * Post Related Errors
     */
    INVALID_UPLOAD_TIME("PO0001", "Invalid Upload Time. The request is outside the valid time range" +
            "(from 12:00 AM yesterday to 12:00 AM today)."),
    DUPLICATE_POST_UPLOAD("PO0002", "Duplicate Post Upload"),
    /**
     * Deep Link Related Errors
     */
    LINK_NOT_VALID("DL0001", "Link is not valid");


    private final String code;
    private final String message;
}
