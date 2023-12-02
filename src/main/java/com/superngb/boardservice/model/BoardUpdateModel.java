package com.superngb.boardservice.model;

import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateModel {
    @NotNull
    private Long id;
    private String name;
    private String description;
    private List<Long> usersId;
}
