package com.igar15.filecabinet.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PathController {

    @GetMapping("/login")
    public String showLoginPage() {
        return "loginPage";
    }

    @GetMapping("/resetPassword")
    public String showResetPasswordPage() {
        return "resetPassword";
    }

    @GetMapping("/profile")
    public String showProfilePage() {
        return "profile";
    }



}
