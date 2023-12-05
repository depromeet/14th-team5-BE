package com.oing.component;

import com.oing.domain.ErrorReportDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.mockito.Mockito.*;

/**
 * no5ing-server
 * User: CChuYong
 * Date: 2023/11/27
 * Time: 2:35 PM
 */
@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class ApplicationListenerTest {

    @Mock
    private SlackGateway slackGateway;

    @Mock
    private Environment environment;

    @InjectMocks
    private ApplicationListener applicationListener;

//    @Test
//    void testOnErrorReportInProductionInstance() {
//        // Given
//        when(environment.getActiveProfiles()).thenReturn(new String[]{"prod"});
//        ErrorReportDTO errorReportDTO = new ErrorReportDTO("Error Message", "Payload");
//
//        // When
//        applicationListener.onErrorReport(errorReportDTO);
//
//        // Then
//        verify(slackGateway, times(1)).sendSlackBotMessage(any());
//    }
//
//    @Test
//    void testOnErrorReportNotInProductionInstance() {
//        // Given
//        when(environment.getActiveProfiles()).thenReturn(new String[]{"test"});
//        ErrorReportDTO errorReportDTO = new ErrorReportDTO("Error Message", "Payload");
//
//        // When
//        applicationListener.onErrorReport(errorReportDTO);
//
//        // Then
//        verify(slackGateway, never()).sendSlackBotMessage(any());
//    }
}
