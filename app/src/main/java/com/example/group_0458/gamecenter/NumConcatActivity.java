package com.example.group_0458.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GestureDetectorCompat;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Activity for game Fives
 */
public class NumConcatActivity extends GamePlayActivity implements
        GestureDetector.OnGestureListener{

    /**
     * manager for number layers
     */
    private NumberLayerManager numberManager;

    /**
     * layout adapter
     */
    private ArrayAdapter<NumberLayer> numberAdapter;

    /**
     * grid view
     */
    private GridView numGrid;

    /**
     * indicator whether activity is loading
     */
    private boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concat_num);

        Intent intent = getIntent();
        if((Boolean)intent.getSerializableExtra("newGame")) {
            numberManager = new NumberLayerManager((Integer)intent.getSerializableExtra(
                    "gameComplexity"));
            saveGame();
        }
        else{
            loadGame();
        }
        numGrid = (GridView) findViewById(R.id.numberGrid);
        numberAdapter = new NumberLayerAdapter(this,
                R.layout.number_layer, numberManager.getNumberLayers());
        numGrid.setAdapter(numberAdapter);
        gestureDetector = new GestureDetectorCompat(this, this);
        setupUndoButton();
        setupAutomaticSaving((Integer)intent.getSerializableExtra("savingInterval"));
        canSave = true;
    }

    /**
     * add listener to undo button
     */
    private void setupUndoButton(){
        Button undoButton = (Button) findViewById(R.id.undoButton);
        undoButton.setBackgroundResource(R.drawable.undo_button);
        undoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(numberManager.undo()){
                    Toast toast = Toast.makeText(NumConcatActivity.this,
                            "You have successfully performed undo operation",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                    updateBoard();
                }
                else{
                    Toast toast = Toast.makeText(NumConcatActivity.this,
                            "You have not made enough moves to apply undo operation",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            }
        });
    }

    @Override
    public void onBackPressed(){
        saveGame();
        finish();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if(!isLoading) {
            if(Math.abs(distanceX) >= 50) {
                processHorizontalSwipe(e1, e2);
            }
            else if(Math.abs(distanceY) >= 50) {
                processVerticalSwipe(e1, e2);
            }
        }
        updateBoard();
        return true;
    }

    /**
     * Process horizontal swipe
     *
     * @param e1 first motion event
     * @param e2 second motion event
     */
    private void processHorizontalSwipe(MotionEvent e1, MotionEvent e2){
        if (e2.getX() - e1.getX() < 0) {
            isLoading = true;
            if(numberManager.shiftLeft()) {
                Toast toast = Toast.makeText(NumConcatActivity.this,
                        "You have swiped left", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else{
                gameOver();
            }
        } else if (e2.getX() - e1.getX() >= 0) {
            isLoading = true;
            if(numberManager.shiftRight()){
                Toast toast = Toast.makeText(NumConcatActivity.this,
                        "You have swiped right", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else{
                gameOver();
            }
        }
    }

    /**
     * Process vertical swipe
     *
     * @param e1 first motion event
     * @param e2 second motion event
     */
    private void processVerticalSwipe(MotionEvent e1, MotionEvent e2){
        if (e2.getY() - e1.getY() >= 0) {
            isLoading = true;
            if(numberManager.shiftUp()){
                Toast toast = Toast.makeText(NumConcatActivity.this,
                        "You have swiped down", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else{
                gameOver();
            }

        } else if (e2.getY() - e1.getY() < 0) {
            isLoading = true;
            if(numberManager.shiftDown()){
                Toast toast = Toast.makeText(NumConcatActivity.this,
                        "You have swiped up", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.BOTTOM, 0, 0);
                toast.show();
            }
            else{
                gameOver();
            }
        }
    }

    /**
     * Inform user that the game is over
     */
    private void gameOver(){
        Toast toast = Toast.makeText(NumConcatActivity.this,
                "GAME OVER", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.TOP, 0, 0);
        toast.show();
        addNewScore();
        finish();
    }

    /**
     * Update current board layout
     */
    private void updateBoard(){
        numberAdapter.notifyDataSetChanged();
        numberAdapter = new NumberLayerAdapter(this,
                R.layout.number_layer, numberManager.getNumberLayers());
        numGrid.setAdapter(numberAdapter);
    }

    @Override
    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        this.gestureDetector.onTouchEvent(event);
        super.onTouchEvent(event);
        if(event.getAction() == MotionEvent.ACTION_UP){
            isLoading = false;
        }
        return true;
    }

    @Override
    protected void loadGame(){
        Intent intent= getIntent();
        int complexity = (Integer)intent.getSerializableExtra("gameComplexity");
        String fileName = "";
        if(complexity == 3){
            fileName = "NumberManager3.ser";
        }
        else if(complexity == 4){
            fileName = "NumberManager4.ser";
        }
        else if(complexity == 5){
            fileName = "NumberManager5.ser";
        }
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                numberManager = (NumberLayerManager) input.readObject();
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

    @Override
    protected void saveGame(){
        Intent intent= getIntent();
        int complexity = (Integer)intent.getSerializableExtra("gameComplexity");
        String fileName = "";
        if(complexity == 3){
            fileName = "NumberManager3.ser";
        }
        else if(complexity == 4){
            fileName = "NumberManager4.ser";
        }
        else if(complexity == 5){
            fileName = "NumberManager5.ser";
        }
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(numberManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Add new score to the current user's list of scores
     */
    private void addNewScore(){
        ScoreboardManager scoreboardManager = new ScoreboardManager(NumConcatActivity.this,
                "Fives");
        User currentUser = scoreboardManager.findUser(SignInActivity.getCurrentUserName());
        int score = numberManager.getGreatestNumber();
        SlidingTilesScore newScore = new SlidingTilesScore(score, "Fives", currentUser);
        newScore.setHasComplexityChoice(true);
        newScore.setGameComplexity(numberManager.getNumRows());
        scoreboardManager.addScore(newScore);
    }
}