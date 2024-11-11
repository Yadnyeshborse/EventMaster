package com.rungroup.web.service;

import com.rungroup.web.models.Club;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ClubServices {
    //List<ClubDTO> findAllClubs();
    Club saveClub(Club club);

    Club findClubById(Long id);

    void updateClub(Club club);

    void saveClubs(MultipartFile file, String title, String content, String description);

    Club deleteClub(Long id);

    List<Club> searchClubs(String query);

}
