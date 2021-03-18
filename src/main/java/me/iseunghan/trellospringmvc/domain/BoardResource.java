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

    public BoardResource(BoardDto boardDto) {
        super.add(linkTo(BoardController.class).slash(boardDto.getBoardId()).withSelfRel());
        this.boardDto = boardDto;
    }

    public BoardResource toModel(Board board) {
        BoardDto boardDto = modelMapper.map(board, BoardDto.class);
        return new BoardResource(boardDto);
    }

    public CollectionModel<BoardResource> toCollectionModel(List<Board> boards) {
        List<BoardResource> boardResources = new ArrayList<>();
        List<BoardDto> boardDtoList = boards
                .stream()
                .map(board -> modelMapper.map(board, BoardDto.class))
                .collect(Collectors.toList());
        boardDtoList
                .stream()
                .forEach(b -> boardResources.add(new BoardResource(b)));

        CollectionModel<BoardResource> collectionModel = CollectionModel.of(boardResources);
        Link link = linkTo(BoardController.class).withSelfRel();
        collectionModel.add(link);
        return collectionModel;
    }

    public BoardDto getBoardDto() {
        return boardDto;
    }
}
