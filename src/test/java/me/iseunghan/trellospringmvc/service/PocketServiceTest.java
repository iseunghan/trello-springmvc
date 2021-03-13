package me.iseunghan.trellospringmvc.service;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.*;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class PocketServiceTest {

    @Autowired
    private PocketService pocketService;
    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("포켓 추가")
    void addPocket() throws NotFoundException {
        // given
        Board testBoard = generateBoard();
        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("new Pocket!");

        // when
        Long id = pocketService.addPocket(testBoard.getId(), pocketDto);

        // then
        Pocket savedPocket = pocketService.findOne(id);
        Board savedBoard = savedPocket.getBoard();
        assertEquals(testBoard.getTitle(), savedBoard.getTitle());
        assertEquals(pocketDto.getTitle(), savedPocket.getTitle());
    }

    @Test
    @DisplayName("해당 보드의 모든 포켓 조회")
    void findAll() throws NotFoundException {
        // given
        Board testBoard = generateBoard();
        generatePocket(testBoard, 10);

        // when
        List<Pocket> savedPockets = pocketService.findAll(testBoard.getId());

        // then
        int afterSize = savedPockets.size();

        assertEquals(afterSize, 10);
        assertEquals("pocket title0", savedPockets.get(0).getTitle());
        assertEquals("pocket title1", savedPockets.get(1).getTitle());
        assertEquals("pocket title2", savedPockets.get(2).getTitle());
        assertEquals("pocket title3", savedPockets.get(3).getTitle());
        assertEquals("pocket title4", savedPockets.get(4).getTitle());
        assertEquals("pocket title5", savedPockets.get(5).getTitle());
        assertEquals("pocket title6", savedPockets.get(6).getTitle());
        assertEquals("pocket title7", savedPockets.get(7).getTitle());
        assertEquals("pocket title8", savedPockets.get(8).getTitle());
        assertEquals("pocket title9", savedPockets.get(9).getTitle());
    }

    @Test
    @DisplayName("하나의 포켓 조회")
    void findOne() throws NotFoundException {
        // given
        Board testBoard = generateBoard();
        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("pocket1");
        Long id = pocketService.addPocket(testBoard.getId(), pocketDto);

        // when
        Pocket savedPocket = pocketService.findOne(id);

        // then
        assertEquals("test board title", savedPocket.getBoard().getTitle());
        assertEquals("pocket1", savedPocket.getTitle());
    }

    @Test
    @DisplayName("포켓 수정")
    void updatePocket() throws NotFoundException {
        // given
        Board testBoard = generateBoard();
        PocketDto pocketDto1 = new PocketDto();
        pocketDto1.setTitle("pocket1");
        Long id = pocketService.addPocket(testBoard.getId(), pocketDto1);

        BoardDto newBoard = new BoardDto();
        newBoard.setTitle("new board title");
        newBoard.setBoardColor("RED");
        Long boardId = boardService.addBoard(newBoard);

        PocketDto pocketDto2 = new PocketDto();
        pocketDto2.setTitle("edited title");
        pocketDto2.setPosition(100);
        pocketDto2.setBoardId(boardId);

        // when
        Pocket editedPocket = pocketService.updatePocket(id, pocketDto2);

        // then
        assertEquals("new board title", editedPocket.getBoard().getTitle());
        assertEquals("edited title", editedPocket.getTitle());
        assertEquals(100, editedPocket.getPosition());
    }

    @Test
    @DisplayName("하나의 포켓 삭제")
    void deletePocket() throws NotFoundException {
        // given
        Board testBoard = generateBoard();
        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("pocket1");
        Long id = pocketService.addPocket(testBoard.getId(), pocketDto);

        // when
        boolean result = pocketService.deletePocket(testBoard.getId(), id);

        // then
        NotFoundException nfe = assertThrows(NotFoundException.class, () -> {
            pocketService.findOne(id);
        });
        assertEquals("id: " + id + "은 존재하지 않는 포켓입니다.", nfe.getMessage());
        assertTrue(result);
    }

    /**
     * test용 보드 생성 메소드
     */
    public Board generateBoard() throws NotFoundException {
        BoardDto testBoard = new BoardDto();
        testBoard.setTitle("test board title");
        testBoard.setBoardColor("GREEN");
        testBoard.setPosition(0);

        Long id = boardService.addBoard(testBoard);

        return boardService.findOne(id);
    }

    public void generatePocket(Board board, int size) {
        PocketDto pocketDto;

        for (int i = 0; i < size; i++) {
            pocketDto = new PocketDto();
            pocketDto.setTitle("pocket title" + i);
            pocketService.addPocket(board.getId(), pocketDto);
        }
    }
}