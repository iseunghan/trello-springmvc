package me.iseunghan.trellospringmvc.controller;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.domain.BoardDto;
import me.iseunghan.trellospringmvc.domain.BoardResource;
import me.iseunghan.trellospringmvc.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/user/{userId}/boards", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    @Autowired
    private BoardService boardService;
    @Autowired
    private BoardResource boardResource;

    @GetMapping
    public ResponseEntity list(@PathVariable Long userId) {
        List<Board> boards = boardService.findAll(userId);
        CollectionModel<BoardResource> boardResources = boardResource.toCollectionModel(boards, userId);
        return ResponseEntity.ok(boardResources);
    }

    @GetMapping(value = "/{boardId}")
    public ResponseEntity show(@PathVariable Long userId, @PathVariable Long boardId) throws NotFoundException {
        Board board = boardService.findOne(boardId);
        BoardResource resource = boardResource.toModel(board, userId);
        return ResponseEntity.ok(resource);
    }

    @PostMapping
    public ResponseEntity addBoard(@PathVariable Long userId, @RequestBody BoardDto boardDto) throws NotFoundException {
        System.out.println("UserId: " + userId + ", " + boardDto.getBoardColor() + ", " + boardDto.getTitle());
        Long id = boardService.addBoard(boardDto, userId);
        Board board = boardService.findOne(id);
        BoardResource resource = boardResource.toModel(board, userId);
        URI uri = linkTo(BoardController.class, userId).slash(board.getId()).withSelfRel().toUri();
        return ResponseEntity.created(uri).body(resource);
    }

    @PatchMapping(value = "/{boardId}")
    public ResponseEntity update(@PathVariable Long userId, @PathVariable Long boardId, @RequestBody BoardDto boardDto) throws NotFoundException {
        System.out.println(boardDto.getTitle() + " , " + boardDto.getBoardColor());
        Board board = boardService.updateBoard(boardId, boardDto);
        BoardResource resource = boardResource.toModel(board, userId);
        return ResponseEntity.ok(resource);
    }

    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity delete(@PathVariable Long boardId) throws NotFoundException {
        boolean result = boardService.deleteBoard(boardId);
        return ResponseEntity.ok(result);
    }
}
