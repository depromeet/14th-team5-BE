package com.oing.config;

import com.oing.domain.ErrorReportDTO;
import com.oing.dto.response.ErrorResponse;
import com.oing.exception.TokenNotValidException;
import com.oing.exception.DomainException;
import com.oing.exception.ErrorCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.ServletRequestBindingException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.security.InvalidParameterException;
import java.util.Enumeration;

/**
 * server
 * User: CChuYong
 * Date: 2023/11/22
 * Time: 4:32 PM
 */
@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class SpringWebExceptionHandler {
    private final ApplicationEventPublisher eventPublisher;

    @ExceptionHandler(DomainException.class)
    ResponseEntity<ErrorResponse> handleDomainException(HttpServletRequest request, DomainException exception) {
        log.debug("[DomainException]", exception);
        if (exception.getErrorCode() == ErrorCode.UNKNOWN_SERVER_ERROR) {
            return handleUnhandledException(request, exception);
        }

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(exception.getErrorCode()));
    }

    @ExceptionHandler(value = {
            HttpMessageNotReadableException.class,
            InvalidParameterException.class,
            ServletRequestBindingException.class,
            MethodArgumentNotValidException.class,
            ConstraintViolationException.class,
    })
    ResponseEntity<ErrorResponse> handleValidateException(Exception exception) {
        log.warn("[InvalidParameterException]", exception);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ErrorCode.INVALID_INPUT_VALUE));
    }

    @ExceptionHandler(value = {
            HttpRequestMethodNotSupportedException.class,
    })
    ResponseEntity<ErrorResponse> handleMethodNotAllowedException(HttpRequestMethodNotSupportedException exception) {
        log.warn("[HttpRequestMethodNotSupportedException]", exception);

        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ErrorCode.METHOD_NOT_ALLOWED));
    }

    @ExceptionHandler(TokenNotValidException.class)
    ResponseEntity<ErrorResponse> handleAuthenticationFailedException(HttpRequestMethodNotSupportedException exception) {
        log.warn("[AuthenticationFailedException]", exception);

        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(ErrorResponse.of(ErrorCode.AUTHENTICATION_FAILED));
    }

    @ExceptionHandler(IOException.class)
    ResponseEntity<ErrorResponse> handleClientCancelException(HttpServletRequest request, IOException exception) {
        if (exception.getMessage().contains("Broken pipe")) {
            log.warn("[IOException] Broken Pipe");
        } else {
            log.error("[IOException]", exception);
        }
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ErrorCode.UNKNOWN_SERVER_ERROR));
    }


    @ExceptionHandler(Throwable.class)
    ResponseEntity<ErrorResponse> handleUnhandledException(HttpServletRequest request, Throwable exception) {
        StringBuilder dump = dumpRequest(request).append("\n ").append(getStackTraceAsString(exception));
        log.error("[UnhandledException] {} \n", dump);
        eventPublisher.publishEvent(new ErrorReportDTO(exception.getMessage(), dump.toString()));
        return ResponseEntity
                .badRequest()
                .body(ErrorResponse.of(ErrorCode.UNKNOWN_SERVER_ERROR));
    }

    private String getStackTraceAsString(Throwable throwable) {
        StringWriter stringWriter = new StringWriter();
        throwable.printStackTrace(new PrintWriter(stringWriter));
        return stringWriter.toString();
    }

    private StringBuilder dumpRequest(HttpServletRequest request) {
        StringBuilder dump = new StringBuilder("HttpRequest Dump:")
                .append("\n  Remote Addr   : ").append(request.getRemoteAddr())
                .append("\n  Protocol      : ").append(request.getProtocol())
                .append("\n  Request Method: ").append(request.getMethod())
                .append("\n  Request URI   : ").append(request.getRequestURI())
                .append("\n  Query String  : ").append(request.getQueryString())
                .append("\n  Parameters    : ");

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement();
            dump.append("\n    ").append(name).append('=');
            String[] parameterValues = request.getParameterValues(name);
            for (String value : parameterValues) {
                dump.append(value);
            }
        }

        dump.append("\n  Headers       : ");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String name = headerNames.nextElement();
            dump.append("\n    ").append(name).append(":");
            Enumeration<String> headerValues = request.getHeaders(name);
            while (headerValues.hasMoreElements()) {
                dump.append(headerValues.nextElement());
            }
        }

        return dump;
    }
}
