package me.iseunghan.trellospringmvc.service;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.domain.BoardColor;
import me.iseunghan.trellospringmvc.domain.BoardDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.awt.print.Pageable;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class BoardServiceTest {

    @Autowired
    private BoardService boardService;

    @Test
    @DisplayName("하나의 보드 추가")
    void addBoard() throws NotFoundException {
        // given
        Board board = new Board();
        board.setTitle("test board");
        board.setBoardColor(BoardColor.BLUE);

        // when
        Long id = boardService.addBoard(board);

        // then
        Board findBoard = boardService.findOne(id);
        assertEquals("test board", findBoard.getTitle());
        assertEquals(BoardColor.BLUE, findBoard.getBoardColor());
        assertEquals(findBoard.getCreatedAt(), findBoard.getUpdatedAt());
    }

    @Test
    @DisplayName("하나의 보드 추가 타이틀 비었을 때")
    void addBoardException() {
        // given
        Board board = new Board();

        // when
        IllegalStateException ise = assertThrows(IllegalStateException.class, () -> {
            boardService.addBoard(board);
        });

        // then
        assertEquals("공백을 입력할 수 없습니다.", ise.getMessage());
    }


    @Test
    @DisplayName("모든 보드 조회")
    void findAll() {
        // given
        int prevSize = boardService.findAll().size();
        int plusSize = 5;
        generateBoard(plusSize);
        int totalSize = prevSize + plusSize;

        // when
        List<Board> boards = boardService.findAll();

        // then
        assertEquals(totalSize, boards.size());
        assertEquals("test board1", boards.get(0 + prevSize).getTitle());
        assertEquals("test board2", boards.get(1 + prevSize).getTitle());
        assertEquals("test board3", boards.get(2 + prevSize).getTitle());
        assertEquals("test board4", boards.get(3 + prevSize).getTitle());
        assertEquals("test board5", boards.get(4 + prevSize).getTitle());
    }

    @Test
    @DisplayName("하나의 보드 조회")
    void findOne() throws NotFoundException {
        // given
        Board board = new Board();
        board.setTitle("test board");
        Long id = boardService.addBoard(board);

        // when
        Board findBoard = boardService.findOne(id);

        // then
        assertEquals(board.getId(), findBoard.getId());
        assertEquals(board.getTitle(), findBoard.getTitle());
    }

    @Test
    @DisplayName("존재하지 않는 보드 조회")
    void findOne404() {
        // given
        Long notFoundId = 100L;

        // when
        NotFoundException notFoundException = assertThrows(NotFoundException.class, () ->
                boardService.findOne(notFoundId));

        // then
        assertEquals("id: " + notFoundId + "는 존재하지 않는 보드입니다.", notFoundException.getMessage());
    }

    @Test
    @DisplayName("보드 수정")
    void updateBoard() throws NotFoundException {
        // given
        Board board = new Board();
        board.setTitle("test board");
        board.setBoardColor(BoardColor.BLUE);
        boardService.addBoard(board);

        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("edited board");
        boardDto.setBoardColor(BoardColor.RED);
        boardDto.setPosition(10);

        // when
        Board updateBoard = boardService.updateBoard(board.getId(), boardDto);

        // then
        assertEquals(boardDto.getTitle(), updateBoard.getTitle());
        assertEquals(boardDto.getBoardColor(), updateBoard.getBoardColor());
        assertEquals(boardDto.getPosition(), updateBoard.getPosition());
    }

    @Test
    @DisplayName("보드 삭제")
    void deleteBoard() throws NotFoundException {
        // given
        Board board = new Board();
        board.setTitle("test board");
        Long id = boardService.addBoard(board);

        // when
        boolean result = boardService.deleteBoard(id);
        // then
        assertTrue(result);
    }

    @Test
    @DisplayName("존재하지 않는 보드 삭제 NotFoundException 터지는지 확인")
    void deleteBoardNotFoundEx() {

        NotFoundException nfe = assertThrows(NotFoundException.class, () -> {
            boardService.deleteBoard(100L);
        });

        assertEquals("id: 100는 존재하지 않는 보드입니다.", nfe.getMessage());
    }

    /**
     * n개의 board를 생성 후 저장해주는 메소드
     *
     * @param n
     */
    public void generateBoard(int n) {
        Board board;
        for (int i = 1; i <= n; i++) {
            board = new Board();
            board.setTitle("test board" + i);
            board.setBoardColor(BoardColor.BLUE);
            board.setPosition(i);
            boardService.addBoard(board);
        }
    }
}