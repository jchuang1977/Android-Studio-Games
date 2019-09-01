package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import java.util.HashMap;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * A user
 */
public class User implements Serializable {

    /**
     * map of game names and scores
     */
    private Map<String, ArrayList<Score>> gameScores;

    /**
     * password of the user
     */
    private String password = null;

    /**
     * name of the user
     */
    private String name;

    /**
     * Constructor for User
     * @param givenName name of user
     */
    User(String givenName) {
        this.name = givenName;
        this.gameScores = new HashMap<>();
    }

    /**
     * Set the password of the user
     *
     * @param inPassword the password of the user to set
     */
    public void setPassword(String inPassword) {
        this.password = inPassword;
    }

    /**
     * Return the password of the user, null if password is not set yet
     *
     * @return the password of the user
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * Set the name of the user.
     *
     * @param inName is the name of the user to reset to
     */
    public void setName(String inName) {
        this.name = inName;
    }

    /**
     * return the name of the user
     *
     * @return the name of the user
     */
    public String getName() {
        return this.name;
    }

    /**
     * Add a score to the user
     *
     * @param givenScore the score to add to the user
     */
    void addScore(Score givenScore) {
        if(gameScores.containsKey(givenScore.getGameName())){
            ArrayList<Score> existingScores = gameScores.get(givenScore.getGameName());
            existingScores.add(givenScore);
            gameScores.replace(givenScore.getGameName(), existingScores);
            updateLeaderBoard(givenScore.getGameName());
        }
        else {
            ArrayList<Score> newScores = new ArrayList<Score>();
            newScores.add(givenScore);
            gameScores.put(givenScore.getGameName(), newScores);
        }
    }

    /**
     * Remove unnecessary scores
     *
     * @param gameName name of the game
     */
    private void updateLeaderBoard(String gameName){
        ArrayList<Score> scores = gameScores.get(gameName);
        while(scores.size() > 5){
            scores.remove(scores.size() - 1);
        }
        gameScores.replace(gameName, scores);
    }

    /**
     * Return scores for all games of user
     *
     * @return scores for all games
     */
    List<Score> getScoresForAllGames(){
        ArrayList<Score> result = new ArrayList<Score>();
        for(Map.Entry<String, ArrayList<Score>> pair : gameScores.entrySet()){
            result.addAll(pair.getValue());
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof User) {
            User other = (User) obj;
            return other.getName().equals(this.getName());
        }
        return false;
    }

    /**
     * Return scores user has for some specific game
     *
     * @param gameName name of the game that scores are requested for
     * @return scores for specific game
     */
    List<Score> scoresPerGame(String gameName){
        if(gameScores.containsKey(gameName)) {
            return gameScores.get(gameName);
        }
        return null;
    }
}