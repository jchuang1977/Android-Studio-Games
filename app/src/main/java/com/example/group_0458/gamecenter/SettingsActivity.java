package com.example.group_0458.gamecenter;
/*
When writing this code, I relied on documentation provided by:
https://docs.oracle.com/javase/8/docs/api/?fbclid=IwAR01h0Gddwo4psVMCJSpszYNG3ZrFy0RtoxbbdbwDOW5tPkR3GS6yg2S75c
https://developer.android.com/reference/packages
 */

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractMap.SimpleImmutableEntry;


/**
 * Activity for editing game settings
 */
public class SettingsActivity extends AppCompatActivity {

    /**
     * TextView representation of current saving interval length
     */
    private TextView currentIntervalText;

    /**
     * current number of undo operations
     */
    private int currentNumUndo = 1;

    /**
     * Return current number of undo operations
     *
     * @return current number of undo operations
     */
    int getCurrentNumUndo() {
        return currentNumUndo;
    }

    /**
     * interval for saving the game
     */
    private int secondsSavingInterval = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        currentIntervalText = (TextView) findViewById(R.id.settingsMenuCurrentValue);
        String newText = Integer.toString(secondsSavingInterval) + " (seconds)";
        currentIntervalText.setText(newText);
        setupReturnButton();
        setupIntervalModifyingButtons();
        setupUndoValueModifyingButtons();
    }

    @Override
    protected void onStart(){
        super.onStart();
        loadSettings();
        updateInterface();
    }

    /**
     * Update interface
     */
    private void updateInterface(){
        String newText = Integer.toString(secondsSavingInterval) + " seconds";
        currentIntervalText.setText(newText);
        TextView currentUndoText = (TextView) findViewById(R.id.undoNumSelection);
        String newUndoText = "undo " + Integer.toString(currentNumUndo) + " times";
        currentUndoText.setText(newUndoText);
    }

    /**
     * Save settings to file
     */
    private void saveSettings(){
        try {
            SimpleImmutableEntry data = new SimpleImmutableEntry<Integer, Integer>(currentNumUndo,
                    secondsSavingInterval);
            ObjectOutputStream outputStream = new ObjectOutputStream(
                    this.openFileOutput("Settings.ser", Activity.MODE_PRIVATE));
            outputStream.writeObject(data);
            outputStream.close();
        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    /**
     * Load settings from file
     */
    @SuppressWarnings("unchecked")
    private void loadSettings(){
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
            saveSettings();
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        } catch (ClassNotFoundException e) {
            Log.e("login activity", "File contained unexpected data type: " +
                    e.toString());
        }
    }

    /**
     * add listeners to interval modifying buttons
     */
    private void setupUndoValueModifyingButtons() {
        TextView currentUndoText = (TextView) findViewById(R.id.undoNumSelection);
        Button subtractUndoButton = (Button) findViewById(R.id.decreaseUndoTimesButton);
        Button increaseUndoButton = (Button) findViewById(R.id.increaseUndoTimesButton);
        subtractUndoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentNumUndo - 1 > 0) {
                    String newText = "undo " + Integer.toString(--currentNumUndo) + " times";
                    currentUndoText.setText(newText);
                    saveSettings();
                }
            }
        }
        );
        increaseUndoButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newText = "undo " + Integer.toString(++currentNumUndo) + "  times";
                currentUndoText.setText(newText);
                saveSettings();
            }
        }
        );
    }

    /**
     * add listeners to return button
     */
    private void setupReturnButton() {
        Button returnButton = (Button) findViewById(R.id.settingsReturnBackButton);
        returnButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }
        );
    }

    /**
     * add listeners to interval modifying buttons
     */
    private void setupIntervalModifyingButtons() {
        Button subtractButton = (Button) findViewById(R.id.settingsSubtractSecondButton);
        Button addButton = (Button) findViewById(R.id.SettingsAddSecondButton);
        subtractButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secondsSavingInterval - 1 > 0) {
                    String newText = Integer.toString(--secondsSavingInterval) + " seconds";
                    currentIntervalText.setText(newText);
                    saveSettings();
                }
            }
        }
        );
        addButton.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (secondsSavingInterval + 1 <= 30) {
                    String newText = Integer.toString(++secondsSavingInterval) + " seconds";
                    currentIntervalText.setText(newText);
                    saveSettings();
                }
            }
        }
        );
    }
}