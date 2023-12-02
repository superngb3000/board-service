package com.superngb.boardservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardUpdateModel {
    private Long id;
    private String name;
    private String description;
    private List<Long> usersId;
}
