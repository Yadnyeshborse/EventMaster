package com.rungroup.web.controller;

import com.rungroup.web.dto.ClubDTO;
import com.rungroup.web.models.Club;
import com.rungroup.web.models.ImageUploadDTO;
import com.rungroup.web.repository.ClubRepository;
import com.rungroup.web.service.ClubServices;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Controller
public class ClubController {
    @Autowired
    private final ClubRepository clubRepository;

    private final ClubServices clubServices;

    @Autowired
    public ClubController(ClubRepository clubRepository, ClubServices clubServices) {
        this.clubRepository = clubRepository;
        this.clubServices = clubServices;
    }




//    @GetMapping("/clubslist")
//    public String listClubs(Model model){
//        List<ClubDTO> clubs =clubServices.findAllClubs();
//        model.addAttribute("clubs",clubs);
//            return "clubs-list";
//    }
//____________________________________________________________________________________________
    @GetMapping("/clubs/{id}/edit")
    public String editClubForm(@PathVariable("id") Long id,Model model){
        Club club=clubServices.findClubById(id);
        model.addAttribute("club",club);
        return "clubs-edit";

    }


    @PostMapping("clubs/{id}/edit")
    public String editClubForm(@PathVariable("id") Long id,@RequestParam("photo") MultipartFile photo,
                               @Valid @ModelAttribute("club") Club club
    , BindingResult result){

        if(result.hasErrors()){
            return "clubs-edit";
        }

        // If an image is provided, update it
        //if we try to change any tesxt other than image than it is is not showing image so below code need ed to be added
        if (!photo.isEmpty()) {
            try {
                club.setPhotoUrl(Base64.getEncoder().encodeToString(photo.getBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } else {
            // Fetch the existing club and retain the existing photoUrl if no new image was uploaded
            Club existingClub = clubServices.findClubById(id);
            club.setPhotoUrl(existingClub.getPhotoUrl());
        }


        club.setId(id);
        clubServices.updateClub(club);

        return "redirect:/getAllClub";
    }
//____________________________________________________________________________________________________________
    @GetMapping("/saveClubs")
    public String saveClubs(Model model){
        Club club=new Club();
        model.addAttribute(club);
        return "saveClubs";
    }

    @PostMapping("/saveClubs")
    public String saveClubs(@RequestParam("file") MultipartFile file,
                                 @RequestParam("title") String title,
                                 @RequestParam("content") String content,
                            @RequestParam("description") String description
    ){
        clubServices.saveClubs(file,title,content,description);
        return "redirect:/getAllClub";
    }
//_____________________________________________________________________________________________________________
    @GetMapping("/getAllClub")
    public String getAllClub(Model model){
        List<Club> clubs=clubRepository.findAll();
        model.addAttribute("clubs",clubs);
        return "getAllClub";
    }

    //getmapping for list  edit purpose
    @GetMapping("/clubs/{id}")
    public String getAllClub(@PathVariable("id") Long id,Model model){
        Club club=clubServices.findClubById(id);
        model.addAttribute("club",club);
        return "clud-details";
    }

    @GetMapping("/clubs/{id}/delete")
    public String deleteClub(@PathVariable("id")Long id,Model model){
       Club club1= clubServices.deleteClub(id);
       model.addAttribute("club",club1);
        return "redirect:/getAllClub";
    }

    //search

    @GetMapping("/clubs/search")
    public String searchClub(@RequestParam(value = "query")String query,Model model){
        List<Club> clubs=clubServices.searchClubs(query);
        model.addAttribute("clubs",clubs);
        return "getAllClub";
    }

    //+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

    @GetMapping("/")
    public String index(Model model) {
        List<Club> list=clubRepository.findAll();
        model.addAttribute("list",list);
        return "layout";
    }







}
