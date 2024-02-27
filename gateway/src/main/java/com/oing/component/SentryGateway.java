package com.oing.component;

import io.sentry.Sentry;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

@Component
@RequiredArgsConstructor
public class SentryGateway {

    public static void captureException(HttpServletRequest request, Throwable exception) {
        // Add extra data to future events in this scope.
        Sentry.withScope(scope -> {
            scope.setExtra("method", request.getMethod());

            if (!request.getQueryString().isEmpty()) {
                scope.setExtra("queryString", request.getQueryString());
            }
            if (!request.getRequestURI().isEmpty()) {
                scope.setExtra("requestURI", request.getRequestURI());
            }
            if (!request.getRequestURL().isEmpty()) {
                scope.setExtra("requestURL", request.getRequestURL().toString());
            }
            if (!request.getParameterMap().isEmpty()) {
                for (String key : request.getParameterMap().keySet()) {
                    scope.setExtra(key, request.getParameter(key));
                }
            }
            if (request.getContentLength() > 0) { // if request body is not empty
                scope.setExtra("requestBody", getBody(request));
            }

            Sentry.captureException(exception);
        });

    }

    private static String getBody(HttpServletRequest request) {
        try (
                InputStream inputStream = request.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))
        ) {
            StringBuilder stringBuilder = new StringBuilder();
            while (bufferedReader.ready()) {
                stringBuilder.append(bufferedReader.readLine());
            }

            return stringBuilder.toString();

        } catch (IOException e) {
            return "Error reading the request body";
        }
    }
}