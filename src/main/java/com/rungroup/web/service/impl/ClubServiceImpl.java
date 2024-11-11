package com.rungroup.web.service.impl;
import java.io.IOException;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import com.rungroup.web.dto.ClubDTO;
import com.rungroup.web.models.Club;
import com.rungroup.web.repository.ClubRepository;
import com.rungroup.web.service.ClubServices;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ClubServiceImpl implements ClubServices {


    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    public ClubServiceImpl(ClubRepository clubRepository) {
        this.clubRepository = clubRepository;
    }



//    @Override
//    public List<ClubDTO> findAllClubs() {
//        List<Club> clubs= clubRepository.findAll();
//        return clubs.stream().map(this::mapToClubDTO).collect(Collectors.toList());
//    }

    @Override
    public Club saveClub(Club club) {
        return clubRepository.save(club);
    }




    //update method
    @Override
    public Club findClubById(Long id) {
        Optional<Club> clubOptional = clubRepository.findById(id);
        Club club = clubOptional.orElseThrow(() -> new RuntimeException("Club not found with id: " + id));
        return club;
    }

    @Override
    public void updateClub(Club club) {
        // Fetch the existing club from the database
        Club existingClub = clubRepository.findById(club.getId())
                .orElseThrow(() -> new RuntimeException("Club not found with id: " + club.getId()));

        // Update the fields with the new values
        existingClub.setTitle(club.getTitle());
        existingClub.setDescription(club.getDescription());
        existingClub.setContent(club.getContent());
        existingClub.setPhotoUrl(club.getPhotoUrl());

        // Save the updated club back to the repository


        clubRepository.save(existingClub);
    }


    private Club mapToClub(ClubDTO clubDTO) {
        return Club.builder()
                .id(clubDTO.getId())
                .title(clubDTO.getTitle())
                .photoUrl(clubDTO.getPhotoUrl())
                .content(clubDTO.getContent())
                .build();
    }


    @Override
    public void saveClubs(MultipartFile file, String title,String content, String description) {
        Club DTO = new Club();
        String filename= StringUtils.cleanPath(file.getOriginalFilename());
        if (filename == null || filename.contains("..")) {
            System.out.println("Not a valid File name");
            return; // Exit method if invalid filename
        }
        try {
            DTO.setPhotoUrl(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();

        }
        DTO.setTitle(title);
        DTO.setContent(content);
        DTO.setDescription(description);
        System.out.println("dto is"+DTO);
        clubRepository.save(DTO);
    }

    //delete
    @Override
    public Club deleteClub(Long id) {
        clubRepository.deleteById(id);

        return null;
    }

    @Override
    public List<Club> searchClubs(String query) {
        List<Club> clubs=clubRepository.searchClubs(query);
        return clubs;
    }
}
