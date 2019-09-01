package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.support.annotation.NonNull;

import java.io.Serializable;


/**
 * A Tile in a sliding tiles puzzle.
 */
public class Tile implements Comparable<Tile>, Serializable {

    /**
     * indicates whether tile is empty
     */
    private boolean empty = false;

    /**
     * Sets whether tile is empty
     *
     * @param updatedStatus indicates new tile status
     */
    void setStatus(boolean updatedStatus) {
        this.empty = updatedStatus;
    }

    /**
     * Return whether tile is empty
     */
    boolean isEmpty() {
        return this.empty;
    }

    /**
     * The background id to find the tile image.
     */
    private int background = 0;

    /**
     * The unique id.
     */
    private int id;

    /**
     * Return the background id.
     *
     * @return the background id
     */

    public int getBackground() {
        return background;
    }

    /**
     * Return the number of a tile
     *
     * @return tile number
     */
    int getTileNum() {
        return this.id;
    }

    /**
     * Return the tile id.
     *
     * @return the tile id
     */
    public int getId() {
        return id;
    }

    /**
     * A Tile with id and background. The background may not have a corresponding image.
     *
     * @param id         the id
     * @param background the background
     */
    public Tile(int id, int background) {
        this.id = id;
        this.background = background;
    }

    /**
     * Constructor for Tile
     *
     * @param backgroundId
     */
    Tile(int backgroundId) {
        id = backgroundId + 1;
        background = R.drawable.tile_16;

    }

    @Override
    public int compareTo(@NonNull Tile o) {
        return o.id - this.id;
    }
}