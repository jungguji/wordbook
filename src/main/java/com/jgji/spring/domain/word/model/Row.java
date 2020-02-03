package com.jgji.spring.domain.word.model;

public class Row {

    private String text = null;
    private Integer cell = null;
    
    public Row() {
        super();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Integer getCell() {
        return cell;
    }

    public void setCell(Integer cell) {
        this.cell = cell;
    }
    
    
}
