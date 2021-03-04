package me.iseunghan.trellospringmvc.domain;

import java.time.LocalDateTime;

public class BoardDto {

    private String title;
    private int position;
    private BoardColor boardColor;

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

    public void setBoardColor(BoardColor boardColor) {
        this.boardColor = boardColor;
    }
}
