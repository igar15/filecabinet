package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String showAll(@SortDefault("email") Pageable pageable, Model model) {
        model.addAttribute("users", userService.findAll(pageable));
        return "users/user-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        return "users/user-form";
    }

    @PostMapping("/save")
    public String save(@Validated User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "users/user-form";
        }
        userService.create(user);
        return "redirect:/users/list";
    }

    @GetMapping("/showUserInfo/{id}")
    public String showUserInfo(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "users/user-info";
    }

    @GetMapping("/showUserInfoByEmail/{email}")
    public String showUserInfo(@PathVariable("email") String email, Model model) {
        model.addAttribute("user", userService.findByEmail(email));
        return "users/user-info";
    }

    @GetMapping("/showFormForUpdate/{id}")
    public String showFormForUpdate(@PathVariable("id") int id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "users/user-form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/users/list";
    }



}
