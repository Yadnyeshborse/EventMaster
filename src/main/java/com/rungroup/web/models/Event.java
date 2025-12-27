package com.rungroup.web.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

@NoArgsConstructor
@AllArgsConstructor
@Entity
@Getter
@Setter
@Table(name = "clubs_event_relation")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    
    @NotBlank(message = "Event name is required")
    @Column(length = 500)
    private String name;
    
    @Column(length = 200)
    private String type;
    
    @Column(length = 50)
    private String price;
    
    @NotNull(message = "Start time is required")
    private LocalDateTime starttime;
    
    @NotNull(message = "End time is required")
    private LocalDateTime endtime;
    @CreationTimestamp
    private LocalDateTime createOn;
    @UpdateTimestamp
    private LocalDateTime updateOn;


    @ManyToOne
    @JoinColumn(name = "club_id",nullable = false)
    private Club club;

    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Booking> bookings=new HashSet<>();

}
