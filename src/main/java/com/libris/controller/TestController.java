package com.libris.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Controller
public class TestController {

    @GetMapping("/test")
    public String hello() {
        return "테스트입니다?";
    }

}
