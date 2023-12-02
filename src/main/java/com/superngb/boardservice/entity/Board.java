package com.superngb.boardservice.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "boards")
public class Board {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;

    @Column(name = "creator_id")
    private Long creatorId;

    @ElementCollection(targetClass = Long.class, fetch = FetchType.EAGER)
    @CollectionTable(name = "board_users", joinColumns = @JoinColumn(name = "board_id"))
    private List<Long> usersId = new ArrayList<>();
}
