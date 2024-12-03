package co.leapwise.assignments.expression_evaluator.web.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FaviconController { //for some reason properties to not fetch favicon were ignored

    @GetMapping({"/favicon","/favicon.ico"})
    public void returnNoFavicon() {
    }

}