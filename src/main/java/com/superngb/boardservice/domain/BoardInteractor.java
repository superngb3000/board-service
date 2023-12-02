package com.superngb.boardservice.domain;

import com.superngb.boardservice.entity.Board;
import com.superngb.boardservice.model.BoardDtoModel;
import com.superngb.boardservice.model.BoardPostModel;
import com.superngb.boardservice.model.BoardUpdateModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class BoardInteractor implements BoardInputBoundary {

    private final BoardDataAccess boardDataAccess;
    private final BoardOutputBoundary boardOutputBoundary;

    public BoardInteractor(BoardDataAccess boardDataAccess, BoardOutputBoundary boardOutputBoundary) {
        this.boardDataAccess = boardDataAccess;
        this.boardOutputBoundary = boardOutputBoundary;
    }

    //TODO проверка на существование creator и users (запрос в user-service)
    @Override
    public BoardDtoModel createBoard(BoardPostModel boardPostModel) {
        if (boardPostModel == null
                || boardPostModel.getName() == null
                || boardPostModel.getDescription() == null
                || boardPostModel.getCreatorId() == null
                || boardPostModel.getUsersId() == null) {
            return boardOutputBoundary.prepareFailPostBoardView();
        }
        if (!boardPostModel.getUsersId().contains(boardPostModel.getCreatorId())){
            boardPostModel.setUsersId(new ArrayList<>(List.of(boardPostModel.getCreatorId())));
        }
        return boardOutputBoundary.prepareSuccessPostBoardView(BoardDtoModel.mapper(
                boardDataAccess.save(Board.builder()
                        .name(boardPostModel.getName())
                        .description(boardPostModel.getDescription())
                        .creatorId(boardPostModel.getCreatorId())
                        .usersId(boardPostModel.getUsersId()).build())
        ));
    }

    @Override
    public BoardDtoModel getBoard(Long id) {
        Board board = boardDataAccess.findById(id);
        return (board == null)
                ? boardOutputBoundary.prepareFailGetBoardView()
                : boardOutputBoundary.prepareSuccessGetBoardView(BoardDtoModel.mapper(board));
    }

    @Override
    public List<BoardDtoModel> getBoards() {
        return boardOutputBoundary.convertUser(BoardDtoModel.mapper(boardDataAccess.getBoards()));
    }

    @Override
    public List<BoardDtoModel> getBoardsByUserId(Long id) {
        return boardOutputBoundary.convertUser(BoardDtoModel.mapper(boardDataAccess.findByUserId(id)));
    }

    //TODO проверка на существование users (запрос в user-service)
    @Override
    public BoardDtoModel updateBoard(BoardUpdateModel boardUpdateModel) {
        Board boardById = boardDataAccess.findById(boardUpdateModel.getId());
        if (boardById == null) {
            return boardOutputBoundary.prepareFailUpdateBoardView();
        }
        List<Long> usersId = new ArrayList<>(boardUpdateModel.getUsersId().stream()
                .filter(Objects::nonNull)
                .sorted()
                .distinct()
                .toList());
        updateFieldIfNotNull(boardUpdateModel.getName(), boardById::getName, boardById::setName);
        updateFieldIfNotNull(boardUpdateModel.getDescription(), boardById::getDescription, boardById::setDescription);
        updateFieldIfNotNull(usersId, boardById::getUsersId, boardById::setUsersId);
        return boardOutputBoundary.prepareSuccessUpdateBoardView(BoardDtoModel.mapper(boardDataAccess.save(boardById)));
    }

    private <T> void updateFieldIfNotNull(T newValue, Supplier<T> currentValueSupplier, Consumer<T> updateFunction) {
        T currentValue = currentValueSupplier.get();
        if (newValue != null && (currentValue == null || !Objects.equals(currentValue, newValue))) {
            updateFunction.accept(newValue);
        }
    }

    //TODO удаление cards по удалению board (запрос в card-service)
    @Override
    public BoardDtoModel deleteBoard(Long id) {
        Board board = boardDataAccess.deleteById(id);
        return (board == null)
                ? boardOutputBoundary.prepareFailDeleteBoardView()
                : boardOutputBoundary.prepareSuccessDeleteBoardView(BoardDtoModel.mapper(board));
    }
}
