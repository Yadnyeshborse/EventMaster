package com.rungroup.web.controller;

import com.rungroup.web.models.ImageUploadDTO;
import com.rungroup.web.repository.ImageUpload;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
public class ImageUploadController {

    @Autowired
    private ImageUpload imageUploadRepo;

    @GetMapping("/indexo")
    public String index(Model model) {
        List<ImageUploadDTO> list=imageUploadRepo.findAll();
        model.addAttribute("list",list);
        return "indexo";
    }

    @PostMapping("/imageUpload")
    public String imageUpload(@RequestParam("img") MultipartFile img, HttpSession session) {
        ImageUploadDTO DTO = new ImageUploadDTO();
        DTO.setImageName(img.getOriginalFilename());
        ImageUploadDTO savedImage = imageUploadRepo.save(DTO);
        System.out.println(savedImage);

        if (savedImage != null) {
            try {
                // Specify the directory where the file should be saved
                File saveDir = new ClassPathResource("static/DemoImg").getFile();

                // Create the path to save the image file
                Path path = Paths.get(saveDir.getAbsolutePath() + File.separator + img.getOriginalFilename());
                System.out.println(path);

                // Copy the uploaded file to the specified path
                Files.copy(img.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            } catch (IOException e) {
                e.printStackTrace();
                session.setAttribute("fail", "Image upload failed. Please try again.");
                return "error";
            }

        }
        session.setAttribute("msg","Image Upload sucessfully");
        session.removeAttribute("remove");


        return "redirect:/"; // Redirects to the home page or another success page
    }
}
