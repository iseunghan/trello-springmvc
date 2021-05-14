package me.iseunghan.trellospringmvc.controller;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.domain.User;
import me.iseunghan.trellospringmvc.repository.UserRepository;
import me.iseunghan.trellospringmvc.security.SessionUser;
import me.iseunghan.trellospringmvc.service.BoardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;

@Controller
public class HomeController {

    @Autowired
    private HttpSession httpSession;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private BoardService boardService;

    @GetMapping(value = "/")
    public String home(SessionUser sessionUser, Model model) {
        sessionUser = (SessionUser) httpSession.getAttribute("user");
        User user = userRepository.findByEmail(sessionUser.getEmail()).get();

        model.addAttribute("userid", user.getId());
        model.addAttribute("name", user.getName());
        model.addAttribute("email", user.getEmail());
        model.addAttribute("image", user.getImage());

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

    @GetMapping(value = "/custom-login")
    public String customLogin() {

        return "/trello/custom-login.html";
    }
}