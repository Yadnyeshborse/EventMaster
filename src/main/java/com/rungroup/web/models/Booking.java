package com.rungroup.web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank(message = "First name is required")
    @Column(length = 100)
    private String firstName;
    
    @NotBlank(message = "Last name is required")
    @Column(length = 100)
    private String lastName;
    
    @NotBlank(message = "Email is required")
    @jakarta.validation.constraints.Email(message = "Email should be valid")
    @Column(length = 200)
    private String email;
    
    @NotBlank(message = "Phone number is required")
    @Column(length = 20)
    private String phoneNo;
    
    @Column(length = 20)
    private String alterNatePhoneNo;
    
    @Column(length = 20)
    private String gender;
    
    @Column(length = 500)
    private String address;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)  // foreign key column in the Booking table
    private Event event;
}
