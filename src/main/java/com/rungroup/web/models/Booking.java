package com.rungroup.web.models;

import jakarta.persistence.*;
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
    private String firstName;
    private String lastName;
    private String email;
    private String phoneNo;
    private String alterNatePhoneNo;
    private String gender;
    private String address;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)  // foreign key column in the Booking table
    private Event event;
}
