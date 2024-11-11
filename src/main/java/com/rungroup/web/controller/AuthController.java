package com.rungroup.web.controller;

import com.rungroup.web.dto.RegistrationDTO;
import com.rungroup.web.models.UserEntity;
import com.rungroup.web.service.UserService;
import com.rungroup.web.service.impl.UserServiceImp;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class AuthController {

    private UserService userService;

    @Autowired
    public AuthController(UserServiceImp userService) {
        this.userService = userService;
    }

    @GetMapping("/register")
    public String getRegisterform(Model model){
        RegistrationDTO registrationDTO = new RegistrationDTO();
        model.addAttribute("user", registrationDTO);
        return "register";
    }

    @PostMapping("/register/save")
    public String register(@Valid @ModelAttribute("user") RegistrationDTO user,
                           BindingResult result, Model model) {
        UserEntity existingUserEmail = userService.findByEmail(user.getEmail());
        if (existingUserEmail != null && existingUserEmail.getEmail() != null && !existingUserEmail.getEmail().isEmpty()) {
            result.rejectValue("email", null, "The user already exists with that email ID.");
        }

        UserEntity existingUsername = userService.findByUsername(user.getUserName());
        if (existingUsername != null && existingUsername.getUserName() != null && !existingUsername.getUserName().isEmpty()) {
            result.rejectValue("userName", null, "The user already exists with that username.");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", user);
            return "register"; // Show the registration form with validation errors
        }

        // Save the user
        userService.saveUser(user);
        return "redirect:/register?success=true"; // Redirect on success
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login";
    }
}
