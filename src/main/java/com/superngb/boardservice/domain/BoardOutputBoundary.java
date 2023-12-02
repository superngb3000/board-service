package com.superngb.boardservice.domain;

import com.superngb.boardservice.model.BoardDtoModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
//TODO переделать
public interface BoardOutputBoundary {
    BoardDtoModel prepareSuccessPostBoardView(BoardDtoModel model);
    BoardDtoModel prepareFailPostBoardView();
    BoardDtoModel prepareSuccessGetBoardView(BoardDtoModel model);
    BoardDtoModel prepareFailGetBoardView();
    BoardDtoModel prepareSuccessUpdateBoardView(BoardDtoModel model);
    BoardDtoModel prepareFailUpdateBoardView();
    BoardDtoModel prepareSuccessDeleteBoardView(BoardDtoModel model);
    BoardDtoModel prepareFailDeleteBoardView();
    List<BoardDtoModel> convertUser(List<BoardDtoModel> modelList);
}
