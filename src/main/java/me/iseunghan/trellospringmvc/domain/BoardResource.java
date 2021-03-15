package me.iseunghan.trellospringmvc.domain;

import me.iseunghan.trellospringmvc.controller.BoardController;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class BoardResource extends RepresentationModel {

    private Board board;

    public BoardResource(Board board) {
        this.board = board;
        super.add(linkTo(BoardController.class).slash(board.getId()).withSelfRel());
    }

    public Board getBoard() {
        return board;
    }
}
