package com.rungroup.web.controller;

import com.rungroup.web.models.UserEntity;
import com.rungroup.web.repository.BookingRepo;
import com.rungroup.web.repository.UserRepositery;
import com.rungroup.web.security.CustomUserDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserProfileController {

    private final UserRepositery userRepositery;
    private final BookingRepo bookingRepo;
    private final CustomUserDetailService customUserDetailService;

    @Autowired
    public UserProfileController(UserRepositery userRepositery, BookingRepo bookingRepo, CustomUserDetailService customUserDetailService) {
        this.userRepositery = userRepositery;
        this.bookingRepo = bookingRepo;
        this.customUserDetailService = customUserDetailService;
    }

    @GetMapping("/profile")
    public String viewProfile(Authentication authentication, Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepositery.findByEmail(userDetails.getUsername());
        
        if (user != null) {
            // Get user's bookings count
            long userBookingsCount = bookingRepo.count();
            
            model.addAttribute("user", user);
            model.addAttribute("bookingsCount", userBookingsCount);
            return "user-profile";
        }
        
        return "redirect:/login";
    }

    @PostMapping("/profile/update")
    public String updateProfile(Authentication authentication, 
                                @RequestParam(required = false) String userName,
                                @RequestParam(required = false) String email,
                                Model model) {
        if (authentication == null || !authentication.isAuthenticated()) {
            return "redirect:/login";
        }

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        UserEntity user = userRepositery.findByEmail(userDetails.getUsername());
        
        if (user != null) {
            if (userName != null && !userName.trim().isEmpty()) {
                user.setUserName(userName);
            }
            if (email != null && !email.trim().isEmpty()) {
                user.setEmail(email);
            }
            userRepositery.save(user);
            model.addAttribute("success", "Profile updated successfully!");
        }
        
        return "redirect:/profile?updated=true";
    }
}

