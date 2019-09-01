package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import java.io.Serializable;

/**
 * interface for undo functionality.
 */
public interface Undoable extends Serializable {

    /**
     * Return whether undo operation was
     * successfully performed given number of times.
     *
     * @param times number of times undo operation needs to be performed
     * @return whether undo operation was successful
     */
    default boolean undo(int times){
        return true;
    }

    /**
     * Doing single step of undo
     *
     * @return whether undo operation was successful
     */
    default boolean undo() { return true; }

    /**
     * Return whether a game can be returned
     * to a previous state.
     *
     * @return whether a game has an action
     * that can be undone.
     */
    default boolean hasPrevious(){
        return false;
    }

    /**
     * Return whether a game can be returned
     * n given number of steps back.
     *
     * @param n number of operations
     * @return whether a game has given number
     * of actions that can be undone.
     */
    default boolean hasPrevious(int n){
        return false;
    }
}
