package com.rungroup.web.repository;

import com.rungroup.web.dto.ClubDTO;
import com.rungroup.web.models.Club;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClubRepository extends JpaRepository<Club,Long> {
    List<ClubDTO> findByTitle(String url);

    @Query("select c from Club c where c.title LIKE CONCAT('%',:query,'%')")
    List<Club> searchClubs(String query);

}
