package com.javarush.task.task35.task3513;

import java.util.*;

public class Model {
    private static final int FIELD_WIDTH = 4;
    private Tile[][] gameTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
    protected int score, maxTile;
    private Stack<Tile[][]> previousStates = new Stack();
    private Stack<Integer> previousScores = new Stack();
    private boolean isSaveNeeded = true;

    public Model() {
        resetGameTiles();
        this.score = 0;
        this.maxTile = 0;
    }

    public Tile[][] getGameTiles() {
        return gameTiles;
    }

    private void saveState(Tile[][] tiles){
        Tile[][] tempTiles = new Tile[FIELD_WIDTH][FIELD_WIDTH];
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                tempTiles[i][j] = new Tile(tiles[i][j].value);
            }
        }
        previousStates.push(tempTiles);
        previousScores.push(score);
        isSaveNeeded = false;
    }

    public void rollback(){
        if (!previousStates.isEmpty() && !previousScores.isEmpty()){
            gameTiles = previousStates.pop();
            score = previousScores.pop();
        }
    }

    public boolean canMove(){
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if (gameTiles[i][j].isEmpty())
                    return true;
            }
        }
        for (int x = 0; x < FIELD_WIDTH; x++) {
            for (int y = 0; y < FIELD_WIDTH; y++) {
                Tile t = gameTiles[x][y];
                if ((x < FIELD_WIDTH - 1 && t.value == gameTiles[x + 1][y].value)
                        || ((y < FIELD_WIDTH - 1) && t.value == gameTiles[x][y + 1].value)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void addTile() {
        List<Tile> list = null;
        if (!getEmptyTiles().isEmpty())
            list = getEmptyTiles();
        else return;
        int numberOfRandomTile = (int) (list.size() * Math.random());
        list.get(numberOfRandomTile).setValue(Math.random() < 0.9 ? 2 : 4);
    }

    private List<Tile> getEmptyTiles() {
        List<Tile> list = new ArrayList<>();
        for (Tile[] tileArray : gameTiles
        ) {
            for (Tile tile : tileArray) {
                if (tile.isEmpty())
                    list.add(tile);
            }
        }
        return list;
    }

    protected void resetGameTiles() {
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                gameTiles[i][j] = new Tile();
            }
        }
        addTile();
        addTile();
    }

    private boolean compressTiles(Tile[] tiles) {
        List<Integer> list = new LinkedList<>();
        for (Tile tile: tiles
             ) {
            list.add(tile.value);
        }
        for (int j = 0; j < tiles.length; j++) {
            for (int i = 0; i < tiles.length; i++) {
                try {
                    if (tiles[i].value == 0){
                        tiles[i].value = tiles[i + 1].value;
                        tiles[i+1].value = 0;
                    }
                } catch (ArrayIndexOutOfBoundsException e) {
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            if (list.get(i)!=tiles[i].value)
                return true;
        }
        return false;
    }

    private boolean mergeTiles(Tile[] tiles) {
        List<Integer> list = new LinkedList<>();
        for (Tile tile: tiles
        ) {
            list.add(tile.value);
        }
        Tile t = null;
        Tile t1 = null;
        for (int i = 0; i < tiles.length; i++) {
            try {
                t = tiles[i];
                t1 = tiles[i + 1];
                if (t.value == t1.value){
                    t.value+=t.value;
                    t1.value=0;
                    compressTiles(tiles);
                    if (maxTile < t.value)
                        maxTile = t.value;
                    score += t.value;

                }
            } catch (ArrayIndexOutOfBoundsException e) {
            }
        }
        for (int i = 0; i < 4; i++) {
            if (list.get(i)!=tiles[i].value)
                return true;
        }
        return false;
    }

    public void left(){
        if (isSaveNeeded){
            saveState(gameTiles);
        }
        boolean moveFlag = false;
        for (int i = 0; i < FIELD_WIDTH; i++) {
            if (compressTiles(gameTiles[i]) | mergeTiles(gameTiles[i])) {
                moveFlag = true;
            }
        }
        if (moveFlag) {
            addTile();
        }
        isSaveNeeded=true;
    }

    private void move90Angle(){
//transpose
        for(int r = 0; r < FIELD_WIDTH; r++) {
            for(int c = r; c < FIELD_WIDTH; c++) {
                swap(r, c, c, r);
            }
        }
//reverse elements on row order
        for(int r = 0; r < FIELD_WIDTH; r++) {
            for(int c =0; c < FIELD_WIDTH/2; c++) {
                swap(r, c, r, FIELD_WIDTH-c-1);
            }
        }
    }

    private void swap(int i, int j, int i2, int j2) {
        Tile tmp = gameTiles[i][j];
        gameTiles[i][j] = gameTiles[i2][j2];
        gameTiles [i2][j2] = tmp;
    }

    public void right(){
        saveState(gameTiles);
        move90Angle();
        move90Angle();
        left();
        move90Angle();
        move90Angle();
    }

    public void up(){
        saveState(gameTiles);
        move90Angle();
        move90Angle();
        move90Angle();
        left();
        move90Angle();
    }

    public void down(){
        saveState(gameTiles);
        move90Angle();
        left();
        move90Angle();
        move90Angle();
        move90Angle();
    }

    public void randomMove(){
        int number = ((int)(Math.random() * 100))%4;
        if (number == 0)
            left();
        else if(number == 1)
            right();
        else if(number == 2)
            up();
        else if(number == 3)
            down();
    }

    public boolean hasBoardChanged(){
        for (int i = 0; i < FIELD_WIDTH; i++) {
            for (int j = 0; j < FIELD_WIDTH; j++) {
                if(gameTiles[i][j].value != previousStates.peek()[i][j].value)
                    return true;
            }
        }
        return false;
    }

    public MoveEfficiency getMoveEfficiency(Move move){
        MoveEfficiency moveEfficiency;
        move.move();
        if (hasBoardChanged())
            moveEfficiency = new MoveEfficiency(getEmptyTiles().size(), score , move);
        else
            moveEfficiency = new MoveEfficiency(-1, 0, move);
        rollback();
        return moveEfficiency;
    }

    public void autoMove(){
        PriorityQueue<MoveEfficiency> moveEfficiencies = new PriorityQueue<>(4, Collections.reverseOrder());
        moveEfficiencies.offer(getMoveEfficiency(this::left));
        moveEfficiencies.offer(getMoveEfficiency(this::up));
        moveEfficiencies.offer(getMoveEfficiency(this::right));
        moveEfficiencies.offer(getMoveEfficiency(this::down));
        assert moveEfficiencies.peek() != null;
        Move move = moveEfficiencies.peek().getMove();
        move.move();
    }
}
