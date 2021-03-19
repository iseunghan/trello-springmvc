package me.iseunghan.trellospringmvc.service;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.domain.Pocket;
import me.iseunghan.trellospringmvc.domain.PocketDto;
import me.iseunghan.trellospringmvc.repository.BoardRepository;
import me.iseunghan.trellospringmvc.repository.CardRepository;
import me.iseunghan.trellospringmvc.repository.PocketRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PocketService {

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PocketRepository pocketRepository;

    @Autowired
    private CardRepository cardRepository;

    /**
     * 포켓을 추가하는 메소드
     * @param pocketDto
     * @return 포켓의 id값
     */
    public Long addPocket(Long boardId, PocketDto pocketDto) {
        if (Strings.isBlank(pocketDto.getTitle())) {
            throw new IllegalStateException("공백을 입력할 수 없습니다.");
        }
        Board board = boardRepository.findById(boardId).get();
        Pocket pocket = new Pocket();
        pocket.setTitle(pocketDto.getTitle());
        pocket.setPosition(pocketDto.getPosition());
        pocket.setCreatedAt(LocalDateTime.now());
        pocket.setUpdatedAt(pocket.getCreatedAt());
        pocket.updateBoard(board);
        pocketRepository.save(pocket);
        return pocket.getId();
    }

    /**
     * 해당 보드의 모든 포켓을 조회하는 메소드
     * @return List<Pocket>
     */
    public List<Pocket> findAll(Long boardId) {
        return pocketRepository.findPocketsByBoard_Id(boardId);
    }


    /**
     * 하나의 포켓을 조회하는 메소드
     * @param id (조회하고 싶은 포켓의 아이디)
     * @return Pocket
     * @throws NotFoundException (존재하지 않는 포켓일 경우)
     */
    public Pocket findOne(Long id) throws NotFoundException {
        return pocketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id: " + id + "은 존재하지 않는 포켓입니다."));
    }

    /**
     * 하나의 포켓을 수정하는 메소드
     *
     * Dto 필드 값이 들어있으면 수정 후 리포지토리에 저장
     * @param id (수정하고 싶은 포켓의 아이디)
     * @param pocketDto
     * @return Pocket (수정 된 포켓)
     * @throws NotFoundException
     */
    public Pocket updatePocket(Long id, PocketDto pocketDto) throws NotFoundException {
        Pocket pocket = pocketRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id: " + id + "은 존재하지 않는 포켓입니다."));

        if (pocketDto.getTitle() != null) {
            pocket.setTitle(pocketDto.getTitle());
        }

        if (pocketDto.getBoardId() != null) {
            Board board = boardRepository.findById(pocketDto.getBoardId()).get();
            pocket.updateBoard(board);
        }

        if (pocketDto.getPosition() > 0) {
            pocket.setPosition(pocketDto.getPosition());
        }

        pocket.setUpdatedAt(LocalDateTime.now());
        return pocketRepository.save(pocket);
    }

    /**
     * 하나의 포켓을 삭제하는 메소드
     *
     * 해당 포켓의 아이디와, 포켓이 속한 보드의 아이디를 함께 받습니다.
     * @param boardId
     * @param pocketId
     * @return true(성공여부)
     * @throws NotFoundException
     */
    public boolean deletePocket(Long boardId, Long pocketId) throws NotFoundException {
        Pocket pocket = pocketRepository.findById(pocketId)
                .orElseThrow(() -> new NotFoundException("id : " + pocketId + "는 존재하지 않는 포켓입니다."));

        pocketRepository.delete(pocket);

        return true;
    }
}
