package me.iseunghan.trellospringmvc.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class BoardDto {

    private Long boardId;
    private String title;
    private int position;
    private BoardColor boardColor;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PocketDto> pockets = new ArrayList<>();

    public void setBoardColor(BoardColor boardColor) {
        this.boardColor = boardColor;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getBoardId() {
        return boardId;
    }

    public void setBoardId(Long boardId) {
        this.boardId = boardId;
    }

    public List<PocketDto> getPockets() {
        return pockets;
    }

    public void setPockets(List<PocketDto> pockets) {
        this.pockets = pockets;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public BoardColor getBoardColor() {
        return boardColor;
    }

    public void setBoardColor(String boardColor) {
        switch (boardColor) {
            case "BLUE":
                this.boardColor = BoardColor.BLUE;
                break;
            case "RED":
                this.boardColor =  BoardColor.RED;
                break;
            case "GREEN":
                this.boardColor =  BoardColor.GREEN;
                break;
            default:
                this.boardColor =  BoardColor.YELLOW;
                break;
        }
    }
}
