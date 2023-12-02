package com.superngb.boardservice.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostModel {
    @NotBlank
    private String name;
    private String description;
    @NotNull
    private Long creatorId;
    private List<Long> usersId;
}
