package com.superngb.boardservice.presenter;

import com.superngb.boardservice.domain.BoardOutputBoundary;
import com.superngb.boardservice.model.BoardDtoModel;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class BoardPresenter implements BoardOutputBoundary {
    @Override
    public BoardDtoModel prepareSuccessPostBoardView(BoardDtoModel model) {
        return model;
    }

    @Override
    public BoardDtoModel prepareFailPostBoardView() {
        return null;
    }

    @Override
    public BoardDtoModel prepareSuccessGetBoardView(BoardDtoModel model) {
        return model;
    }

    @Override
    public BoardDtoModel prepareFailGetBoardView() {
        return null;
    }

    @Override
    public BoardDtoModel prepareSuccessUpdateBoardView(BoardDtoModel model) {
        return model;
    }

    @Override
    public BoardDtoModel prepareFailUpdateBoardView() {
        return null;
    }

    @Override
    public BoardDtoModel prepareSuccessDeleteBoardView(BoardDtoModel model) {
        return model;
    }

    @Override
    public BoardDtoModel prepareFailDeleteBoardView() {
        return null;
    }

    @Override
    public List<BoardDtoModel> convertUser(List<BoardDtoModel> modelList) {
        return modelList;
    }

    @Override
    public boolean prepareBoardExistsView() {
        return true;
    }

    @Override
    public boolean prepareBoardDoesNotExistView() {
        return false;
    }
}
