package me.iseunghan.trellospringmvc.domain;

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
