package com.example.group_0458.gamecenter;

import android.support.v7.app.AppCompatActivity;
import java.util.List;

/**
 * Main menu of a game
 */
abstract class MainMenuActivity extends AppCompatActivity implements DialogAction {

    /**
     * leader board per game
     */
    protected List<Score> leaderBoardPerGame;

    /**
     * leader board per user
     */
    protected List<Score> leaderBoardPerUser;

    /**
     * file statuses
     */
    protected boolean[] fileStatuses = {false, false, false};

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onStart(){
        super.onStart();
        updateLeaderBoard();
    }

    /**
     * Update leader board
     */
    abstract void updateLeaderBoard();

    @Override
    public void positiveAction(){
        finish();
    }

    @Override
    public void negativeAction(){

    }

    /**
     * Save file statuses
     */
    abstract void saveFileStatuses();

    /**
     * Load file statuses
     */
    abstract void loadFileStatuses();
}