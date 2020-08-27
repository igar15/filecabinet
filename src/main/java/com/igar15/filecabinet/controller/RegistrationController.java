package com.igar15.filecabinet.controller;

import com.igar15.filecabinet.entity.PasswordResetToken;
import com.igar15.filecabinet.entity.User;
import com.igar15.filecabinet.entity.VerificationToken;
import com.igar15.filecabinet.registration.OnRegistrationCompleteEvent;
import com.igar15.filecabinet.repository.PasswordResetTokenRepository;
import com.igar15.filecabinet.service.UserService;
import com.igar15.filecabinet.util.exception.EmailExistsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.core.env.Environment;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Calendar;
import java.util.UUID;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @Autowired
    private ApplicationEventPublisher eventPublisher;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private Environment env;




    @GetMapping("/signup")
    public String registrationForm(Model model) {
        model.addAttribute("user", new User());
        return "registrationPage";
    }

    @PostMapping("user/register")
    public String registerUser(@Valid User user, BindingResult result, HttpServletRequest request, Model model) {
        if (result.hasErrors()) {
            return "registrationPage";
        }
        try {
            User registered = userService.registerNewUser(user);

            String appUrl = "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
            eventPublisher.publishEvent(new OnRegistrationCompleteEvent(registered, appUrl));
            model.addAttribute("emailMessage", "We sent confirmation-letter to your email. Please confirm it.");

        } catch (EmailExistsException e) {
            result.addError(new FieldError("user", "email", e.getMessage()));
            return "registrationPage";
        }
        return "loginPage";
    }

    @GetMapping("/registrationConfirm")
    public String confirmRegistration(Model model, @RequestParam("token") String token, RedirectAttributes redirectAttributes) {
        VerificationToken verificationToken = userService.getVerificationToken(token);
        if (verificationToken == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Invalid account confirmation token.");
            return "redirect:/login";
        }

        User user = verificationToken.getUser();
        Calendar cal = Calendar.getInstance();
        if ((verificationToken.getExpiryDate()
                .getTime()
                - cal.getTime()
                .getTime()) <= 0) {
            redirectAttributes.addFlashAttribute("errorMessage", "Your registration token has expired. Please register again.");
            return "redirect:/login";
        }

        user.setEnabled(true);
        userService.saveRegisteredUser(user);
        redirectAttributes.addFlashAttribute("message", "Your account verified successfully");
        return "redirect:/login";
    }

    @GetMapping("/forgotPassword")
    public String showForgotPasswordPage() {
        return "forgotPassword";
    }

    @PostMapping("/user/resetPassword")
    public String resetPassword(HttpServletRequest request,
                                @RequestParam("email") String userEmail,
                                RedirectAttributes redirectAttributes) {
        User user = userService.findUserByEmail(userEmail);
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

    @GetMapping("/user/changePassword")
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
        return "resetPassword";
    }

    @PostMapping("/user/savePassword")
    public String savePassword(@RequestParam("password") String password,
                               @RequestParam("passwordConfirmation") String passwordConfirmation,
                               @RequestParam("token") String token,
                               RedirectAttributes redirectAttributes,
                               Model model) {

        if (!password.equals(passwordConfirmation)) {
            model.addAttribute("errorMessage", "Passwords do not match");
            model.addAttribute("token", token);
            return "resetPassword";
        }

        PasswordResetToken p = userService.getPasswordResetToken(token);
        if (p == null) {
            redirectAttributes.addFlashAttribute("message", "Invalid token");
        } else {
            User user = p.getUser();
            if (user == null) {
                redirectAttributes.addFlashAttribute("message", "Unknown user");
            } else {
                userService.changeUserPassword(user, password);
                redirectAttributes.addFlashAttribute("message", "Password reset successfully");
            }
        }
        return "redirect:/login";
    }


    private SimpleMailMessage constructResetTokenEmail(final String contextPath, final String token, final User user) {
        final String url = contextPath + "/user/changePassword?id=" + user.getId() + "&token=" + token;
        final SimpleMailMessage email = new SimpleMailMessage();
        email.setTo(user.getEmail());
        email.setSubject("Reset Password");
        email.setText("Please open the following URL to reset your password: \r\n" + url);
        email.setFrom(env.getProperty("support.email"));
        return email;
    }








}
