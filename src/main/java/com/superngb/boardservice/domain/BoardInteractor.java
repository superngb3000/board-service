package com.superngb.boardservice.domain;

import com.superngb.boardservice.client.CardServiceClient;
import com.superngb.boardservice.client.UserServiceClient;
import com.superngb.boardservice.entity.Board;
import com.superngb.boardservice.model.BoardDtoModel;
import com.superngb.boardservice.model.BoardPostModel;
import com.superngb.boardservice.model.BoardUpdateModel;
import com.superngb.boardservice.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

@Service
public class BoardInteractor implements BoardInputBoundary {

    private final BoardDataAccess boardDataAccess;
    private final CardServiceClient cardServiceClient;
    private final UserServiceClient userServiceClient;

    public BoardInteractor(BoardDataAccess boardDataAccess,
                           CardServiceClient cardServiceClient,
                           UserServiceClient userServiceClient) {
        this.boardDataAccess = boardDataAccess;
        this.cardServiceClient = cardServiceClient;
        this.userServiceClient = userServiceClient;
    }

    @Override
    public ResponseModel<?> createBoard(BoardPostModel boardPostModel) {
        ResponseEntity<?> responseEntity = userServiceClient.getUser(boardPostModel.getCreatorId());
        if (!responseEntity.getStatusCode().equals(HttpStatus.valueOf(200))) {
            return ResponseModel.builder().code(403).body("User with userId = " + boardPostModel.getCreatorId().toString() + " does not exist").build();
        }
        List<Long> usersId;
        if (boardPostModel.getUsersId() != null) {
            usersId = boardPostModel.getUsersId();
            usersId.add(boardPostModel.getCreatorId());
            usersId = usersId.stream()
                    .filter(u -> u != null
                            && userServiceClient.getUser(u).getStatusCode().equals(HttpStatus.valueOf(200)))
                    .sorted()
                    .distinct()
                    .toList();
        } else {
            usersId = new ArrayList<>(List.of(boardPostModel.getCreatorId()));
        }
        return ResponseModel.builder().code(201).body(
                BoardDtoModel.mapper(
                        boardDataAccess.save(Board.builder()
                                .name(boardPostModel.getName())
                                .description(boardPostModel.getDescription())
                                .creatorId(boardPostModel.getCreatorId())
                                .usersId(usersId).build()))
        ).build();
    }

    @Override
    public ResponseModel<?> getBoard(Long id) {
        Board board = boardDataAccess.findById(id);
        return (board == null)
                ? ResponseModel.builder().code(404).body("Board with boardId = " + id.toString() + " not found").build()
                : ResponseModel.builder().code(200).body(BoardDtoModel.mapper(board)).build();
    }

    @Override
    public ResponseModel<?> getBoards() {
        return ResponseModel.builder().code(200).body(BoardDtoModel.mapper(boardDataAccess.getBoards())).build();
    }

    @Override
    public ResponseModel<?> getBoardsByUserId(Long id) {
        List<Board> boardList = boardDataAccess.findByUserId(id);
        return (boardList == null)
                ? ResponseModel.builder().code(404).body("There are no boards with user with userId = " + id.toString()).build()
                : ResponseModel.builder().code(200).body(BoardDtoModel.mapper(boardList)).build();
    }

    @Override
    public ResponseModel<?> updateBoard(BoardUpdateModel boardUpdateModel) {
        Board boardById = boardDataAccess.findById(boardUpdateModel.getId());
        if (boardById == null) {
            return ResponseModel.builder().code(404).body("Board with boardId = " + boardUpdateModel.getId().toString() + " not found").build();
        }
        updateFieldIfNotNull(boardUpdateModel.getName(), boardById::getName, boardById::setName);
        updateFieldIfNotNull(boardUpdateModel.getDescription(), boardById::getDescription, boardById::setDescription);
        if (boardUpdateModel.getUsersId() != null) {
            List<Long> usersId = new ArrayList<>(boardUpdateModel.getUsersId().stream()
                    .filter(u -> u != null
                            && userServiceClient.getUser(u).getStatusCode().equals(HttpStatus.valueOf(200)))
                    .sorted()
                    .distinct()
                    .toList());
            updateFieldIfNotNull(usersId, boardById::getUsersId, boardById::setUsersId);
        }
        return ResponseModel.builder().code(200).body(BoardDtoModel.mapper(boardDataAccess.save(boardById))).build();
    }

    private <T> void updateFieldIfNotNull(T newValue, Supplier<T> currentValueSupplier, Consumer<T> updateFunction) {
        T currentValue = currentValueSupplier.get();
        if (newValue != null && (currentValue == null || !Objects.equals(currentValue, newValue))) {
            updateFunction.accept(newValue);
        }
    }

    @Override
    public ResponseModel<?> deleteBoard(Long id) {
        Board board = boardDataAccess.deleteById(id);
        if (board == null) {
            return ResponseModel.builder().code(404).body("Board with boardId = " + id.toString() + " not found").build();
        }
        cardServiceClient.deleteCardsByBoard(id);
        return ResponseModel.builder().code(200).body(BoardDtoModel.mapper(board)).build();
    }

    @Override
    public ResponseModel<?> removeUserFromBoards(Long id) {
        ResponseEntity<?> responseEntity = userServiceClient.getUser(id);
        if (!responseEntity.getStatusCode().equals(HttpStatus.valueOf(200))) {
            return ResponseModel.builder().code(404).body("User with userId = " + id.toString() + " does not exist").build();
        }
        List<Board> boardList = boardDataAccess.findByUserId(id);
        boardList.forEach(board -> {
            List<Long> usersId = board.getUsersId();
            usersId.remove(id);
            board.setUsersId(usersId);
            boardDataAccess.save(board);
        });
        return ResponseModel.builder().code(200).body(BoardDtoModel.mapper(boardList)).build();
    }
}
