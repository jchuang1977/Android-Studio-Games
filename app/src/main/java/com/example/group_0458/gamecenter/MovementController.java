package com.example.group_0458.gamecenter;

import android.content.Context;
import android.widget.Toast;

/**
 * Movement controller for the game
 */
class MovementController {

    /**
     * board manager used by Movement controller
     */
    private BoardManager boardManager = null;

    /**
     * game activity used by movement controller
     */
    private GameActivity gameActivity;

    /**
     * default constructor
     */
    MovementController() {
    }

    /**
     * Set value of gameActivity to given activity
     *
     * @param activity given activity
     */
    void setActivity(GameActivity activity) {
        gameActivity = activity;
    }

    /**
     * Set board manager to given board manager
     *
     * @param boardManager new value of boardManager
     */
    void setBoardManager(BoardManager boardManager) {
        this.boardManager = boardManager;
    }

    /**
     * Process tap
     *
     * @param context  given context
     * @param position position
     */
    void processTapMovement(Context context, int position) {
        if (boardManager.isValidTap(position)) {
            boardManager.touchMove(position);
            if (boardManager.puzzleSolved()) {
                Toast.makeText(context, "YOU WIN!", Toast.LENGTH_SHORT).show();
                // make board manager calculate new score
                gameActivity.addNewScore();
                // make sure game activity saves contents before exiting
                gameActivity.saveGame();
                // tell game activity to finish
                gameActivity.finish();
            }
        } else {
            Toast.makeText(context, "Invalid Tap", Toast.LENGTH_SHORT).show();
        }
    }
}
