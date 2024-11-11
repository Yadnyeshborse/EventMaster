package com.rungroup.web.repository;

import com.rungroup.web.models.ImageAndDataSaveInDBDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepoImageInDB extends JpaRepository<ImageAndDataSaveInDBDTO,Long> {
}
