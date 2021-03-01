package com.marigarci.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;

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
        if (intent.hasExtra("NEW_NOTE")){
            currNote = (Note) intent.getSerializableExtra("NEW_NOTE");
            currContent = "";
            currTitle = "";
        }
        //if modifying existing note
        if (intent.hasExtra("EDIT_NOTE")){
            currNote = (Note) intent.getSerializableExtra("EDIT_NOTE");
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

    //Options menu with save button
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_note_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.saveNote){
            onSaveNote();
        }
        return super.onOptionsItemSelected(item);
    }

    //Return note to mainActivity, will be saved
    public void onSaveNote() {
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        if (unchanged(title, content)){finish();}

        if (title.trim().isEmpty()) {
            Toast.makeText(this, "No title, note not saved", Toast.LENGTH_LONG).show();
        } else if (!content.trim().isEmpty()) {
            currNote.setAll(title, content, new Date());

            Intent intent = new Intent();
            intent.putExtra("EDIT_NOTE", currNote);
            intent.putExtra("POS", pos);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    //True is note is unchanged, false is note is modified
    public boolean unchanged(String currentTitle, String currentContent) {
        return currTitle.equals(currentTitle) && currContent.equals(currentContent);
    }



    //TODO: If back button is pressed, ask to save
}