package com.example.group_0458.gamecenter;

import android.app.Activity;
import android.util.Log;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Score board manager for score board
 */
class ScoreboardManager {

    /**
     * activity used by ScoreboardManager
     */
    private Activity activity;

    /**
     * name of a game
     */
    private String gameName;

    /**
     * Constructor for ScoreboardManager
     *
     * @param activity activity that scoreboard manager works with
     * @param gameName name of the current game
     */
    ScoreboardManager(Activity activity, String gameName){
        this.activity = activity;
        this.gameName = gameName;
    }

    /**
     * The lists of users
     */
    private List<User> users = new ArrayList<User>();

    /**
     * Load the lists of users
     */
    private void loadUsers() {
        try {
            InputStream inputStream = activity.openFileInput("Accounts.ser");
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                users = (ArrayList<User>) input.readObject();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " +
                    e.toString());
        }
    }

    /**
     * Save the lists of users
     */
    private void saveUsers() {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    activity.openFileOutput("Accounts.ser", Activity.MODE_PRIVATE));
            outputStream.writeObject(users);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Return leader board per game
     *
     * @return leader board per game
     */
    List<Score> leaderBoardPerGame(){
        loadUsers();
        ArrayList<Score> scores = new ArrayList<Score>();
        for(int i = 0; i < users.size(); i++) {
            ArrayList<Score> receivedScores = (ArrayList<Score>)users.get(i).scoresPerGame(gameName);
            if(receivedScores != null) {
                scores.addAll(receivedScores);
            }
        }
        if(scores.size() >= 1) {
            Collections.sort(scores, new Comparator<Score>() {
                @Override
                public int compare(Score firstScore, Score secondScore) {
                    return firstScore.compareTo(secondScore);
                }
            });
            Collections.reverse(scores);
        }
        return scores;
    }

    /**
     * Return leader board for current user
     *
     * @return leader board for current user
     */
    List<Score> leaderBoardPerUser(){
        loadUsers();
        User currentUser = findUser(SignInActivity.getCurrentUserName());
        ArrayList<Score> scores = (ArrayList<Score>)currentUser.getScoresForAllGames();
        if(scores != null) {
            Collections.shuffle(scores);
        }
        return scores;
    }

    /**
     * Return user with userName
     *
     * @param userName name of a user
     * @return user with name
     */
    User findUser(String userName){
        loadUsers();
        for (int i = 0; i < users.size(); i++){
            if(users.get(i).getName().equals(userName)){
                return users.get(i);
            }
        }
        return null;
    }

    /**
     * Add new score to the current user's list of scores
     *
     * @param newScore new score to be added
     */
    void addScore(Score newScore){
        loadUsers();
        User currentUser = findUser(SignInActivity.getCurrentUserName());
        currentUser.addScore(newScore);
        saveUsers();
    }
}