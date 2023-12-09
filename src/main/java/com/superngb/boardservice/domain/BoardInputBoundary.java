package com.superngb.boardservice.domain;

import com.superngb.boardservice.model.BoardPostModel;
import com.superngb.boardservice.model.BoardUpdateModel;
import com.superngb.boardservice.model.ResponseModel;
import org.springframework.stereotype.Component;


@Component
public interface BoardInputBoundary {
    ResponseModel<?> createBoard(BoardPostModel boardPostModel);

    ResponseModel<?> getBoard(Long id);

    ResponseModel<?> getBoards();

    ResponseModel<?> getBoardsByUserId(Long id);

    ResponseModel<?> updateBoard(BoardUpdateModel boardUpdateModel);

    ResponseModel<?> deleteBoard(Long id);

    ResponseModel<?> removeUserFromBoards(Long id);
}
