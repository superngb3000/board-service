package com.superngb.boardservice.controller;


import com.superngb.boardservice.domain.BoardInputBoundary;
import com.superngb.boardservice.model.BoardDtoModel;
import com.superngb.boardservice.model.BoardPostModel;
import com.superngb.boardservice.model.BoardUpdateModel;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
//@RequestMapping("/boards")
public class BoardController {
    private final BoardInputBoundary boardInputBoundary;

    public BoardController(BoardInputBoundary boardInputBoundary) {
        this.boardInputBoundary = boardInputBoundary;
    }

    @PostMapping
    public BoardDtoModel postBoard(@RequestBody BoardPostModel model) {
        return boardInputBoundary.createBoard(model);
    }

    @GetMapping("/{id}")
    public BoardDtoModel getBoard(@PathVariable Long id) {
        return boardInputBoundary.getBoard(id);
    }

    @GetMapping
    public List<BoardDtoModel> getBoards() {
        return boardInputBoundary.getBoards();
    }

    @GetMapping("/user/{id}")
    public List<BoardDtoModel> getBoardsByUserId(@PathVariable Long id){
        return boardInputBoundary.getBoardsByUserId(id);
    }

    @PutMapping
    public BoardDtoModel updateBoard(@RequestBody BoardUpdateModel model) {
        return boardInputBoundary.updateBoard(model);
    }

    @DeleteMapping("/{id}")
    public BoardDtoModel deleteBoard(@PathVariable Long id) {
        return boardInputBoundary.deleteBoard(id);
    }
}
