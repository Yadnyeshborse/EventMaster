package com.rungroup.web.repository;

import com.rungroup.web.controller.ImageUploadController;
import com.rungroup.web.models.ImageUploadDTO;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageUpload extends JpaRepository<ImageUploadDTO,Long> {
}
