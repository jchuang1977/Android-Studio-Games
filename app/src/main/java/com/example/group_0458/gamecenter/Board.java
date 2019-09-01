package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.support.annotation.NonNull;
import java.util.Observable;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * The sliding tiles board.
 */
public class Board extends Observable implements Serializable, Iterable<Tile> {

    /**
     * image used for board
     * in order to be saved, BitmapDataObject must be package-private
     */
    BitmapDataObject boardImage;

    /**
     * Return image used for board
     *
     * @return image used for board
     */
    BitmapDataObject getBoardImage() {
        return boardImage;
    }

    /**
     * Set board image to a new board image
     *
     * @param newBoardImage new value for boardImage
     */
    void setBoardImage(BitmapDataObject newBoardImage) {
        this.boardImage = newBoardImage;
    }

    /**
     * Return whether tile at position (row, col) is empty.
     *
     * @param row row index
     * @param col column index
     * @return whether tile at position (row, col) is empty
     */
    boolean isEmptyTile(int row, int col) {
        return getTile(row, col).isEmpty();
    }

    /**
     * The tiles on the board in row-major order.
     */
    private Tile[][] tiles = new Tile[4][4];

    /**
     * A new board of tiles in row-major order.
     *
     * Precondition: len(tiles) == NUM_ROWS * NUM_COLS
     *
     * @param tiles the tiles for the board
     * @param numRows number of rows
     * @param numCols number of columns
     */
    Board(List<Tile> tiles, int numRows, int numCols) {
        Iterator<Tile> it = tiles.iterator();
        this.tiles = new Tile[numRows][numCols];
        for (int row = 0; row != numRows; row++) {
            for (int col = 0; col != numCols; col++) {
                this.tiles[row][col] = it.next();
            }
        }
    }

    Board(){

    }

    /**
     * Return the number of tiles on the board.
     *
     * @return the number of tiles on the board
     */
    int numTiles() {
        int tileCount = 0;
        for (int i = 0; i < this.tiles.length; i++) {
            for (int j = 0; j < this.tiles[i].length; j++) {
                tileCount++;
            }
        }
        return tileCount;
    }

    /**
     * Return the tile at (row, col).
     *
     * @param row the tile row
     * @param col the tile column
     * @return the tile at (row, col)
     */
    Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    /**
     * Swap the tiles at (row1, col1) and (row2, col2).
     *
     * @param row1 the first tile row
     * @param col1 the first tile col
     * @param row2 the second tile row
     * @param col2 the second tile col
     */
    void swapTiles(int row1, int col1, int row2, int col2) {
        Tile tile1 = this.tiles[row1][col1];
        Tile tile2 = this.tiles[row2][col2];
        this.tiles[row1][col1] = tile2;
        this.tiles[row2][col2] = tile1;
        setChanged();
        notifyObservers();
    }

    @Override
    public String toString() {
        return "Board{" + "tiles=" + Arrays.toString(tiles) + '}';
    }

    @NonNull
    @Override
    public Iterator<Tile> iterator() {
        return new BoardIterator(tiles);
    }

    /**
     * An iterator for Board.
     */
    public class BoardIterator implements Iterator<Tile>, Serializable {

        /*
         * current outer index of 2-dimensional array
         */
        int outerIndex = 0;

        /*
         * current inner index of 2-dimensional array
         */
        int innerIndex = -1;

        /*
         * 2-dimensional array containing board tiles
         */
        Tile[][] contents;

        /*
         * Constructor of BoardIterator
         *
         * @param givenBoard - board tiles
         */
        BoardIterator(Tile[][] givenBoard) {
            this.contents = givenBoard;
        }

        @Override
        public boolean hasNext() {
            if (outerIndex + 1 < this.contents.length ||
                    innerIndex + 1 < this.contents[outerIndex].length) {
                return true;
            }
            return false;
        }

        @Override
        public Tile next() {
            if (this.hasNext()) {
                if (this.innerIndex + 1 < this.contents[outerIndex].length) {
                    return this.contents[outerIndex][++innerIndex];
                }
                innerIndex = 0;
                return this.contents[++outerIndex][innerIndex];
            }
            return null;
        }
    }
}