package com.marigarci.androidnotes;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NoteAdapter extends RecyclerView.Adapter<NoteViewHolder>{
    private static final String TAG = "NotesAdapter";
    private ArrayList<Note> noteList;
    private MainActivity mainAct;

    NoteAdapter(ArrayList<Note> nList, MainActivity ma){
        this.noteList = nList;
        mainAct = ma;
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: MAKING NEW ViewHolder");

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.note_entry, parent, false);

        itemView.setOnClickListener(mainAct);
        itemView.setOnLongClickListener(mainAct);

        return  new NoteViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        Note n = noteList.get(position);

        holder.title.setText(n.getNoteTitle());
        if(n.getNoteContent().length() > 80) {
            holder.content.setText(n.getNoteContent().substring(0, 79) + "...");
        } else{
            holder.content.setText(n.getNoteContent());
        }
        holder.time.setText(n.getLastEdit());
    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }
}
