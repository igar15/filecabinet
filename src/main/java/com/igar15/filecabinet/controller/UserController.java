package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.PasswordResetToken;
import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.service.DepartmentService;
import com.igar15.filecabinet.service.UserService;
import org.passay.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.SortDefault;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BeanPropertyBindingResult;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;

    @Autowired
    private PasswordValidator passwordValidator;


    @GetMapping("/list")
    public String showAll(@RequestParam(value = "email", required = false) String email,
                          @SortDefault("name") Pageable pageable,
                          Model model) {
        Page<User> users = null;
        if (email == null) {
            users = userService.findAll(pageable);
        } else {
            users = userService.findAllByEmail(email, pageable);
        }

        model.addAttribute("users", users);
        return "users/user-list";
    }

    @GetMapping("/showAddForm")
    public String showAddForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departments", departmentService.findAll());
        return "users/user-form";
    }

    @PostMapping("/save")
    public String save(@Validated User user, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
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
        model.addAttribute("departments", departmentService.findAll());
        return "users/user-update-form";
    }

    @PostMapping("/updateWithoutPassword")
    public String updateWithoutPassword(@Validated User user, BindingResult bindingResult, Model model) {
        List<FieldError> errorsToKeep = bindingResult.getFieldErrors().stream()
                .filter(fer -> !fer.getField().equals("password")
                        && !fer.getField().equals("passwordConfirmation"))
                .collect(Collectors.toList());
        bindingResult = new BeanPropertyBindingResult(user, "user");
        for (FieldError fieldError : errorsToKeep) {
            bindingResult.addError(fieldError);
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("departments", departmentService.findAll());
            return "users/user-update-form";
        }
        userService.updateWithoutPassword(user);
        return "redirect:/users/showUserInfo/" + user.getId();
    }

    @GetMapping("/showFormForChangePassword/{id}")
    public String showFormForChangePassword(@PathVariable("id") int id) {
        return "users/user-change-password-form";
    }

    @PostMapping("/changePasswordForUser/{id}")
    public String changePasswordForUser(@PathVariable("id") int id,
                                        @RequestParam("password") String password,
                                        @RequestParam("passwordConfirmation") String passwordConfirmation,
                                        RedirectAttributes redirectAttributes,
                                        Model model) {
        if (!password.equals(passwordConfirmation)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            return "users/user-change-password-form";
        }

        String validPasswordMessage = validPassword(password);
        if (!validPasswordMessage.equals("Password reset successfully")) {
            model.addAttribute("errorMessage", validPasswordMessage);
            return "users/user-change-password-form";
        }
        userService.updateUserPassword(id, password);
        return "redirect:/users/showUserInfo/" + id;
    }




    @GetMapping("/delete/{id}")
    public String delete(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/users/list";
    }

    @GetMapping("/changeStatus/{id}")
    public String changeStatus(@PathVariable("id") int id, Model model) {
        User user = userService.findById(id);
        user.setEnabled(!user.getEnabled());
        userService.changeStatus(user);
        model.addAttribute("user", user);
        return "users/user-info";
    }

    @PostMapping("/resetPassword")
    public String resetPassword(HttpServletRequest request,
                                @RequestParam("email") String userEmail,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findByEmail(userEmail);
        if (user != null) {
            String token = UUID.randomUUID().toString();
            userService.createPasswordResetTokenForUser(user, token);
            final String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            final SimpleMailMessage email = constructResetTokenEmail(appUrl, token, user);
            mailSender.send(email);
        }
        redirectAttributes.addFlashAttribute("message", "You should receive an Password Reset Email shortly");
        return "redirect:/login";
    }

    @GetMapping("/changePassword")
    public String showChangePasswordPage(@RequestParam("id") int id,
                                         @RequestParam("token") String token,
                                         RedirectAttributes redirectAttributes,
                                         Model model) {
        PasswordResetToken passToken = userService.getPasswordResetToken(token);
        if (passToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
            return "redirect:/login";
        }
        User user = passToken.getUser();
        if (user.getId() != id) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid password reset token");
            return "redirect:/login";
        }

        final Calendar cal = Calendar.getInstance();
        if ((passToken.getExpiryDate()
                .getTime()
                - cal.getTime()
                .getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your password reset token has expired");
            return "redirect:/login";
        }

        model.addAttribute("token", token);
        return "changePassword";
    }

    @PostMapping("/savePassword")
    public String savePassword(@RequestParam("password") String password,
                               @RequestParam("passwordConfirmation") String passwordConfirmation,
                               @RequestParam("token") String token,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (!password.equals(passwordConfirmation)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            model.addAttribute("token", token);
            return "changePassword";
        }

        PasswordResetToken p = userService.getPasswordResetToken(token);
        if (p == null) {
            redirectAttributes.addFlashAttribute("message", "Invalid token");
        } else {
            User user = p.getUser();
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "Unknown user");
            } else {
                String validPasswordMessage = validPassword(password);
                if (!validPasswordMessage.equals("Password reset successfully")) {
                    model.addAttribute("errorMessage", validPasswordMessage);
                    model.addAttribute("token", token);
                    return "changePassword";
                }
                    userService.changeUserPassword(user, password);
                    redirectAttributes.addFlashAttribute("message", validPasswordMessage);
            }
        }
        return "redirect:/login";
    }


    private String validPassword(String password) {
        RuleResult result = passwordValidator.validate(new PasswordData(password));
        if (result.isValid()) {
            return "Password reset successfully";
        }
        StringBuilder stringBuilder = new StringBuilder();
        passwordValidator.getMessages(result).forEach(message -> stringBuilder.append(message).append("\n"));
        return stringBuilder.toString();
    }








    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/users/changePassword?id=" + user.getId() + "&token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText("Please open the following URL to reset your password: \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }


}
