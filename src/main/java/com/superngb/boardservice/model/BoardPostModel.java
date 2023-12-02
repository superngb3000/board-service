package com.superngb.boardservice.model;

import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardPostModel {
    private String name;
    private String description;
    private Long creatorId;
    private List<Long> usersId;
}
