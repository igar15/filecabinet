package com.igar15.filecabinet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PathController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "loginPage";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordPage() {
        return "forgotPassword";
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }



}
