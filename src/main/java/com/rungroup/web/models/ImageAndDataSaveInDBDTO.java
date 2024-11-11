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
    private String productName;
    private String productDesc;
    private Long price;
    private String image;
}
