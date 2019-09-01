package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.ViewTreeObserver;
import android.widget.Button;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.widget.Toast;
import android.view.MotionEvent;
import android.support.v4.view.GestureDetectorCompat;


/**
 * The game activity.
 */
public class GameActivity extends GamePlayActivity implements Observer,
        GestureDetector.OnGestureListener {

    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * The buttons to display.
     */
    private List<Button> tileButtons;

    /**
     * Grid view for gesture detection
     */
    private GestureDetectGridView gridView;

    /**
     * column width and column height
     */
    private int columnWidth, columnHeight;

    /**
     * current number of undo operations and saving interval
     */
    private int currentNumUndo, secondsSavingInterval;

    /**
     * style controller for the game
     */
    private BoardStyleController styleController;

    /**
     * Set up the background image for each button based on the master list
     * of positions, and then call the adapter to set the view.
     */
    // Display
    public void display() {
        updateTileButtons();
        gridView.setAdapter(new CustomAdapter((ArrayList<Button>)tileButtons, columnWidth,
                columnHeight));
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
        if (distanceX <= -50) {
            if (boardManager.undo(currentNumUndo)) {
                Toast toast = Toast.makeText(GameActivity.this,
                        "You have successfully performed action undo " +
                                Integer.toString(currentNumUndo) +
                                " times", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 400);
                toast.show();
                updateTileButtons();
            } else {
                Toast toast = Toast.makeText(GameActivity.this,
                        "You have not performed enough actions to perform undo " +
                                Integer.toString(currentNumUndo) +
                                " times", Toast.LENGTH_LONG);
                toast.setGravity(Gravity.BOTTOM, 0, 400);
                toast.show();
            }
        }
        return true;
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
        return true;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        styleController = new BoardStyleController(GameActivity.this);
        styleController.loadData(GameActivity.this);
        loadFromFile("Temp.ser");
        if(boardManager.isImageBase() && boardManager.getBoard().getBoardImage() == null){
            boardManager.getBoard().setBoardImage(new BitmapDataObject(
                    styleController.getCurrentImage()));
        }
        createTileButtons(this);
        setContentView(R.layout.activity_main);
        gestureDetector = new GestureDetectorCompat(this, this);
        // Add View to activity
        gridView = findViewById(R.id.grid);
        gridView.setNumColumns(boardManager.getNumCols());
        gridView.setBoardManager(boardManager);
        gridView.getMovementController().setActivity(this);
        boardManager.getBoard().addObserver(this);
        // Observer sets up desired dimensions as well as calls our display function
        setUpGridView();
        loadData();
        setupAutomaticSaving(secondsSavingInterval);
        showInstructions();
        Intent intent = getIntent();
        saveGame();
        canSave = true;
    }

    @Override
    public void onBackPressed(){
        saveGame();
        finish();
    }

    /**
     * load settings data from file
     */
    private void loadData(){
        try {
            InputStream inputStream = this.openFileInput("Settings.ser");
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                SimpleImmutableEntry<Integer, Integer> data = (SimpleImmutableEntry<Integer,
                        Integer>) input.readObject();
                currentNumUndo = data.getKey();
                secondsSavingInterval = data.getValue();
                inputStream.close();
            }
        } catch (FileNotFoundException e) {
            secondsSavingInterval = 5;
            currentNumUndo = 1;
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " +
                    e.toString());
        }
    }

    /**
     * Set up GridView
     */
    private void setUpGridView(){
        gridView.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        gridView.getViewTreeObserver().removeOnGlobalLayoutListener(
                                this);
                        int displayWidth = gridView.getMeasuredWidth();
                        int displayHeight = gridView.getMeasuredHeight();
                        columnWidth = displayWidth / boardManager.getNumCols();
                        columnHeight = displayHeight / boardManager.getNumRows();
                        display();
                    }
                });
    }

    /**
     * Show instructions for how undo works
     */
    private void showInstructions(){
        Toast toasts = Toast.makeText(GameActivity.this,
                "Swipe left to undo (if you are using mouse click and move your mouse left)",
                Toast.LENGTH_LONG);
        toasts.setGravity(Gravity.BOTTOM, 0, 200);
        toasts.show();
    }

    /**
     * Create the buttons for displaying the tiles.
     *
     * @param context the context
     */
    private void createTileButtons(Context context) {
        tileButtons = new ArrayList<>();
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        if (boardManager.getBoard().getBoardImage() != null) {
            bitmaps = (ArrayList<Bitmap>)styleController.performSlicing(
                    boardManager.getBoard().getBoardImage().getCurrentImage(),
                    boardManager.getNumRows());
        } else if (styleController.getCurrentImage() != null) {
            boardManager.getBoard().setBoardImage(new BitmapDataObject(
                    styleController.getCurrentImage()));
            bitmaps = (ArrayList<Bitmap>)styleController.performSlicing(
                    styleController.getCurrentImage(),
                    boardManager.getNumRows());
        }
        drawTileButtons(context, bitmaps);
    }

    /**
     * Draw tile buttons using given context and bitmaps
     *
     * @param context given context
     * @param bitmaps array list of bitmaps used for tile buttons
     */
    private void drawTileButtons(Context context, ArrayList<Bitmap> bitmaps){
        Board board = boardManager.getBoard();
        for (int row = 0; row != boardManager.getNumRows(); row++) {
            for (int col = 0; col != boardManager.getNumCols(); col++) {
                Button tmp = new Button(context);
                tmp.setBackgroundResource(board.getTile(row, col).getBackground());
                if (!boardManager.isImageBase()) {
                    if (!boardManager.getBoard().isEmptyTile(row, col)) {
                        String newString = Integer.toString(
                                boardManager.getBoard().getTile(row, col).getTileNum());
                        tmp.setText(newString);
                    } else {
                        tmp.setText("");
                    }
                    tmp.setTextSize(30);
                } else {
                    Drawable dw = new BitmapDrawable(getResources(), bitmaps.get(
                            boardManager.getBoard().getTile(row, col).getTileNum() - 1));
                    if (!(boardManager.getBoard().getTile(row, col).isEmpty())) {
                        tmp.setBackgroundDrawable(dw);
                    }
                }
                this.tileButtons.add(tmp);
            }
        }
    }


    /**
     * Update the backgrounds on the buttons to match the tiles.
     */
    private void updateTileButtons() {
        ArrayList<Bitmap> bitmaps = new ArrayList<>();
        if (boardManager.getBoard().getBoardImage() != null) {
            bitmaps = (ArrayList<Bitmap>)styleController.performSlicing(
                    boardManager.getBoard().getBoardImage().getCurrentImage(),
                    boardManager.getNumRows());
        } else if (styleController.getCurrentImage() != null) {
            bitmaps = (ArrayList<Bitmap>)styleController.performSlicing(
                    styleController.getCurrentImage(),
                    boardManager.getNumRows());
        }
        applyStyleToButtons(bitmaps);
    }

    /**
     * Apply new style to tile buttons
     *
     * @param bitmaps array list of bitmaps used for tile buttons
     */
    private void applyStyleToButtons(ArrayList<Bitmap> bitmaps){
        Board board = boardManager.getBoard();
        int nextPos = 0;
        for (Button b : tileButtons) {
            int row = nextPos / boardManager.getNumRows();
            int col = nextPos % boardManager.getNumCols();
            b.setBackgroundResource(board.getTile(row, col).getBackground());
            if (!boardManager.isImageBase()) {
                if (!boardManager.getBoard().isEmptyTile(row, col)) {
                    String newString = Integer.toString(boardManager.getBoard().getTile(row,
                            col).getTileNum());
                    b.setText(newString);
                } else {
                    b.setText("");
                }
                b.setTextSize(30);
            } else {
                b.setText("");
                Drawable dw = new BitmapDrawable(getResources(), bitmaps.get(
                        boardManager.getBoard().getTile(row, col).getTileNum() - 1));
                if (!(boardManager.getBoard().getTile(row, col).isEmpty())) {
                    b.setBackgroundDrawable(dw);
                }
            }
            nextPos++;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveToFile("Temp.ser");
        if(boardManager.getNumRows() == 3) {
            saveToFile("SlidingTiles3.ser");
        }
        if(boardManager.getNumRows() == 4) {
            saveToFile("SlidingTiles4.ser");
        }
        if(boardManager.getNumRows() == 5) {
            saveToFile("SlidingTiles5.ser");
        }
    }

    @Override
    protected void saveGame(){
        saveToFile("Temp.ser");
        if(boardManager.getNumRows() == 3) {
            saveToFile("SlidingTiles3.ser");
        }
        if(boardManager.getNumRows() == 4) {
            saveToFile("SlidingTiles4.ser");
        }
        if(boardManager.getNumRows() == 5) {
            saveToFile("SlidingTiles5.ser");
        }
    }

    @Override
    protected void loadGame(){
        loadFromFile("Temp.ser");
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    protected void loadFromFile(String fileName) {

        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManager) input.readObject();
                StartingActivity.setIsCustomStyle(boardManager.isImageBase());
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
     * Save the board manager to fileName.
     *
     * @param fileName the name of the file
     */
    public void saveToFile(String fileName) {
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput(fileName, MODE_PRIVATE));
            outputStream.writeObject(boardManager);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    public void update(Observable o, Object arg) {
        display();
    }

    /**
     * Add new score to the current user's list of scores
     */
    void addNewScore(){
        ScoreboardManager scoreboardManager = new ScoreboardManager(GameActivity.this,
                BoardManager.gameName());
        User currentUser = scoreboardManager.findUser(SignInActivity.getCurrentUserName());
        int res = boardManager.getNumRows() *  boardManager.getNumOfMoves();
        SlidingTilesScore newScore = new SlidingTilesScore(res,
                BoardManager.gameName(), currentUser);
        newScore.setHasComplexityChoice(true);
        newScore.setGameComplexity(boardManager.getNumCols());
        scoreboardManager.addScore(newScore);
    }
}