package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import java.io.Serializable;

/**
 * SlidingTilesScore for SlidingTiles Puzzle
 */
class SlidingTilesScore extends Score<Integer> implements Serializable {

    /**
     * Constructor for SlidingTilesScore
     *
     * @param data     representation of a score
     * @param gameName represents name of a game
     * @param givenUser given user
     */
    SlidingTilesScore(Integer data, String gameName, User givenUser) {
        super(data, gameName, givenUser);
    }

    @Override
    public int compareTo(Score other) {
        int coefficient = 1;
        if (hasComplexityChoice) {
            coefficient = gameComplexity;
        }
        if (data.intValue() * coefficient < ((SlidingTilesScore) other).getData().intValue() *
                coefficient) {
            return -1;
        } else if (data.intValue() * coefficient > ((SlidingTilesScore) other).getData().intValue() *
                coefficient) {
            return 1;
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return data.toString();
    }

}
