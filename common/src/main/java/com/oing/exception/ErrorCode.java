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
    AUTHENTICATION_FAILED("AU0001", "인증에 실패했습니다."),
    TOKEN_AUTHENTICATION_FAILED("AU0002", "토큰을 통한 인증에 실패했습니다."),
    AUTHORIZATION_FAILED("AU0003", "권한이 없습니다."),
    REFRESH_TOKEN_INVALID("AU0004", "유효하지 않은 리프레쉬 토큰입니다."),
    TOKEN_EXPIRED("AU0005", "토큰이 만료됐습니다."),
    UNAUTHORIZED_MEMBER_USED("AU0006", "인증되지 않은/권한이 없는 회원에 접근하였습니다."),

    /**
     * Member Related Errors
     */
    MEMBER_NOT_FOUND("MB0001", "찾을 수 없는 회원입니다."),
    MEMBER_ALREADY_EXISTS("MB0002", "이미 존재하는 회원입니다."),
    INVALID_MEMBER_NAME_LENGTH("MB0003", "회원 이름의 길이가 유효하지 않습니다."),
    /**
     * Post Related Errors
     */
    POST_NOT_FOUND("PO0001", "존재하지 않는 게시글입니다."),
    INVALID_UPLOAD_TIME("PO0002", "게시글 업로드가 허용되지 않은 시간대입니다."),
    DUPLICATE_SURVIVAL_POST_UPLOAD("PO0003", "이미 생존신고 게시글을 업로드했습니다."),
    DUPLICATE_MISSION_POST_UPLOAD("PO0004", "이미 미션 게시글을 업로드했습니다."),
    MISSION_POST_ACCESS_DENIED("PO0005", "미션 게시글 기능에 접근할 수 없습니다."),

    /**
     * Emoji Related Errors
     */
    EMOJI_ALREADY_EXISTS("EM0001", "이미 존재하는 이모지입니다."),
    EMOJI_NOT_FOUND("EM0002", "존재하지 않는 이모지입니다."),

    /**
     * Comment Related Errors
     */
    POST_COMMENT_NOT_FOUND("CM0001", "존재하지 않는 댓글입니다."),

    /**
     * Family Related Errors
     */
    FAMILY_NOT_FOUND("FM0001", "존재하지 않는 가족입니다."),
    ALREADY_IN_FAMILY("FM0002", "이미 가족에 가입되어 있습니다."),

    /**
     * Real-Emoji Related Errors
     */
    REAL_EMOJI_NOT_FOUND("RE0001", "존재하지 않는 리얼이모지입니다."),
    REAL_EMOJI_ALREADY_EXISTS("RE0002", "이미 존재하는 리얼이모지입니다."),
    REGISTERED_REAL_EMOJI_NOT_FOUND("RE0003", "등록된 리얼이모지가 아닙니다."),
    DUPLICATE_REAL_EMOJI("RE0004", "이미 등록된 리얼이모지입니다."),

    /**
     * Mission Related Errors
     */
    MISSION_NOT_FOUND("MI0001", "존재하지 않는 미션입니다."),
    DAILY_MISSION_HISTORY_NOT_FOUND("MI0002", "존재하지 않는 미션 히스토리입니다."),

    /**
     * Deep Link Related Errors
     */
    LINK_NOT_VALID("DL0001", "Link is not valid"),

    /**
     * Member Pick Related Errors
     */
    ALREADY_PICKED_TODAY("MP0001", "Already picked today"),
    MEMBER_ALREADY_UPLOADED_POST("MP0002", "Member already uploaded post"),

    ;


    private final String code;
    private final String message;
}
