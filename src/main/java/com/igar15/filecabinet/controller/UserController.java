package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/list")
    public String showAll(@RequestParam(value = "email", required = false) String email,
                          @SortDefault("email") Pageable pageable,
                          Model model) {
        Page<User> users = null;
        if (email == null) {
            users = userService.findAll(pageable);
        }
        else {
            users = userService.findAllByEmail(email, pageable);
        }

        model.addAttribute("users", users);
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
