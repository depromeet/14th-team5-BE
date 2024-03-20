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
    TOKEN_AUTHENTICATION_FAILED("AU0002", "Token Authentication failed"),
    AUTHORIZATION_FAILED("AU0003", "No Permission"),
    REFRESH_TOKEN_INVALID("AU0004", "Refresh Token is invalid"),
    TOKEN_EXPIRED("AU0005", "Token is expired"),
    UNAUTHORIZED_MEMBER_USED("AU0006", "Attempting to use unauthorized member"),
    /**
     * Member Related Errors
     */
    MEMBER_NOT_FOUND("MB0001", "Member not found"),
    MEMBER_ALREADY_EXISTS("MB0002", "Member With That Credentials Already Exists"),
    INVALID_MEMBER_NAME_LENGTH("MB0003", "Member name is empty or longer than 9 characters"),
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
     * MemberComment Related Errors
     */
    POST_COMMENT_NOT_FOUND("CM0001", "Comment not found"),
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
     * Real-Emoji Related Errors
     */
    REAL_EMOJI_NOT_FOUND("RE0001", "Real-Emoji not found"),
    REAL_EMOJI_ALREADY_EXISTS("RE0002", "Real-Emoji already exists"),
    REGISTERED_REAL_EMOJI_NOT_FOUND("RE0003", "Registered Real-Emoji not found"),
    DUPLICATE_REAL_EMOJI("RE0004", "Duplicate Real Emoji"),
    /**
     * Deep Link Related Errors
     */
    LINK_NOT_VALID("DL0001", "Link is not valid");


    private final String code;
    private final String message;
}
