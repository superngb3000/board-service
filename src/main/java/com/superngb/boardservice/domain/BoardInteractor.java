package com.superngb.boardservice.domain;

import com.superngb.boardservice.client.CardServiceClient;
import com.superngb.boardservice.client.UserServiceClient;
import com.superngb.boardservice.entity.Board;
import com.superngb.boardservice.model.BoardDtoModel;
import com.superngb.boardservice.model.BoardPostModel;
import com.superngb.boardservice.model.BoardUpdateModel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class BoardInteractor implements BoardInputBoundary {

    private final BoardDataAccess boardDataAccess;
    private final BoardOutputBoundary boardOutputBoundary;
    private final CardServiceClient cardServiceClient;
    private final UserServiceClient userServiceClient;

    public BoardInteractor(BoardDataAccess boardDataAccess,
                           BoardOutputBoundary boardOutputBoundary,
                           CardServiceClient cardServiceClient,
                           UserServiceClient userServiceClient) {
        this.boardDataAccess = boardDataAccess;
        this.boardOutputBoundary = boardOutputBoundary;
        this.cardServiceClient = cardServiceClient;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public BoardDtoModel createBoard(BoardPostModel boardPostModel) {
        if (!userServiceClient.userExists(boardPostModel.getCreatorId())) {
            return boardOutputBoundary.prepareFailPostBoardView();
        }
        List<Long> usersId = new ArrayList<>(boardPostModel.getUsersId().stream()
                .filter(u -> u != null && userServiceClient.userExists(u))
                .sorted()
                .distinct()
                .toList());
        if (!usersId.contains(boardPostModel.getCreatorId())) {
            usersId.add(boardPostModel.getCreatorId());
        }
        return boardOutputBoundary.prepareSuccessPostBoardView(BoardDtoModel.mapper(
                boardDataAccess.save(Board.builder()
                        .name(boardPostModel.getName())
                        .description(boardPostModel.getDescription())
                        .creatorId(boardPostModel.getCreatorId())
                        .usersId(usersId).build())
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

    @Override
    public BoardDtoModel updateBoard(BoardUpdateModel boardUpdateModel) {
        Board boardById = boardDataAccess.findById(boardUpdateModel.getId());
        if (boardById == null) {
            return boardOutputBoundary.prepareFailUpdateBoardView();
        }
        List<Long> usersId = new ArrayList<>(boardUpdateModel.getUsersId().stream()
                .filter(u -> u != null && userServiceClient.userExists(u))
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

    @Override
    public BoardDtoModel deleteBoard(Long id) {
        Board board = boardDataAccess.deleteById(id);
        if (board == null) {
            return boardOutputBoundary.prepareFailDeleteBoardView();
        }
        cardServiceClient.deleteCardsByBoard(id);
        return boardOutputBoundary.prepareSuccessDeleteBoardView(BoardDtoModel.mapper(board));
    }

    @Override
    public void removeUserFromBoards(Long id) {
        List<Board> boardList = boardDataAccess.findByUserId(id);
        boardList.forEach(board -> {
            List<Long> usersId= board.getUsersId();
            usersId.remove(id);
            board.setUsersId(usersId);
            boardDataAccess.save(board);
        });
    }

    @Override
    public boolean boardExists(Long id) {
        return (boardDataAccess.findById(id) != null)
                ? boardOutputBoundary.prepareBoardExistsView()
                : boardOutputBoundary.prepareBoardDoesNotExistView();
    }
}
