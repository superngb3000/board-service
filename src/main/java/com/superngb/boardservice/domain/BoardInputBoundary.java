package com.superngb.boardservice.domain;

import com.superngb.boardservice.model.BoardDtoModel;
import com.superngb.boardservice.model.BoardPostModel;
import com.superngb.boardservice.model.BoardUpdateModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BoardInputBoundary {
    BoardDtoModel createBoard(BoardPostModel boardPostModel);
    BoardDtoModel getBoard(Long id);
    List<BoardDtoModel> getBoards();
    List<BoardDtoModel> getBoardsByUserId(Long id);
    BoardDtoModel updateBoard(BoardUpdateModel boardUpdateModel);
    BoardDtoModel deleteBoard(Long id);
}
