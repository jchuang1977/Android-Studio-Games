package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.CompoundButton;
import android.view.Gravity;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;


/**
 * The initial activity for the sliding puzzle tile game.
 */
public class StartingActivity extends MainMenuActivity {

    /**
     * The board manager.
     */
    private BoardManager boardManager;

    /**
     * current game complexity
     */
    private int currentComplexity = 4;

    /**
     * represents whether current style type selection is 'Custom'
     */
    private static boolean isCustomType = false;

    /**
     * style controller for the game
     */
    private BoardStyleController styleController = null;

    /**
     * Update whether board style is custom or default
     *
     * @param isCustom new value for whether board is custom or default
     */
    static void setIsCustomStyle(boolean isCustom) {
        isCustomType = isCustom;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveSettings();
        currentComplexity = 4;
        boardManager = new BoardManager(currentComplexity, currentComplexity);
        setContentView(R.layout.activity_starting_);
        addStartButtonListener();
        addLoadButtonListener();
        TextView complexityText = (TextView) findViewById(R.id.complexityText);
        String newString = currentComplexity + " x " + currentComplexity;
        complexityText.setText(newString);
        setupStyleButton();
        setupComplexityButtons();
        setupSettingsButton();
        setupSwitches();
        setupLeaderBoardButton();
        setupExitButton();
        loadFileStatuses();
        saveFileStatuses();
    }

