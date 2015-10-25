package com.brett.minesweeper;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;

/**
 * Created by Brett on 15/10/21.
 */
public class Board {
    //===================FIELDS====================
    private GameStatus mStatus;
    private List<Tile> mTiles;

    //===================CONSTRUCTORS====================
    public Board(){
        mStatus = GameStatus.PLAYING;
        mTiles = new ArrayList<Tile>(Utility.TILE_COUNT);
        //Setting all tiles blanks
        for(int i = 0; i < Utility.TILE_COUNT; i++){
            mTiles.add(new Tile());
        }
        SetMines();
    }

    //===================METHODS====================

    /**
     * Set mines in random positions
     */
    public void SetMines(){
        Random rand = new Random();
        //Used set so we dont get duplicates
        Set<Integer> mineSet = new HashSet<>();
        //First we randomly generate all positions
        while (mineSet.size() < Utility.MINES_COUNT){
            Integer pos = rand.nextInt(Utility.TILE_COUNT);
            mineSet.add(pos);
        }
        //Now we can set the mines accordingly
        for (Integer pos: mineSet){
            mTiles.get(pos).setMine(true);
            for (Integer adjacent: getAdjacentIndexes(pos)){
                mTiles.get(adjacent).increaseAdjacentMinesCount();
            }
        }
    }

    /**
     * Method that returns indexes of all neighbors
     */
    public List<Integer> getAdjacentIndexes(int pos){
        List<Integer> indexes = new ArrayList<>();
        int row = pos / Utility.COL_COUNT;
        int col = pos % Utility.COL_COUNT;

        // upper row
        if(row-1 >= 0 && col-1 >= 0) indexes.add((row-1)*Utility.ROW_COUNT+(col-1));
        if(row-1 >= 0) indexes.add((row-1)*Utility.ROW_COUNT+(col));
        if(row-1 >= 0 && col+1 < Utility.COL_COUNT) indexes.add((row-1)*Utility.ROW_COUNT+(col+1));

        // same row
        if(col-1 >= 0) indexes.add((row)*Utility.ROW_COUNT+(col-1));
        if(col+1 < Utility.COL_COUNT) indexes.add((row)*Utility.ROW_COUNT+(col+1));

        // next row
        if(row+1 < Utility.ROW_COUNT && col-1 >= 0) indexes.add((row+1)*Utility.ROW_COUNT+(col-1));
        if(row+1 < Utility.ROW_COUNT) indexes.add((row+1)*Utility.ROW_COUNT+(col));
        if(row+1 < Utility.ROW_COUNT && col+1 < Utility.COL_COUNT) indexes.add((row+1)*Utility.ROW_COUNT+(col+1));
        return indexes;
    }

    /*
        initialize each tile, then set mines
     */
    public void newGame(){
        if(mTiles == null) return;
        for(Tile tile: mTiles){
            tile.setMine(false);
            tile.setStatus(Tile.Status.COVERED);
            tile.setAdjacentMinesCount(0);
        }
        SetMines();
    }

    public boolean validate(){
        for (Tile tile: mTiles){
            if(tile.getStatus() == Tile.Status.COVERED){
                mStatus = GameStatus.LOST;
                return false;
            }
            if(tile.isMine() && tile.getStatus() == Tile.Status.UNCOVERED){
                mStatus = GameStatus.LOST;
                return false;
            }
        }
        mStatus = GameStatus.WIN;
        return true;
    }


    /**
     * for cheat
     */
    public void cheat(){
        for (Tile tile: mTiles){
            if(tile.isMine()){
                tile.setStatus(Tile.Status.FLAG);
            }
            if(!tile.isMine() && tile.getStatus() == Tile.Status.FLAG){
                tile.setStatus(Tile.Status.COVERED);
            }
        }
    }

    /**
     * Flips tiles recursively
     */
    public void flipTile(int pos){
        if(pos < 0 || pos > Utility.TILE_COUNT || mTiles.get(pos) == null){
            return;
        }

        Tile tile = mTiles.get(pos);
        if (tile.getStatus() != Tile.Status.COVERED){
            return;
        }

        tile.setStatus(Tile.Status.UNCOVERED);
        if(tile.isMine()){
            mStatus = GameStatus.LOST;
            return;
        }
        // if the number was 0, behaves as if the user has clicked on every cell around it
        if(tile.getAdjacentMinesCount() == 0){
            for (Integer i: getAdjacentIndexes(pos)){
                flipTile(i);
            }
        }
    }

    /*
        when lose the game, show all the mines
     */
    public void showAllMines(){
        for(Tile tile: mTiles){
            if(tile.isMine()){
                tile.setStatus(Tile.Status.UNCOVERED);
            }
        }
    }

    public List<Tile> getTiles() {
        return mTiles;
    }

    public GameStatus getStatus() {
        return mStatus;
    }

    public void updateStatus() {
        boolean allFlipped = true;
        for(Tile tile: mTiles){
            if(tile.getStatus() == Tile.Status.UNCOVERED && tile.isMine()){
                mStatus = GameStatus.LOST;
                return;
            }
            if (tile.getStatus() == Tile.Status.COVERED){
                allFlipped = false;
            }
        }
//        if(allFlipped){
//            mStatus =  GameStatus.WIN;
//        }
    }

    public void setGameStatus(GameStatus status){
        this.mStatus = status;
    }

    public void setFlag(int pos){
        Tile tile = mTiles.get(pos);
        if(tile.getStatus() == Tile.Status.COVERED){
            tile.setStatus(Tile.Status.FLAG);
        }
        else if(tile.getStatus() == Tile.Status.FLAG){
            tile.setStatus(Tile.Status.COVERED);
        }
    }

    /**
     * Enum that defines the game status
     */
    public enum GameStatus {
        PLAYING, WIN, LOST
    }
}
