package com.superngb.boardservice.domain;

import com.superngb.boardservice.entity.Board;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface BoardDataAccess {
    Board save(Board board);

    Board findById(Long id);

    List<Board> findByUserId(Long userId);

    List<Board> getBoards();

    Board deleteById(Long id);
}
