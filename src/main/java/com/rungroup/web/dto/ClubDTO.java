package com.rungroup.web.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class ClubDTO {
    private long id;
    @NotEmpty(message = "Club Title should not be empty")
    private String title;
    private String photoUrl;
    @NotEmpty(message = "Club Content should not be empty")
    private String content;
    private LocalDateTime creationOn;
    private LocalDateTime updateOn;



}
