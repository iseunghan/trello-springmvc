package me.iseunghan.trellospringmvc.service;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Card;
import me.iseunghan.trellospringmvc.domain.CardDto;
import me.iseunghan.trellospringmvc.domain.Pocket;
import me.iseunghan.trellospringmvc.repository.CardRepository;
import me.iseunghan.trellospringmvc.repository.PocketRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private PocketRepository pocketRepository;

    private static int positionSequence = 0;
    /**
     * 하나의 카드를 추가하는 메소드
     * @param pocketId
     * @param cardDto
     * @return cardId
     */
    public Long addCard(Long pocketId, CardDto cardDto) {
        if (Strings.isBlank(cardDto.getTitle())) {
            throw new IllegalStateException("공백을 입력할 수 없습니다.");
        }
        Pocket pocket = pocketRepository.findById(pocketId).get();
        Card card = new Card();
        card.setTitle(cardDto.getTitle());
        card.updatePocket(pocket);
        card.setCreatedAt(LocalDateTime.now());
        card.setUpdatedAt(card.getCreatedAt());
        card.setPosition(positionSequence++);
        cardRepository.save(card);
        return card.getId();
    }

    /**
     * 해당 포켓의 모든 카드를 조회하는 메소드
     * @param pocketId
     * @return List<Card>
     */
    public List<Card> findAll(Long pocketId) {
        return cardRepository.findCardsByPocket_IdOrderByPosition(pocketId);
    }

    /**
     * 하나의 카드를 조회하는 메소드
     * @param cardId
     * @return Card
     * @throws NotFoundException
     */
    public Card findOne(Long cardId) throws NotFoundException {
        return cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("해당 id: " + cardId + "는 존재하지 않는 카드입니다."));
    }

    /**
     * 카드 수정 메소드
     * (타이틀, 설명, 위치, 포켓) 을 변경할 수 있음.
     * @param cardId
     * @param cardDto
     * @return Card
     * @throws NotFoundException
     */
    public Card updateCard(Long cardId, CardDto cardDto) throws NotFoundException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("해당 id: " + cardId + "는 존재하지 않는 카드입니다."));

        if (Strings.isNotBlank(cardDto.getTitle())) {
            card.setTitle(cardDto.getTitle());
        }
        if (Strings.isNotBlank(cardDto.getDescription())) {
            card.setDescription(cardDto.getDescription());
        }
        if (cardDto.getPosition() > 0) {
            List<Card> cards = cardRepository.findCardsByPocket_IdOrderByPosition(cardDto.getPocketId());
            cards.forEach(card1 -> {
                if (card1.getPosition() >= cardDto.getPosition()) {
                    card1.setPosition(card1.getPosition());
                }
            });
            positionSequence++;
            card.setPosition(cardDto.getPosition());
        }
        if (cardDto.getPocketId() != null) {
            Pocket updatePocket = pocketRepository.findById(cardDto.getPocketId()).get();
            card.updatePocket(updatePocket);
        }
        card.setUpdatedAt(LocalDateTime.now());
        return cardRepository.save(card);
    }

    /**
     * 하나의 카드를 삭제하는 메소드
     * @param cardId
     * @return  true
     * @throws NotFoundException
     */
    public boolean deleteCard(Long cardId) throws NotFoundException {
        Card card = cardRepository.findById(cardId)
                .orElseThrow(() -> new NotFoundException("해당 id: " + cardId + "는 존재하지 않는 카드입니다."));

        cardRepository.delete(card);
        return true;
    }
}
