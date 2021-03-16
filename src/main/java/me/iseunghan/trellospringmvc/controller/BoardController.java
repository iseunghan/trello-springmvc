package me.iseunghan.trellospringmvc.controller;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.domain.BoardDto;
import me.iseunghan.trellospringmvc.domain.BoardResource;
import me.iseunghan.trellospringmvc.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping(value = "/boards", produces = MediaType.APPLICATION_JSON_VALUE)
public class BoardController {

    @Autowired
    private BoardService boardService;

    @GetMapping
    public ResponseEntity list() {
        List<Board> boards = boardService.findAll();
        List<BoardResource> resources = new ArrayList<>();

        BoardResource boardResource = null;
        for (Board board : boards) {
            boardResource = new BoardResource(board);
            resources.add(boardResource);
        }

        return ResponseEntity.ok(resources);
    }

    @GetMapping(value = "/{boardId}")
    public ResponseEntity show(@PathVariable Long boardId) throws NotFoundException {
        Board board = boardService.findOne(boardId);
        BoardResource boardResource = new BoardResource(board);

        return ResponseEntity.ok(boardResource);
    }

    @PostMapping
    public ResponseEntity addBoard(@RequestBody BoardDto boardDto) throws NotFoundException {
        System.out.println(boardDto.getBoardColor() + ", " + boardDto.getTitle());
        Long id = boardService.addBoard(boardDto);
        Board board = boardService.findOne(id);
        BoardResource boardResource = new BoardResource(board);
        URI uri = linkTo(BoardController.class).slash(board.getId()).withSelfRel().toUri();

        return ResponseEntity.created(uri).body(boardResource);
    }

    @PatchMapping(value = "/{boardId}")
    public ResponseEntity update(@PathVariable Long boardId, BoardDto boardDto) throws NotFoundException {
        Board board = boardService.updateBoard(boardId, boardDto);
        BoardResource boardResource = new BoardResource(board);

        return ResponseEntity.ok(boardResource);
    }

    @DeleteMapping(value = "/{boardId}")
    public ResponseEntity delete(@PathVariable Long boardId) throws NotFoundException {
        boolean result = boardService.deleteBoard(boardId);

        return ResponseEntity.ok(result);
    }
}
