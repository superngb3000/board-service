package com.superngb.boardservice.controller;


import com.superngb.boardservice.domain.BoardInputBoundary;
import com.superngb.boardservice.model.BoardPostModel;
import com.superngb.boardservice.model.BoardUpdateModel;
import com.superngb.boardservice.model.ResponseModel;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/")
public class BoardController {
    private final BoardInputBoundary boardInputBoundary;

    public BoardController(BoardInputBoundary boardInputBoundary) {
        this.boardInputBoundary = boardInputBoundary;
    }

    @PostMapping
    public ResponseEntity<?> postBoard(@RequestBody @Valid BoardPostModel model) {
        ResponseModel<?> response = boardInputBoundary.createBoard(model);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getBoard(@PathVariable Long id) {
        ResponseModel<?> response = boardInputBoundary.getBoard(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping
    public ResponseEntity<?> getBoards() {
        ResponseModel<?> response = boardInputBoundary.getBoards();
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<?> getBoardsByUserId(@PathVariable Long id) {
        ResponseModel<?> response = boardInputBoundary.getBoardsByUserId(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping
    public ResponseEntity<?> updateBoard(@RequestBody @Valid BoardUpdateModel model) {
        ResponseModel<?> response = boardInputBoundary.updateBoard(model);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteBoard(@PathVariable Long id) {
        ResponseModel<?> response = boardInputBoundary.deleteBoard(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }

    @PutMapping("/removeUser/{id}")
    public ResponseEntity<?> removeUserFromBoards(@PathVariable Long id) {
        ResponseModel<?> response = boardInputBoundary.removeUserFromBoards(id);
        return new ResponseEntity<>(response.getBody(), HttpStatus.valueOf(response.getCode()));
    }
}
