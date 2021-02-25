package com.marigarci.androidnotes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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
    private final ArrayList<Note> noteList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.opt_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.infoBtn){
            return true;
        } else if (item.getItemId() == R.id.new_note_btn){
            return true;
        } else{
            return super.onOptionsItemSelected(item);
        }
    }

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