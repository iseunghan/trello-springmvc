package me.iseunghan.trellospringmvc.domain;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import me.iseunghan.trellospringmvc.controller.BoardController;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Component
public class BoardResource extends RepresentationModel {

    @Autowired
    private ModelMapper modelMapper;

    @JsonUnwrapped
    private BoardDto boardDto;

    public BoardResource() {

    }

    public BoardResource(BoardDto boardDto, Long userId) {
        super.add(linkTo(BoardController.class, userId).slash(boardDto.getBoardId()).withSelfRel());
        this.boardDto = boardDto;
    }

    public BoardResource toModel(Board board, Long userId) {
        BoardDto boardDto = modelMapper.map(board, BoardDto.class);
        return new BoardResource(boardDto, userId);
    }

    public CollectionModel<BoardResource> toCollectionModel(List<Board> boards, Long userId) {
        List<BoardResource> boardResources = new ArrayList<>();
        List<BoardDto> boardDtoList = boards
                .stream()
                .map(board -> modelMapper.map(board, BoardDto.class))
                .collect(Collectors.toList());
        boardDtoList
                .stream()
                .forEach(b -> boardResources.add(new BoardResource(b, userId)));

        CollectionModel<BoardResource> collectionModel = CollectionModel.of(boardResources);
        Link link = linkTo(BoardController.class, userId).withSelfRel();
        collectionModel.add(link);
        return collectionModel;
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }
}
