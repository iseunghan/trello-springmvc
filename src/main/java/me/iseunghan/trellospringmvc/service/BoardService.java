package me.iseunghan.trellospringmvc.service;

import javassist.NotFoundException;
import me.iseunghan.trellospringmvc.domain.Board;
import me.iseunghan.trellospringmvc.domain.BoardDto;
import me.iseunghan.trellospringmvc.domain.User;
import me.iseunghan.trellospringmvc.repository.BoardRepository;
import me.iseunghan.trellospringmvc.repository.UserRepository;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BoardService {

    @Autowired
    private BoardRepository boardRepository;
    @Autowired
    private UserRepository userRepository;

    private static int positionSequence = 0;
    /**
     * 보드를 추가하는 메소드
     * 현재 시간을 보드의 create,update로 설정해줍니다.
     * <p>
     * return 보드의 아이디 값을 넘겨줍니다.
     */
    public Long addBoard(BoardDto boardDto, Long userId) {
        User user = userRepository.findById(userId).get();
        if (Strings.isBlank(boardDto.getTitle())) {
            throw new IllegalStateException("공백을 입력할 수 없습니다.");
        }
        if (boardDto.getBoardColor() == null) {
            throw new IllegalStateException("보드 배경을 선택하세요.");
        }
        Board board = new Board();
        board.setUser(user);
        board.setTitle(boardDto.getTitle());
        board.setBoardColor(boardDto.getBoardColor());
        board.setCreatedAt(LocalDateTime.now());
        board.setUpdatedAt(board.getCreatedAt());
        board.setPosition(positionSequence++);
        boardRepository.save(board);
        return board.getId();
    }

    /**
     * 전체 보드를 조회하는 메소드
     */
    public List<Board> findAll(Long userId) {
        return boardRepository.findByUserId(userId);
    }

    /**
     * 하나의 보드를 조회하는 메소드
     */
    public Board findOne(Long id) throws NotFoundException {
        return boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id: " + id + "는 존재하지 않는 보드입니다."));
    }

    /**
     * 보드를 업데이트 해주는 메소드
     * 타이틀을 수정할 때, 보드색상을 변경할 때, 위치를 변경할 때
     * 처리 후, update 시간 적용 후 저장.
     */
    public Board updateBoard(Long id, BoardDto boardDto) throws NotFoundException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id: " + id + "는 존재하지 않는 보드입니다."));

        if (Strings.isNotBlank(boardDto.getTitle())) {
            board.setTitle(boardDto.getTitle());
        }

        if (boardDto.getBoardColor() != null) {
            board.setBoardColor(boardDto.getBoardColor());
        }

        if (boardDto.getPosition() > 0) {
            board.setPosition(boardDto.getPosition());
        }

        board.setUpdatedAt(LocalDateTime.now());
        return boardRepository.save(board);
    }

    /**
     * 하나의 보드를 삭제하는 메소드
     */
    public boolean deleteBoard(Long id) throws NotFoundException {
        Board board = boardRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("id: " + id + "는 존재하지 않는 보드입니다."));
        boardRepository.delete(board);
        return true;
    }
}
