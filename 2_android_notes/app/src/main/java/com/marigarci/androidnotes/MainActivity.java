package com.marigarci.androidnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, View.OnLongClickListener{
    private static final String TAG = "MainActivity";
    private static final int NEW_NOTE = 1;
    private static final int EDIT_NOTE = 2;
    private final ArrayList<Note> noteList = new ArrayList<>();

    RecyclerView recyclerView;
    NoteAdapter nAdapter;

    private Note nRecieved = new Note();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //JSON
        noteList.clear();
        loadFile();


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Recycler + Adapter
        nAdapter = new NoteAdapter(noteList, this);
        recyclerView = findViewById(R.id.noteRecycler);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        setTitle("Android Notes" + "(" + nAdapter.getItemCount() + ")" );
    }

    //JSON/////////////////////////////////////////
    private void saveNoteJSON(){
        Log.d(TAG, "saveNote: Saving JSON File");

        try{
            FileOutputStream fos = getApplicationContext().openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);
            PrintWriter printWriter = new PrintWriter(fos);
            printWriter.print(noteList);
            printWriter.close();
            fos.close();
            Log.d(TAG, "saveNote: JSON:\n" + noteList.toString());
        }
        catch (Exception e){
            e.getStackTrace();
        }
    }

//    @Override
//    protected void onPause() {
//        saveNoteJSON();
//        super.onPause();
//    }

    private ArrayList<Note> loadFile(){
        Log.d(TAG, "loadFile: Loading JSON File");
        try {
            InputStream is = getApplicationContext().openFileInput("Note.json");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));

            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null){
                sb.append(line);
            }

            JSONArray jsonArray = new JSONArray(sb.toString());
            for (int i=0; i<jsonArray.length(); i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String title = jsonObject.getString("title");
                String content = jsonObject.getString("content");
                String time = jsonObject.getString("time");
                Note note = new Note(title, content, time);
                noteList.add(note);
            }
        }
        catch (FileNotFoundException e){
            Toast.makeText(this, "No JSON Note File Present", Toast.LENGTH_SHORT).show();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return noteList;
    }


    //Options Menu/////////////////////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.infoBtn:
                Intent infoIntent = new Intent(this, AppInfo.class);
                startActivity(infoIntent);
                break;
            case R.id.new_note_btn:
                Note emptyNote = new Note();
                Intent newNoteIntent = new Intent(this, EditNote.class);
                newNoteIntent.putExtra("NEW_NOTE", emptyNote);
                startActivityForResult(newNoteIntent, NEW_NOTE);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Navigating Activities/////////////////////////////////////////
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_NOTE){
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    nRecieved = (Note) data.getSerializableExtra("EDIT_NOTE");
                    if (nRecieved != null) {
                        noteList.add(0, nRecieved);
                        saveNoteJSON();
                        nAdapter.notifyDataSetChanged();
                        setTitle("Android Notes" + "(" + nAdapter.getItemCount() + ")" );
                    }
                }else {
                    Toast.makeText(this, "Empty note not saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (requestCode == EDIT_NOTE){
            if (resultCode == RESULT_OK) {
                if (data != null) {
                    nRecieved = (Note) data.getSerializableExtra("EDIT_NOTE");
                    int pos = data.getIntExtra("POS", 0);
                    if (nRecieved != null) {
                        noteList.remove(pos);
                        noteList.add(0, nRecieved);
                        //TODO:SAVE EDITS
                        saveNoteJSON();
                        nAdapter.notifyDataSetChanged();
                        setTitle("Android Notes" + "(" + nAdapter.getItemCount() + ")" );
                    }
                }else {
                    Toast.makeText(this, "Empty note not saved", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else {
            Toast.makeText(this, "Unexpected code received: " + requestCode, Toast.LENGTH_SHORT).show();
        }
    }

    // CLICKING THE NOTES
    @Override
    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        Log.d(TAG, "SENT POS:" + pos);
        Note selectedNote = noteList.get(pos);
        Intent editNoteIntent = new Intent(this, EditNote.class);
        editNoteIntent.putExtra("EDIT_NOTE", selectedNote);
        editNoteIntent.putExtra("POS", pos);
        nAdapter.notifyDataSetChanged();
        startActivityForResult(editNoteIntent, EDIT_NOTE);
    }

    @Override
    public boolean onLongClick(final View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                deleteNote(v);
            }
        });
        builder.setNegativeButton("Cancel", null);
        builder.setMessage("Do you want to permanently delete this note?");
        builder.setTitle("Delete Note?");
        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }

    public void deleteNote(View v){
        int pos = recyclerView.getChildLayoutPosition(v);
        noteList.remove(pos);
        saveNoteJSON();
        nAdapter.notifyDataSetChanged();
        if(nAdapter.getItemCount()==0){
            setTitle("Android Notes");
        }else {
            setTitle("Android Notes" + "(" + nAdapter.getItemCount() + ")");
        }
    }
}

