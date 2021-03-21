package me.iseunghan.trellospringmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.domain.BoardColor;
import me.iseunghan.trellospringmvc.domain.BoardDto;
import me.iseunghan.trellospringmvc.service.BoardService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class BoardControllerTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("전체 보드 조회")
    void list() throws Exception {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("test Title");
        boardDto.setBoardColor("RED");

        BoardDto boardDto2 = new BoardDto();
        boardDto2.setTitle("test Title2");
        boardDto2.setBoardColor("BLUE");

        BoardDto boardDto3 = new BoardDto();
        boardDto3.setTitle("test Title3");
        boardDto3.setBoardColor("GREEN");

        Long id = boardService.addBoard(boardDto);
        Long id2 = boardService.addBoard(boardDto2);
        Long id3 = boardService.addBoard(boardDto3);

        // when & then
        mockMvc.perform(get("/boards"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.boardResourceList[0].title").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[0].boardColor").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[0]._links.self").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[1].title").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[1].boardColor").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[1]._links.self").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[2].title").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[2].boardColor").exists())
                .andExpect(jsonPath("_embedded.boardResourceList[2]._links.self").exists())
                .andExpect(jsonPath("_links.self").exists())
                ;
    }

    @Test
    @DisplayName("하나의 보드 조회")
    void show() throws Exception {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("board Title");
        boardDto.setBoardColor("BLUE");

        Long id = boardService.addBoard(boardDto);

        // when & then
        mockMvc.perform(get("/boards/{id}",id))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("boardColor").exists())
                .andExpect(jsonPath("_links.self").exists())
                ;
    }

    @Test
    @DisplayName("보드 추가")
    void addBoard() throws Exception {
        // given
        Board board = new Board();
        board.setTitle("first Board");
        board.setBoardColor(BoardColor.BLUE);

        // when & then
        mockMvc.perform(post("/boards")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(board)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("position").exists())
                .andExpect(jsonPath("boardColor").exists())
                .andExpect(jsonPath("createdAt").exists())
                .andExpect(jsonPath("updatedAt").exists())
                .andExpect(jsonPath("pockets").isEmpty())
                .andExpect(jsonPath("_links.self").exists())
                ;
    }

    @Test
    @DisplayName("보드 수정")
    void update() throws Exception {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("first title");
        boardDto.setBoardColor("RED");

        Long id = boardService.addBoard(boardDto);

        BoardDto editBoard = new BoardDto();
        editBoard.setTitle("second title");
        editBoard.setBoardColor("BLUE");
        editBoard.setPosition(100);

        // when & then
        mockMvc.perform(patch("/boards/{id}",id)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(editBoard)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value("second title"))
                .andExpect(jsonPath("boardColor").value("BLUE"))
                .andExpect(jsonPath("position").value(100))
                .andExpect(jsonPath("_links.self").exists())
                ;
    }

    @Test
    @DisplayName("하나의 보드 삭제")
    void deleteTest() throws Exception {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("title");
        boardDto.setBoardColor("RED");

        Long id = boardService.addBoard(boardDto);

        // when & then
        mockMvc.perform(delete("/boards/{id}", id))
                .andDo(print())
                .andExpect(status().isOk())
                ;
    }
}