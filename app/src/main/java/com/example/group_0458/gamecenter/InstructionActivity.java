package com.example.group_0458.gamecenter;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.ArrayList;

/**
 * Activity for giving instructions to the user
 */
public class InstructionActivity extends AppCompatActivity {

    @Override
    @SuppressWarnings("unchecked")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_instruction);
        setUpReturnButton();
        ListView instructionsList = (ListView) findViewById(R.id.instructionList);
        Intent intent = getIntent();
        ArrayList<SimpleImmutableEntry<Integer, String>> instructions =
                (ArrayList<SimpleImmutableEntry<Integer,
                String>>) intent.getSerializableExtra("instructions");
        InstructionAdapter instructionAdapter = new
                InstructionAdapter(this, R.layout.activity_instruction_adapter,
                instructions);
        instructionsList.setAdapter(instructionAdapter);

    }

    /**
     * add listeners to return button
     */
    private void setUpReturnButton(){
        Button returnButton = (Button)findViewById(R.id.exitInstructionsButton);
        returnButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v){
                finish();
            }
        }
        );
    }
}
