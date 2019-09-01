package com.example.group_0458.gamecenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.widget.Toast;
import java.util.Timer;
import java.util.TimerTask;

/**
 * abstract class for game-playing activities
 */
abstract class GamePlayActivity extends AppCompatActivity {

    /**
     * Message handler
     */
    protected Handler handler;

    /**
     * Timer used for automatic saving
     */
    protected Timer autoSaveTimer;

    /**
     * Task used by timer for saving game
     */
    protected SavingTask savingTask;

    /**
     * gesture detector used to detect user gestures
     */
    protected GestureDetectorCompat gestureDetector;

    /**
     * indicates whether game can be saved
     */
    protected boolean canSave = false;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setupMessageHandler();
    }

    @Override
    protected void onStop(){
        super.onStop();
        autoSaveTimer.cancel();
        savingTask.cancel();
    }

    @Override
    protected void onStart(){
        super.onStart();
    }

    /**
     * Setup automatic saving of the game
     *
     * Precondition: savingInterval must be time interval in seconds
     *
     * @param savingInterval time interval in seconds used for saving
     */
    protected void setupAutomaticSaving(int savingInterval) {
        autoSaveTimer = new Timer();
        savingTask = new SavingTask();
        // Schedule saving task
        if(savingInterval <= 0){
            savingInterval = 5;
        }
        autoSaveTimer.schedule(savingTask, 1000, savingInterval * 1000);
    }

    /**
     * Set up message handler
     */
    protected void setupMessageHandler(){
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(Message message) {
                if(message.what == 0) {
                    Toast toast = Toast.makeText(GamePlayActivity.this,
                            "game was saved",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 540);
                    toast.show();
                }
            }
        };
    }

    /**
     * Saving task for timer
     */
    protected class SavingTask extends TimerTask {
        @Override
        public void run() {
            if(canSave) {
                saveGame();
                Message message = handler.obtainMessage(0, 0);
                message.sendToTarget();
            }
        }
    };

    /**
     * Save game to file
     */
    protected void saveGame(){

    }

    /**
     * Load game from file
     */
    protected void loadGame(){

    }
}