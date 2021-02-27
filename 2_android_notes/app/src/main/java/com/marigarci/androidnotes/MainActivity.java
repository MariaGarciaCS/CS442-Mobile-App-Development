package com.marigarci.androidnotes;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    private static final int NEW_NOTE_REQUEST = 1234;
    private final ArrayList<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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
                startActivityForResult(newNoteIntent, NEW_NOTE_REQUEST);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    //Other Activities/////////////////////////////////////////

    public void openEditNote(View v){
        Intent intent = new Intent(this, EditNote.class);
//        intent.putExtra("Title", title);
//        intent.putExtra("Content", content);
//        startActivityForResult(intent, EDIT_NOTE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Note note;

//        if (requestCode == EDIT_NOTE_CODE){
//            if (resultCode == NOTE_OK){
//                if (data != null){
//                    note = (Note) data.getSerializableExtra("Title");
//                }
//            }
//        }
    }

    //JSON/////////////////////////////////////////

    @Override
    protected void onResume() {
        noteList.clear();
        noteList.addAll(loadFile());
        super.onResume();

        int numNotes = noteList.size();
        for(int i=0; i<numNotes; i++){
            Note n = noteList.get(i);
//            title.setText(n.getNoteTitle());
//            content.setText(n.getNoteContent());
        }
    }

    private void saveNote(){
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
                Note note = new Note(title, content);
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
}