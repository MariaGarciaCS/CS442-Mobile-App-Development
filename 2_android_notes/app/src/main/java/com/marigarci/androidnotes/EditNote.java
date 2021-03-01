package com.marigarci.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
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
        noteContent.setMovementMethod(new ScrollingMovementMethod());

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

        else if (title.matches("")) {
            final AlertDialog.Builder noTitle = new AlertDialog.Builder(this);
            noTitle.setMessage("A note without a title will not be saved");
            noTitle.setTitle("Empty Title");

            noTitle.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            noTitle.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            AlertDialog dialog = noTitle.create();
            dialog.show();

        } else if (!content.trim().isEmpty()) {
            currNote.setAll(title, content, new Date());

            Intent intent = new Intent();
            intent.putExtra("EDIT_NOTE", currNote);
            intent.putExtra("POS", pos);
            setResult(RESULT_OK, intent);
            finish();
        }

    }

    //True is note is unchanged, false is note is modified
    public boolean unchanged(String currentTitle, String currentContent) {
        return currTitle.equals(currentTitle) && currContent.equals(currentContent);
    }


    //Back button alert
    @Override
    public void onBackPressed() {
        String title = noteTitle.getText().toString();
        String content = noteContent.getText().toString();

        if (!unchanged(title, content)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Do you want to save '"+ title + "'?");
            builder.setTitle("Save Note?");
            builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    onSaveNote();
                }
            });
            builder.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });

            AlertDialog dialog = builder.create();
            dialog.show();
        }else {
            finish();
        }
    }

    //Can't save without title
    public void noTitle() {

    }
}