    /**
     * add listeners to style switch
     */
    private void setupSwitches() {
        Switch styleSwitch = (Switch) findViewById(R.id.styleSwitch);
        isCustomType = false;
        styleSwitch.setOnCheckedChangeListener(null);
        styleSwitch.setChecked(false);
        styleSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCustomType = !isCustomType;
            }
        });
    }

    /**
     * add listeners to settings button
     */
    private void setupSettingsButton() {
        Button settingsButton = (Button) findViewById(R.id.openSettingsButton);
        settingsButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartingActivity.this,
                        SettingsActivity.class));
            }
        }
        );
    }

    /**
     * add listeners to style button
     */
    private void setupStyleButton() {
        Button signInButton = (Button) findViewById(R.id.styleCreationMenuButton);
        signInButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartingActivity.this,
                        BoardStyleActivity.class));
            }
        }
        );
    }

    /**
     * add listeners to complexity buttons
     */
    private void setupComplexityButtons() {
        Button increaseComplexityButton = (Button) findViewById(R.id.addComplexityButton);
        increaseComplexityButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentComplexity < 5) {
                    String num = Integer.toString(++currentComplexity);
                    String newString = num + " x " + num;
                    TextView complexityText = (TextView) findViewById(R.id.complexityText);
                    complexityText.setText(newString);
                }
            }
        }
        );
        Button decreaseComplexityButton = (Button) findViewById(R.id.subtractComplexityButton);
        decreaseComplexityButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentComplexity > 3) {
                    String num = Integer.toString(--currentComplexity);
                    String newString = num + " x " + num;
                    TextView complexityText = (TextView) findViewById(R.id.complexityText);
                    complexityText.setText(newString);
                }
            }
        }
        );
    }

    /**
     * Activate the start button.
     */
    private void addStartButtonListener() {
        Button startButton = findViewById(R.id.StartButton);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boardManager = new BoardManager(currentComplexity, currentComplexity);
                if (isCustomType && styleController.getCurrentImage() != null) {
                    boardManager.setImageBased(true);
                    saveToFile("Temp.ser");
                    updateFileStatuses();
                    switchToGame();

                } else if (!isCustomType) {
                    boardManager.setImageBased(false);
                    saveToFile("Temp.ser");
                    updateFileStatuses();
                    switchToGame();
                } else {
                    Toast toast = Toast.makeText(StartingActivity.this,
                            "You have set switcher to Curstom Style however you have not" +
                                    " chosen one in Style Creation Menu", Toast.LENGTH_LONG);
                    toast.setGravity(Gravity.BOTTOM, 0, 400);
                    toast.show();
                }
            }
        });
    }

    /**
     * Update and save file statuses
     */
    private void updateFileStatuses(){
        if(currentComplexity == 3){
            fileStatuses[0] = true;
        }
        else if(currentComplexity == 4){
            fileStatuses[1] = true;
        }
        else if(currentComplexity == 5){
            fileStatuses[2] = true;
        }
        saveFileStatuses();
    }

    /**
     * Activate the load button for 3xx3 games
     */
    private void addLoadButtonListener() {
        Button loadButton = findViewById(R.id.load3);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileStatuses[0]) {
                    loadFromFile("SlidingTiles3.ser");
                    saveToFile("Temp.ser");
                    makeToastLoadedText();
                    switchToGame();
                }
                else{
                    Toast toast = Toast.makeText(StartingActivity.this,
                            "You have not saved any 'Sliding Tiles' games with complexity " +
                                    "3x3.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            }
        });
        addLoadButtonListener2();
        addLoadButtonListener3();
    }

    /**
     * Activate the load button for 4x4 games
     */
    private void addLoadButtonListener2() {
        Button loadButton = findViewById(R.id.load4);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileStatuses[1]) {
                    loadFromFile("SlidingTiles4.ser");
                    saveToFile("Temp.ser");
                    makeToastLoadedText();
                    switchToGame();
                }
                else{
                    Toast toast = Toast.makeText(StartingActivity.this,
                            "You have not saved any 'Sliding Tiles' games with complexity " +
                                    "4x4.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            }
        });
    }

    /**
     * Activate the load button for 5x5 games
     */
    private void addLoadButtonListener3() {
        Button loadButton = findViewById(R.id.load5);
        loadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileStatuses[2]) {
                    loadFromFile("SlidingTiles5.ser");
                    saveToFile("Temp.ser");
                    makeToastLoadedText();
                    switchToGame();
                }
                else{
                    Toast toast = Toast.makeText(StartingActivity.this,
                            "You have not saved any 'Sliding Tiles' games with complexity " +
                                    "5x5.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            }
        });
    }

    /**
     * Display that a game was loaded successfully.
     */
    private void makeToastLoadedText() {
        Toast.makeText(this, "Loaded Game", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onStart(){
        super.onStart();
        if(styleController != null) {
            styleController.loadData(StartingActivity.this);
        }
        else{
            styleController = new BoardStyleController(StartingActivity.this);
            styleController.loadData(StartingActivity.this);
        }
    }

    /**
     * Switch to the GameActivity view to play the game.
     */
    void switchToGame() {
        Intent tmp = new Intent(this, GameActivity.class);
        startActivity(tmp);
    }

    /**
     * Load the board manager from fileName.
     *
     * @param fileName the name of the file
     */
    void loadFromFile(String fileName) {
        try {
            InputStream inputStream = this.openFileInput(fileName);
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                boardManager = (BoardManager) input.readObject();
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
    protected void updateLeaderBoard(){
        ScoreboardManager scoreboardManager = new
                ScoreboardManager(StartingActivity.this,
                "Sliding Tiles");
        leaderBoardPerGame = (ArrayList<Score>)scoreboardManager.leaderBoardPerGame();
        if(leaderBoardPerGame == null){
            leaderBoardPerGame = new ArrayList<Score>();
        }
        leaderBoardPerUser = (ArrayList<Score>)scoreboardManager.leaderBoardPerUser();
        if(leaderBoardPerUser == null){
            leaderBoardPerUser = new ArrayList<Score>();
        }
    }

    /**
     * add listeners to open leader-board button
     */
    private void setupLeaderBoardButton() {
        Button scoreBoardButton = (Button) findViewById(R.id.viewLeaderBoardButton);
        scoreBoardButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(StartingActivity.this,
                        ScoreboardActivity.class);
                ScoreboardManager scoreboardManager = new
                        ScoreboardManager(StartingActivity.this,
                        "Sliding Tiles");
                newIntent.putExtra("boardPerGame",
                        (Serializable) leaderBoardPerGame);
                newIntent.putExtra("boardPerUser",
                        (Serializable) leaderBoardPerUser);
                String instructions = "Score = Number of moves made x complexity " +
                        "of the board. The smaller the score, the higher the ranking. Scores are " +
                        "arranged top-to-bottom(highest score) in increasing order.";
                newIntent.putExtra("perGameDescription", (Serializable) instructions);
                startActivity(newIntent);
            }
        }
        );
    }

    /**
     * add listeners to exit game button
     */
    private void setupExitButton(){
        Button exitGameButton = (Button)findViewById(R.id.exitSlidingTilesButton);
        exitGameButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                PopupDialog dialog = new PopupDialog();
                dialog.setTitle("Exit game?");
                dialog.setReply("Yes");
                dialog.setNegativeReply("No");
                dialog.setActionObject(StartingActivity.this);
                String message = "Are you sure you want to exit the game? Don't worry" +
                        " your progress will be automatically saved.";
                dialog.setMessage(message);
                dialog.show(getSupportFragmentManager(), message);
            }
        }
        );
    }

    /**
     * Save settings to file
     */
    private void saveSettings(){
        try {
            AbstractMap.SimpleImmutableEntry data = new AbstractMap.SimpleImmutableEntry<Integer,
                    Integer>(1, 5);
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput("Settings.ser", Activity.MODE_PRIVATE));
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    protected void saveFileStatuses(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput("FileStatusesSlidingTiles.ser", MODE_PRIVATE));
            outputStream.writeObject(fileStatuses);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    protected void loadFileStatuses(){
        try {
            InputStream inputStream = this.openFileInput("FileStatusesSlidingTiles.ser");
            if (inputStream != null) {
                ObjectInputStream input = new ObjectInputStream(inputStream);
                fileStatuses = (boolean[]) input.readObject();
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
}