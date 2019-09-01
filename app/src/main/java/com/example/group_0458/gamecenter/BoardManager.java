package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Observable;
import java.util.Stack;
import java.util.AbstractMap.SimpleImmutableEntry;


/**
 * Board manager
 */
class BoardManager extends Observable implements Serializable{
    /**
     * The number of undo steps already used
     */
    private int numStepsUndone = 0;

    /**
     * The board being managed.
     */
    private Board board;

    /**
     * indicates whether board is image-based
     */
    private boolean isImageBased;

    /**
     * Return whether board is image-based.
     *
     * @return isImageBase represents whether board is image-based.
     */
    boolean isImageBase() {
        return this.isImageBased;
    }

    /**
     * number of rows
     */
    private int numRows;

    /**
     * number of columns
     */
    private int numCols;

    /**
     * Return number of rows
     *
     * @return number of rows
     */
    int getNumRows(){
        return this.numRows;
    }

    /**
     * Return number of columns
     *
     * @return number of columns
     */
    int getNumCols(){
        return this.numCols;
    }

    /**
     * The previous moves are represented by ImmutableEntry. Key and value of each
     * ImmutableEntry represents the positions of two tiles swapped.
     */
    private Stack<SimpleImmutableEntry<Integer, Integer>> previousMoves = new Stack<>();

    /**
     * Return current name of the game
     *
     * @return current name of the game
     */
    static String gameName() {
        return "Sliding Tiles";
    }

    /**
     * Set whether board is image-based.
     *
     * @param imageBased represents new value of whether board is image-based.
     */
    void setImageBased(boolean imageBased) {
        this.isImageBased = imageBased;
    }

    /**
     * Return the current number of steps undone.
     *
     * @return numStepsUndone
     */
    int getNumStepsUndone() { return numStepsUndone; }

    /**
     * Constructor for BoardManager
     *
     * @param board the board
     */
    BoardManager(Board board) {
        this.board = board;
    }

    /**
     * Return current board.
     *
     * @return current board
     */
    Board getBoard() {
        return board;
    }

    /**
     * Constructor for BoardManager
     *
     * @param givenNumRows given number of rows
     * @param givenNumCols given number of columns
     */
    BoardManager(int givenNumRows, int givenNumCols) {
        this.numRows = givenNumRows;
        this.numCols = givenNumCols;
        List<Tile> tiles = new ArrayList<>(
        );
        int numTiles = givenNumRows * givenNumCols;
        for (int tileNum = 0; tileNum < numTiles; tileNum++) {
            Tile newTile = new Tile(tileNum);
            if (tileNum == numTiles - 1) {
                newTile.setStatus(true);
            } else {
                newTile.setStatus(false);
            }
            tiles.add(newTile);
        }
        Collections.shuffle(tiles);
        this.board = new Board(tiles, numRows, numCols);
    }

    /**
     * Reset number of steps that was undone
     */
    void resetNumStepsUndone(){
        this.numStepsUndone = 1;
    }

    BoardManager(){

    }

    /**
     * Return whether the tiles are in row-major order.
     *
     * @return whether the tiles are in row-major order
     */
    boolean puzzleSolved() {
        boolean solved = true;
        Iterator<Tile> tileIter = board.iterator();
        Tile prev = tileIter.next();
        Tile next;
        while (tileIter.hasNext()) {
            next = tileIter.next();
            if (prev.getId() > next.getId()) {
                solved = false;
            }
            prev = next;
        }
        return solved;
    }

    /**
     * Return whether any of the four surrounding tiles is the blank tile.
     *
     * @param position the tile to check
     *
     * @return whether the tile at position is surrounded by a blank tile
     */
    boolean isValidTap(int position) {
        int row = position / numCols;
        int col = position % numCols;
        int blankId = board.numTiles();
        // Are any of the 4 the blank tile?
        Tile above = row == 0 ? null : board.getTile(row - 1, col);
        Tile below = row == numRows - 1 ? null : board.getTile(row + 1, col);
        Tile left = col == 0 ? null : board.getTile(row, col - 1);
        Tile right = col == numRows - 1 ? null : board.getTile(row, col + 1);
        return (below != null && below.getId() == blankId)
                || (above != null && above.getId() == blankId)
                || (left != null && left.getId() == blankId)
                || (right != null && right.getId() == blankId);
    }

    /**
     * Process a touch at position in the board, swapping tiles as appropriate.
     *
     * @param position the position
     */
    void touchMove(int position) {
        int row = position / numRows;
        int col = position % numCols;
        int blankId = board.numTiles();

        int position2 = position;
        if (this.isValidTap(position)) find:{
            for (int i = 0; i < numRows; i++) {
                for (int j = 0; j < numCols; j++) {
                    if (this.board.getTile(i, j).getId() == blankId) {
                        position2 = numCols * i + j;
                        this.board.swapTiles(i, j, row, col);
                        break find;
                    }
                }
            }
        }
        previousMoves.push(new SimpleImmutableEntry<Integer, Integer>(position, position2));
        if (this.numStepsUndone > 0) {
            this.numStepsUndone = this.numStepsUndone - 1;
        }
    }

    /**
     * Return whether undo operation was successful
     *
     * @return whether undo operation was successful
     */
    public boolean undo() {
        if (!hasPrevious() || this.numStepsUndone >= 3) {
            return false;
        } else {
            SimpleImmutableEntry<Integer, Integer> lastMove = previousMoves.pop();
            int pos1 = lastMove.getKey();
            int pos2 = lastMove.getValue();
            int row1 = pos1 / numCols;
            int col1 = pos1 % numCols;
            int row2 = pos2 / numCols;
            int col2 = pos2 % numCols;
            board.swapTiles(row1, col1, row2, col2);
            this.numStepsUndone = this.numStepsUndone + 1;
            return true;
        }
    }

    /**
     * Perform operation undo
     *
     * @param times number of times operation has to be performed
     * @return whether operation was successful
     */
    boolean undo(int times) {
        boolean re = true;
        while (times > 0 && re) {
            re = undo();
            times--;
        }
        return re;
    }

    /**
     * Return whether there is previous game state
     *
     * @return whether there is previous game state
     */
    public boolean hasPrevious() {
        return !(previousMoves.isEmpty());
    }

    /**
     * Return the number of moves made in this game
     *
     * @return the number of moves made in this game
     */
    int getNumOfMoves() {
        return previousMoves.size();
    }
}