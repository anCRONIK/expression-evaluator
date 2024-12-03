package co.leapwise.assignments.expression_evaluator.web.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class FaviconControllerTest {

    @InjectMocks
    private FaviconController faviconController;

    @Test
    void doNothing() {
        faviconController.returnNoFavicon();
    }

}