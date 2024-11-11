package com.rungroup.web.service;

import com.rungroup.web.controller.ProductImageInDB;
import com.rungroup.web.models.ImageAndDataSaveInDBDTO;
import com.rungroup.web.models.ImageUploadDTO;
import com.rungroup.web.repository.ProductRepoImageInDB;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.List;

@Service
public class ProductService {

    @Autowired
    public ProductRepoImageInDB productRepoImageInDB;


    public void saveAllProduct(MultipartFile file, String productName, String productDesc, Long price) {
        ImageAndDataSaveInDBDTO DTO = new ImageAndDataSaveInDBDTO();
        String filename= StringUtils.cleanPath(file.getOriginalFilename());
        if (filename == null || filename.contains("..")) {
            System.out.println("Not a valid File name");
            return; // Exit method if invalid filename
        }
        try {
            DTO.setImage(Base64.getEncoder().encodeToString(file.getBytes()));
        } catch (IOException e) {
            e.printStackTrace();

        }
        DTO.setProductName(productName);
        DTO.setProductDesc(productDesc);
        DTO.setPrice(price);
        productRepoImageInDB.save(DTO);



    }
}
