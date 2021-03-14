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
class CardServiceTest {

    @Autowired
    private BoardService boardService;
    @Autowired
    private PocketService pocketService;
    @Autowired
    private CardService cardService;

    @Test
    @DisplayName("하나의 카드 추가")
    void addCard() throws NotFoundException {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("addCard Board");
        boardDto.setBoardColor("RED");
        Long boardId = boardService.addBoard(boardDto);

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("addCard Board");
        Long pocketId = pocketService.addPocket(boardId, pocketDto);

        CardDto cardDto = new CardDto();
        cardDto.setTitle("lets do it!!");

        // when
        Long cardId = cardService.addCard(pocketId, cardDto);

        // then
        Card card = cardService.findOne(cardId);
        assertEquals(card.getTitle(), cardDto.getTitle());
        assertEquals(card.getPocket().getId(), pocketId);
        assertEquals(card.getPocket().getBoard().getId(), boardId);
    }

    @Test
    @DisplayName("해당 포켓의 모든 카드 조회")
    void findAll() {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("findAll Board");
        boardDto.setBoardColor("RED");
        Long boardId = boardService.addBoard(boardDto);

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("findAll Pocket");
        Long pocketId = pocketService.addPocket(boardId, pocketDto);

        CardDto cardDto = new CardDto();
        cardDto.setTitle("card title1");
        CardDto cardDto2 = new CardDto();
        cardDto2.setTitle("card title2");
        CardDto cardDto3 = new CardDto();
        cardDto3.setTitle("card title3");

        // when
        Long id1 = cardService.addCard(pocketId, cardDto);
        Long id2 = cardService.addCard(pocketId, cardDto2);
        Long id3 = cardService.addCard(pocketId, cardDto3);

        // then
        List<Card> pockets = cardService.findAll(pocketId);
        assertEquals(pockets.size(), 3);
        assertEquals(pockets.get(0).getTitle(), cardDto.getTitle());
        assertEquals(pockets.get(1).getTitle(), cardDto2.getTitle());
        assertEquals(pockets.get(2).getTitle(), cardDto3.getTitle());
    }

    @Test
    @DisplayName("하나의 카드 조회")
    void findOne() throws NotFoundException {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("findOne Board");
        boardDto.setBoardColor("RED");
        Long boardId = boardService.addBoard(boardDto);

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("findOne Pocket");
        Long pocketId = pocketService.addPocket(boardId, pocketDto);

        CardDto cardDto = new CardDto();
        cardDto.setTitle("this is my card");
        Long cardId = cardService.addCard(pocketId, cardDto);

        // when
        Card card = cardService.findOne(cardId);

        // then
        assertEquals(card.getTitle(), cardDto.getTitle());
        assertEquals(card.getPocket().getId(), pocketId);
    }

    @Test
    @DisplayName("카드 수정")
    void updateCard() throws NotFoundException {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("update Board");
        boardDto.setBoardColor("RED");
        Long boardId = boardService.addBoard(boardDto);

        PocketDto oldPocket = new PocketDto();
        oldPocket.setTitle("update Pocket");
        Long oldPocketId = pocketService.addPocket(boardId, oldPocket);

        PocketDto newPocket = new PocketDto();
        newPocket.setTitle("new update Pocket");
        Long newPocketId = pocketService.addPocket(boardId, newPocket);

        CardDto oldCardDto = new CardDto();
        oldCardDto.setTitle("old card title");
        oldCardDto.setDescription("old description");
        Long cardId = cardService.addCard(oldPocketId, oldCardDto);

        // when
        CardDto newCardDto = new CardDto();
        newCardDto.setTitle("new card title");
        newCardDto.setDescription("new description");
        newCardDto.setPocketId(newPocketId);
        Card newCard = cardService.updateCard(cardId, newCardDto);

        // then
        assertEquals(newCard.getTitle(), newCardDto.getTitle());
        assertEquals(newCard.getDescription(), newCardDto.getDescription());
        assertEquals(newCard.getPocket().getId(), newPocketId);
    }

    @Test
    @DisplayName("하나의 카드 삭제")
    void deleteCard() throws NotFoundException {
        // given
        BoardDto boardDto = new BoardDto();
        boardDto.setTitle("findOne Board");
        boardDto.setBoardColor("RED");
        Long boardId = boardService.addBoard(boardDto);

        PocketDto pocketDto = new PocketDto();
        pocketDto.setTitle("findOne Pocket");
        Long pocketId = pocketService.addPocket(boardId, pocketDto);

        CardDto cardDto = new CardDto();
        cardDto.setTitle("this is my card");
        Long cardId = cardService.addCard(pocketId, cardDto);

        // when
        boolean result = cardService.deleteCard(cardId);

        // then
        assertTrue(result);
    }
}