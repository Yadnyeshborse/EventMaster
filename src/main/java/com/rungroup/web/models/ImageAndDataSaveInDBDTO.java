package com.rungroup.web.models;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ImageInDB_ImageAndDataSaveInDBDTO")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ImageAndDataSaveInDBDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @Column(length = 200)
    private String productName;
    
    @Column(length = 1000)
    private String productDesc;
    
    private Long price;
    
    @Column(length = 10000) // Increased length for base64 encoded images
    private String image;
}
