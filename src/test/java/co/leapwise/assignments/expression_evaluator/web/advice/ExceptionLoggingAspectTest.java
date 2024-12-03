package co.leapwise.assignments.expression_evaluator.web.advice;

import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ExceptionLoggingAspectTest {

    @InjectMocks
    private ExceptionLoggingAspect exceptionLoggingAspect;

    @Mock
    private ProceedingJoinPoint joinPoint;

    @Mock
    private Signature signature;

    private ListAppender<ILoggingEvent> listAppender;

    @BeforeEach
    void setUp() {
        Logger logger = (Logger) LoggerFactory.getLogger(ExceptionLoggingAspect.class);

        listAppender = new ListAppender<>();
        listAppender.start();
        logger.addAppender(listAppender);
    }


    @Test
    void logGlobalExceptionHandlerResponses_ShouldLogResponse() throws Throwable {
        Object mockResponse = "Mock response";
        given(joinPoint.proceed()).willReturn(mockResponse);
        given(joinPoint.getArgs()).willReturn(new Object[]{new RuntimeException("test")});

        Object result = exceptionLoggingAspect.logGlobalExceptionHandlerResponses(joinPoint);

        assertThat(result).isEqualTo(mockResponse);
        List<ILoggingEvent> logs = listAppender.list;
        assertThat(logs).hasSize(2);
        assertThat(logs.getFirst().getFormattedMessage())
                .isEqualTo("-- Handling exception");
        assertThat(logs.get(1).getFormattedMessage())
                .isEqualTo("-- Error response from Mock response");
    }

    @Test
    void logGlobalExceptionHandlerResponses_ShouldLogException() throws Throwable {
        RuntimeException exception = new RuntimeException("Test exception");
        given(joinPoint.proceed()).willThrow(exception);

        assertThatThrownBy(() -> exceptionLoggingAspect.logGlobalExceptionHandlerResponses(joinPoint)).isEqualTo(exception);

        List<ILoggingEvent> logs = listAppender.list;
        assertThat(logs).hasSize(1);
        assertThat(logs.get(0).getFormattedMessage())
                .isEqualTo("-- Exception in error mapping");
    }
}
