package com.superngb.boardservice.database;

import com.superngb.boardservice.domain.BoardDataAccess;
import com.superngb.boardservice.entity.Board;
import com.superngb.boardservice.repository.BoardRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class BoardDataAccessImpl implements BoardDataAccess {

    private final BoardRepository boardRepository;

    public BoardDataAccessImpl(BoardRepository boardRepository) {
        this.boardRepository = boardRepository;
    }

    @Override
    public Board save(Board board) {
        return boardRepository.save(board);
    }

    @Override
    public Board findById(Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    @Override
    public List<Board> findByUserId(Long userId) {
        return boardRepository.findByUsersId(userId).orElse(null);
    }

    @Override
    public List<Board> getBoards() {
        return boardRepository.findAll();
    }

    @Override
    public Board deleteById(Long id) {
        Optional<Board> optionalBoard = boardRepository.findById(id);
        if (optionalBoard.isPresent()) {
            boardRepository.deleteById(id);
            return optionalBoard.get();
        }
        return null;
    }
}
