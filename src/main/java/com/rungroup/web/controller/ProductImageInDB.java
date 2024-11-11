package com.rungroup.web.controller;

import com.rungroup.web.models.Club;
import com.rungroup.web.models.ImageAndDataSaveInDBDTO;
import com.rungroup.web.repository.ProductRepoImageInDB;
import com.rungroup.web.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Controller
public class ProductImageInDB {

    @Autowired
    private ProductRepoImageInDB productRepoImageInDB;

    @Autowired
    private ProductService productService;

    @GetMapping("/saveProductData")
    public String showAllProduct(Model model){
        ImageAndDataSaveInDBDTO data=new ImageAndDataSaveInDBDTO();
        model.addAttribute("data",data);
        return "saveProductData";

    }

    @PostMapping("/saveProductData")
    public String saveAllProduct(@RequestParam("file")MultipartFile file,
                                 @RequestParam("productName") String productName,
                                 @RequestParam("productDesc") String productDesc,
                                 @RequestParam("price") Long price
                                 ){
        productService.saveAllProduct(file,productName,productDesc,price);
        return "redirect:/saveProductData";
    }

    @GetMapping("/getAllProduct")
    public String getAllData(Model model){
        List<ImageAndDataSaveInDBDTO> list=productRepoImageInDB.findAll();
        model.addAttribute("list",list);
        return "getAllProduct";
    }
}
