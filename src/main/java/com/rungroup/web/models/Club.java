package com.rungroup.web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@Entity
@Table(name = "clubs")
@NoArgsConstructor
@AllArgsConstructor
public class Club {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "Title is required")
    private String title;
    
    @Column(length = 10000) // Increased length for base64 encoded images
    private String photoUrl;
    
    @Column(length = 2000)
    private String content;
    
    @Column(length = 2000)
    private String description;

    @CreationTimestamp
    private LocalDateTime creationOn;

    @UpdateTimestamp
    private LocalDateTime updateOn;

    @OneToMany(mappedBy = "club" ,cascade = CascadeType.REMOVE)
    private Set<Event> events=new HashSet<>();

    // Default constructor is provided by Lombok when using @Data

    // Constructor that accepts specific fields


}
