package com.superngb.boardservice.repository;

import com.superngb.boardservice.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long> {
    Optional<List<Board>> findByUsersId(Long userId);
}
