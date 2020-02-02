package com.jgji.spring.domain.word.model;

public class Row {

    private Variety variety = null;
    private Integer cell = null;
    
    public Row() {
        super();
    }

    public Variety getVariety() {
        return variety;
    }

    public void setVariety(Variety variety) {
        this.variety = variety;
    }

    public Integer getCell() {
        return cell;
    }

    public void setCell(Integer cell) {
        this.cell = cell;
    }
    
    
}
