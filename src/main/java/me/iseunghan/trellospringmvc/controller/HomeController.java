package me.iseunghan.trellospringmvc.controller;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class HomeController {

    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/")
    public String home() {
        return "/trello/index.html";
    }

    @GetMapping(value = "/boards/{boardId}/detail")
    public String boardPage(@PathVariable Long boardId, Model model) throws NotFoundException {
        Board board = boardService.findOne(boardId);
        model.addAttribute("boardTitle", board.getTitle());
        model.addAttribute("boardId", board.getId());
        model.addAttribute("boardColor", board.getBoardColor());
        return "/trello/boardDetail.html";
    }
}