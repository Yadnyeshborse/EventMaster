package com.rungroup.web.controller;

import com.rungroup.web.models.Club;

import com.rungroup.web.service.ClubServices;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class PostMethod {

    private final ClubServices clubServices;

    public PostMethod(ClubServices clubServices) {
        this.clubServices = clubServices;
    }



    @GetMapping("/post")
    public String getData(Model model){
        Club clubform=new Club();
        model.addAttribute("clubForm",clubform);
        return "post";
    }

    @PostMapping("/post")
    public String submitForm(@ModelAttribute("clubForm") Club club){  //fetching userform data from register form declare in body
        clubServices.saveClub(club);
        return "redirect:/post";  // Corrected line
    }


}
