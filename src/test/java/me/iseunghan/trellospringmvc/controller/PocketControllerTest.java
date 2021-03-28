package me.iseunghan.trellospringmvc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.iseunghan.trellospringmvc.domain.BoardColor;
import me.iseunghan.trellospringmvc.domain.BoardDto;
import me.iseunghan.trellospringmvc.domain.PocketDto;
import me.iseunghan.trellospringmvc.service.BoardService;
import me.iseunghan.trellospringmvc.service.PocketService;
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
class PocketControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PocketService pocketService;
    @Autowired
    private BoardService boardService;

    public Long generateBoard() {
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("Test Board Title");
        boardDto.setBoardColor(BoardColor.BLUE);
        return boardService.addBoard(boardDto);
    }

    @Test
    void list() throws Exception {
        // given
        Long boardId = generateBoard();

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("pocket title");

        PocketDto pocketDto2 = new PocketDto();
        pocketDto2.setTitle("pocket title2");

        PocketDto pocketDto3 = new PocketDto();
        pocketDto3.setTitle("pocket title3");

        pocketService.addPocket(boardId, pocketDto);
        pocketService.addPocket(boardId, pocketDto2);
        pocketService.addPocket(boardId, pocketDto3);

        // when & then
        mockMvc.perform(get("/boards/{boardId}/pockets", boardId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("_embedded.pocketResourceList[0]._links.self").exists())
                ;

    }

    @Test
    void findOne() throws Exception {
        // given
        Long boardId = generateBoard();

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("pocket title1");

        Long pocketId = pocketService.addPocket(boardId, pocketDto);

        // when & then
        mockMvc.perform(get("/boards/{boardId}/pockets/{pocketId}",boardId, pocketId))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").exists())
                .andExpect(jsonPath("_links.self").exists())
                ;
    }

    @Test
    void addPocket() throws Exception {
        // given
        Long boardId = generateBoard();
        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("pocket Title");

        // when & then
        mockMvc.perform(post("/boards/{boardId}/pockets", boardId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(pocketDto)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("title").value("pocket Title"))
                .andExpect(jsonPath("position").exists())
                .andExpect(jsonPath("boardId").exists())
                .andExpect(jsonPath("cards").isEmpty())
                .andExpect(jsonPath("_links.self").exists())
                ;
    }

    @Test
    void update() throws Exception {
        // given
        Long boardId = generateBoard();
        Long diffBoardId = generateBoard();

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("pocket title");

        Long pocketId = pocketService.addPocket(boardId, pocketDto);

        // when & then
        PocketDto updatePocket = new PocketDto();
        updatePocket.setTitle("edited title");
        updatePocket.setPosition(100);
        updatePocket.setBoardId(diffBoardId);

        mockMvc.perform(patch("/boards/{boardId}/pockets/{pocketId}", boardId, pocketId)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(updatePocket)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("title").value(updatePocket.getTitle()))
                .andExpect(jsonPath("position").value(updatePocket.getPosition()))
                .andExpect(jsonPath("boardId").value(diffBoardId))
                ;
    }

    @Test
    void deleteOne() throws Exception {
        // given
        Long boardId = generateBoard();

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("board Title");

        Long pocketId = pocketService.addPocket(boardId, pocketDto);

        // when & then
        mockMvc.perform(delete("/boards/{boardId}/pockets/{pocketId}", boardId, pocketId))
                .andDo(print())
                .andExpect(status().isOk())
                ;
    }
}