package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Gravity;
import java.util.ArrayList;
import android.widget.ListView;

/**
 * Activity viewing score board
 */
public class ScoreboardActivity extends AppCompatActivity {

    /**
     * indicates whether score board is per game
     */
    private boolean perGame = true;

    /**
     * user adapter for score board
     */
    private UserAdapter userAdapter = null;

    /**
     * list view for score board
     */
    private ListView userListView = null;

    /**
     * list of scores per game
     */
    private ArrayList<Score> scoresPerGame = null;

    /**
     * scores per user
     */
    private ArrayList<Score> scoresPerUser = null;

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        userListView = (ListView) findViewById(R.id.userListView);
        setupTypeSwitch();
        setupReturnButton();
        displayInformation();
        Intent intent = getIntent();
        scoresPerGame = (ArrayList<Score>) intent.getSerializableExtra("boardPerGame");
        scoresPerUser = (ArrayList<Score>) intent.getSerializableExtra("boardPerUser");
        perGame = true;
        userAdapter = new UserAdapter(this, R.layout.scoreboard_entry,
                scoresPerGame);
        if (scoresPerGame.size() > 0) {
            userListView.setAdapter(userAdapter);
        } else {
            Toast toast = Toast.makeText(ScoreboardActivity.this,
                    "Leader board has noting to display. Please play several games to " +
                            "receive scores.",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.BOTTOM, 0, 400);
            toast.show();
        }
    }

    /**
     * add listeners to return button
     */
    private void setupReturnButton(){
        Button returnButton = (Button)findViewById(R.id.backButton);
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * add listeners to type switch
     */
    private void setupTypeSwitch(){
        Switch leaderBoardSwitch = (Switch) findViewById(R.id.scoreBoardTypeSwitch);
        leaderBoardSwitch.setOnCheckedChangeListener(null);
        leaderBoardSwitch.setChecked(false);
        leaderBoardSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                perGame = !perGame;
                updateScoreBoard();
            }
        });
    }

    /**
     * update score board
     */
    private void updateScoreBoard(){
        userAdapter.notifyDataSetChanged();
        if (perGame) {
            userAdapter = new UserAdapter(this, R.layout.scoreboard_entry,
                    scoresPerGame);
        } else {
            userAdapter = new UserAdapter(this, R.layout.scoreboard_entry,
                    scoresPerUser);
        }
        userListView.setAdapter(userAdapter);
        displayInformation();
    }

    /**
     * Display relevant information to the user.
     */
    private void displayInformation(){
        String info = "";
        TextView informationText = (TextView) findViewById(R.id.informationText);
        if (perGame) {
            Toast toasts = Toast.makeText(ScoreboardActivity.this,
                    "YOU ARE VIEWING SCOREBOARD PER GAME",
                    Toast.LENGTH_SHORT);
            toasts.setGravity(Gravity.BOTTOM, 0, 800);
            toasts.show();
            info = (String)getIntent().getSerializableExtra("perGameDescription");
        }
        else{
            Toast toasts = Toast.makeText(ScoreboardActivity.this,
                    "YOU ARE VIEWING SCOREBOARD PER USER",
                    Toast.LENGTH_SHORT);
            toasts.setGravity(Gravity.BOTTOM, 0, 800);
            toasts.show();
            info = "Scores for current user for all games are shown. Since all games are scored " +
                    "differently, there is no sorting in this list.";
        }
        informationText.setText(info);
    }
}