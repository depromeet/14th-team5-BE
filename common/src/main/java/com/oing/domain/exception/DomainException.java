package com.oing.domain.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public abstract class DomainException extends RuntimeException {
    private final ErrorCode errorCode;
}
