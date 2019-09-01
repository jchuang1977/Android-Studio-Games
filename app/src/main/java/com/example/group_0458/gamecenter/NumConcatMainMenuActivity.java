package com.example.group_0458.gamecenter;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;
import java.util.List;

/**
 * Main menu for game Fives
 */
public class NumConcatMainMenuActivity extends MainMenuActivity {

    /**
     * current game complexity
     */
    private int gameComplexity = 3;

    /**
     * current saving interval
     */
    private int savingInterval = 5;

    /**
     * name of the game
     */
    private String gameName = "Fives";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_num_concat_main_menu);
        setupComplexityButtons();
        setupAutosavingIntervalButtons();
        setupNewGameButton();
        setupLoadGameButton();
        setupExitGameButton();
        setupGameInfoButton();
        setupLeaderBoardButton();
        updateLeaderBoard();
        loadFileStatuses();
        saveFileStatuses();
    }

    /**
     * add listeners to game complexity selection buttons
     */
    private void setupComplexityButtons(){
        Button increaseComplexityButton = (Button)findViewById(R.id.concatIncreaseComplexityButton);
        TextView complexityText = (TextView)findViewById(R.id.concatComplexityText);
        increaseComplexityButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(gameComplexity < 5) {
                    gameComplexity++;
                    String newText = Integer.toString(gameComplexity) + "x" +
                            Integer.toString(gameComplexity);
                    complexityText.setText(newText);
                }
            }
        }
        );
        Button decreaseComplexityButton = (Button)findViewById(R.id.concatDecreaseComplexityButton);
        decreaseComplexityButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(gameComplexity > 3) {
                    gameComplexity--;
                    String newText = Integer.toString(gameComplexity) + "x" +
                            Integer.toString(gameComplexity);
                    complexityText.setText(newText);
                }
            }
        }
        );
    }

    /**
     * add listeners to auto-save interval selection buttons
     */
    private void setupAutosavingIntervalButtons(){
        Button increaseSavingIntervalButton = (Button)findViewById(
                R.id.concatIncreaseAutosavingIntervalButton);
        TextView intervalText = (TextView)findViewById(R.id.concatAutosavingIntervalText);
        increaseSavingIntervalButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                savingInterval++;
                String newText = Integer.toString(savingInterval) + " seconds";
                intervalText.setText(newText);
            }
        }
        );
        Button decreaseSavingIntervalButton = (Button)findViewById(
                R.id.concatDecreaseAutosavingIntervalButton);
        decreaseSavingIntervalButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(savingInterval > 1) {
                    savingInterval--;
                    String newText = Integer.toString(savingInterval) + " seconds";
                    intervalText.setText(newText);
                }
            }
        }
        );
    }

    /**
     * add listeners to new game button
     */
    private void setupNewGameButton(){
        Button newGameButton = (Button)findViewById(R.id.concatNewGameButton);
        newGameButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent newIntent = new Intent(NumConcatMainMenuActivity.this,
                        NumConcatActivity.class);
                newIntent.putExtra("gameComplexity", gameComplexity);
                newIntent.putExtra("savingInterval", gameComplexity);
                newIntent.putExtra("newGame", true);
                updateFileStatuses();
                startActivity(newIntent);
            }
        }
        );
    }

    /**
     * add listeners to load game button for 3x3 games
     */
    private void setupLoadGameButton(){
        Button loadGameButton = (Button)findViewById(R.id.concatLoadGameButton3);
        loadGameButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(fileStatuses[0]) {
                    Intent newIntent = new Intent(NumConcatMainMenuActivity.this,
                            NumConcatActivity.class);
                    newIntent.putExtra("gameComplexity", 3);
                    newIntent.putExtra("savingInterval", gameComplexity);
                    newIntent.putExtra("newGame", false);
                    startActivity(newIntent);
                }
                else{
                    Toast toast = Toast.makeText(NumConcatMainMenuActivity.this,
                            "You have not saved any 'Fives' games with complexity 3x3.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            }
        }
        );
        setupLoadGameButton2();
        setupLoadGameButton3();
    }

    /**
     * add listeners to load game button for 4x4 games
     */
    private void setupLoadGameButton2(){
        Button loadGameButton = (Button)findViewById(R.id.concatLoadGameButton4);
        loadGameButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(fileStatuses[1]) {
                    Intent newIntent = new Intent(NumConcatMainMenuActivity.this,
                            NumConcatActivity.class);
                    newIntent.putExtra("gameComplexity", 4);
                    newIntent.putExtra("savingInterval", gameComplexity);
                    newIntent.putExtra("newGame", false);
                    startActivity(newIntent);
                }
                else{
                    Toast toast = Toast.makeText(NumConcatMainMenuActivity.this,
                            "You have not saved any 'Fives' games with complexity 4x4.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            }
        }
        );
    }

    /**
     * add listeners to load game button for 5x5 games
     */
    private void setupLoadGameButton3(){
        Button loadGameButton = (Button)findViewById(R.id.concatLoadGameButton5);
        loadGameButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                if(fileStatuses[2]) {
                    Intent newIntent = new Intent(NumConcatMainMenuActivity.this,
                            NumConcatActivity.class);
                    newIntent.putExtra("gameComplexity", 5);
                    newIntent.putExtra("savingInterval", gameComplexity);
                    newIntent.putExtra("newGame", false);
                    startActivity(newIntent);
                }
                else{
                    Toast toast = Toast.makeText(NumConcatMainMenuActivity.this,
                            "You have not saved any 'Fives' games with complexity 5x5.",
                            Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.BOTTOM, 0, 300);
                    toast.show();
                }
            }
        }
        );
    }

    /**
     * add listeners to exit game button
     */
    private void setupExitGameButton(){
        Button exitGameButton = (Button)findViewById(R.id.concatExitGameButton);
        exitGameButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                PopupDialog dialog = new PopupDialog();
                dialog.setTitle("Exit game?");
                dialog.setReply("Yes");
                dialog.setNegativeReply("No");
                dialog.setActionObject(NumConcatMainMenuActivity.this);
                String message = "Are you sure you want to exit the game? Don't worry" +
                        " your progress will be automatically saved.";
                dialog.setMessage(message);
                dialog.show(getSupportFragmentManager(), message);
            }
        }
        );
    }

    /**
     * add listeners to game info button
     */
    private void setupGameInfoButton(){
        Button instructionsButton = (Button)findViewById(R.id.gameRulesButton);
        instructionsButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent newIntent = new Intent(NumConcatMainMenuActivity.this,
                        InstructionActivity.class);
                newIntent.putExtra("instructions", (ArrayList<SimpleImmutableEntry<Integer,
                        String>>)getInstructions());
                startActivity(newIntent);
            }
        }
        );
    }

    /**
     * Return list of instructions
     *
     * @return list of instructions
     */
    private List<SimpleImmutableEntry<Integer, String>> getInstructions(){
        ArrayList<SimpleImmutableEntry<Integer, String>> result = new
                ArrayList<SimpleImmutableEntry<Integer, String>>();
        String instr1 = "2s can only be added with 3s. Alternatively, 3s can only be concatenated " +
                "with 2s.";
        String instr2 = "Any values greater than or equal to 5 can only be concatenated with values" +
                " that are are equal to themselves.";
        String instr3 = "Newly-added values are highlighted with orange colour.";
        result.add(new SimpleImmutableEntry<Integer, String>(R.drawable.fives_instr1, instr1));
        result.add(new SimpleImmutableEntry<Integer, String>(R.drawable.fives_instr2, instr2));
        result.add(new SimpleImmutableEntry<Integer, String>(R.drawable.fives_instr3, instr3));
        return result;
    }

    @Override
    protected void updateLeaderBoard(){
        ScoreboardManager scoreboardManager = new
                ScoreboardManager(NumConcatMainMenuActivity.this,
                gameName);
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
     * add listener to leader board button
     */
    private void setupLeaderBoardButton(){
        Button leaderBoardButton = (Button) findViewById(R.id.leaderBoardButton);
        leaderBoardButton.setBackgroundResource(R.drawable.leader_board);
        leaderBoardButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newIntent = new Intent(NumConcatMainMenuActivity.this,
                        ScoreboardActivity.class);
                newIntent.putExtra("boardPerGame",
                        (Serializable) leaderBoardPerGame);
                newIntent.putExtra("boardPerUser",
                        (Serializable) leaderBoardPerUser);
                String instructions = "Scores earned by users correspond to the greatest number " +
                        "they were able to get in a single Fives game. Scores are sorted " +
                        "top-to-bottom from highest to lowest.";
                newIntent.putExtra("perGameDescription", (Serializable) instructions);
                startActivity(newIntent);
            }
        });
    }

    @Override
    protected void saveFileStatuses(){
        try {
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput("FileStatusesFives.ser", MODE_PRIVATE));
            outputStream.writeObject(fileStatuses);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    @Override
    protected void loadFileStatuses(){
        try {
            InputStream inputStream = this.openFileInput("FileStatusesFives.ser");
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

    /**
     * Update and save file statuses
     */
    private void updateFileStatuses(){
        if(gameComplexity == 3){
            fileStatuses[0] = true;
        }
        else if(gameComplexity == 4){
            fileStatuses[1] = true;
        }
        else if(gameComplexity == 5){
            fileStatuses[2] = true;
        }
        saveFileStatuses();
    }
}