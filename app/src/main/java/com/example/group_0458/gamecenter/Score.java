package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import java.io.Serializable;

/**
 * Score for the game
 */
abstract class Score<T extends Serializable> implements Comparable<Score>, Serializable {

    /**
     * data containing information about the game score
     */
    protected T data = null;

    /**
     * indicates whether game allows user to select complexity
     */
    protected boolean hasComplexityChoice = false;

    /**
     * complexity of a game
     */
    protected int gameComplexity = 3;

    /**
     * Set whether game in which score was achieved has complexity choice
     *
     * @param hasChoice indicate whether game in which score was achieved has complexity choice
     */
    void setHasComplexityChoice(boolean hasChoice){
        hasComplexityChoice = hasChoice;
    }

    /**
     * Return whether game in which score was achieved has adjustable complexity
     *
     * @return whether game in which score was achieved has adjustable complexity
     */
    boolean hasComplexity(){
        return this.hasComplexityChoice;
    }

    /**
     * Return complexity of a game in which score was achieved
     *
     * @return complexity of a game in which score was achieved
     */
    int getGameComplexity(){
        return this.gameComplexity;
    }

    /**
     * Set value of complexity of a game in which score was achieved
     *
     * @param newGameComplexity new value for game complexity
     */
    void setGameComplexity(int newGameComplexity){
        this.gameComplexity = newGameComplexity;
    }

    /**
     * Return data contained in Score
     *
     * @return data contained in score
     */
    T getData() {
        return data;
    }

    /**
     * Set data to newData
     *
     * @param newData
     */
    void setData(T newData) { this.data = newData; }

    /**
     * Name of the game related to this score
     */
    protected String gameName = "default";

    /**
     * owner of the score
     */
    protected User owner;

    /**
     * Return owner of a score.
     *
     * @return user who owns this score
     */
    User getOwner(){
        return this.owner;
    }

    /**
     * Return name of the game that corresponds to this Score
     *
     * @return name of a game
     */
    String getGameName() {
        return this.gameName;
    }

    /**
     * Score for the game
     * @return current score
     */
    T getScore() {
        return this.data;
    }

    /**
     * Constructor for Score
     *
     * @param givenData representation of a score
     * @param gameName name of the game
     * @param givenOwner owner of score
     */
    Score(T givenData, String gameName, User givenOwner) {
        this.data = givenData;
        this.gameName = gameName;
        this.owner = givenOwner;
    }

    /**
     * default constructor for Score
     */
    Score() {
        this.data = null;
        this.gameName = "default name";
        this.owner = null;
    }
}