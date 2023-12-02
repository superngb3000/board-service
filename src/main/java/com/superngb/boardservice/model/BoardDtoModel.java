package com.superngb.boardservice.model;

import com.superngb.boardservice.entity.Board;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDtoModel {
    private Long id;
    private String name;
    private String description;
    private Long creatorId;
    private List<Long> usersId;

    public static BoardDtoModel mapper(Board board) {
        return new BoardDtoModel(
                board.getId(),
                board.getName(),
                board.getDescription(),
                board.getCreatorId(),
                board.getUsersId());
    }

    public static List<BoardDtoModel> mapper(List<Board> boardList){
        return boardList.stream()
                .map(BoardDtoModel::mapper)
                .toList();
    }
}
