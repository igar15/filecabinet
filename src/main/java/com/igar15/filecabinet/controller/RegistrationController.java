package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.service.UserService;
import com.igar15.filecabinet.util.exception.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/signUp")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationPage";
    }

    @PostMapping("/user/register")
    public String registerUser(@Valid User user, BindingResult result) {
        if (result.hasErrors()) {
            return "registrationPage";
        }
        try {
            userService.registerNewUser(user);
        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return "registrationPage";
        }
        return "redirect:/login";
    }




}
