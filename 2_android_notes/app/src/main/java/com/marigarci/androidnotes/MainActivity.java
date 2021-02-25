package com.marigarci.androidnotes;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
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