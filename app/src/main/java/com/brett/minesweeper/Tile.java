package com.brett.minesweeper;

/**
 * Created by Brett on 15/10/21.
 */
public class Tile {

    //===================FIELDS====================
    public boolean isMine;
    public int adjacentMinesCount;
    public Status mStatus;

    //===================CONSTRUCTORS====================
    public Tile(){
        this.initialize();
    }

    //===================METHODS====================
    public void initialize(){
        isMine = false;
        adjacentMinesCount = 0;
        mStatus = Status.COVERED;
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status status) {
        this.mStatus = status;
    }

    public int getAdjacentMinesCount() {
        return adjacentMinesCount;
    }

    public void setAdjacentMinesCount(int count) {
        this.adjacentMinesCount = count;
    }

    public void increaseAdjacentMinesCount(){
        if(!this.isMine){
            adjacentMinesCount++;
        }
    }

    public boolean isMine(){
        return isMine;
    }

    public void setMine(boolean isMine){
        this.isMine = isMine;
    }


    //==================Inner Enum Status============
    public enum Status {
        UNCOVERED, FLAG, COVERED, FAILED
    }
}
