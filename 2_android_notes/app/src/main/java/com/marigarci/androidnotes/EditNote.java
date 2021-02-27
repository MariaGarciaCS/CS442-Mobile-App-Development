package com.marigarci.androidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;

public class EditNote extends AppCompatActivity {
    private static final String TAG = "EDIT_NOTE";
    EditText noteTitle;
    EditText noteContent;

    private Note currNote;
    private String currContent, currTitle;
    private int pos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        noteTitle = findViewById(R.id.editTitle);
        noteContent = findViewById(R.id.editContent);

        Intent intent = getIntent();

        //if creating new note
        if (intent.hasExtra("NEW")){
            currNote = (Note) intent.getSerializableExtra("NEW");
            currContent = "";
            currTitle = "";
        }
        //if modifying existing note
        if (intent.hasExtra("EDIT")){
            currNote = (Note) intent.getSerializableExtra("EDIT");
            if(currNote != null){
                currContent = currNote.getNoteContent();
                currTitle = currNote.getNoteTitle();

                noteTitle.setText(currTitle);
                noteContent.setText(currContent);

            }

            if (intent.hasExtra("POS")){
                pos = intent.getIntExtra("POS", 0);
                Log.d(TAG, "IntentPositionReceived = "+ pos);
            }
        }
    }
    
    //TODO: Options menu with save button

    //TODO: If back button is pressed, ask to save
}