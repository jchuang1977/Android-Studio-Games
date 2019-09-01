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

/**
 * Game selection activity
 */
public class GameSelectionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_selection);
        setupGameSelectionButtons();
        setUpBackButton();
    }

    /**
     * add listeners to game selection buttons
     */
    private void setupGameSelectionButtons(){
        Button b = (Button) findViewById(R.id.slidingTilesButton);
        b.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent newIntent = new Intent(GameSelectionActivity.this,
                        StartingActivity.class);
                startActivity(newIntent);
            }
        }
        );
        Button b2 = (Button) findViewById(R.id.cornersGameButton);
        b2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent newIntent = new Intent(GameSelectionActivity.this,
                        NumConcatMainMenuActivity.class);
                startActivity(newIntent);
            }
        }
        );
    }

    /**
     * add listeners to return button
     */
    private void setUpBackButton(){
        Button b2 = (Button) findViewById(R.id.backToLogIn);
        b2.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        }
        );
    }
}