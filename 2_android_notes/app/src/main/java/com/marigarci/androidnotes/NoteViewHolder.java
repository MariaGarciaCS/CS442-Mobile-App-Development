package com.marigarci.androidnotes;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    TextView title;
    TextView content;
    TextView time;

    public NoteViewHolder(@NonNull View itemView) {
        super(itemView);

        title = itemView.findViewById(R.id.noteTitle);
        content = itemView.findViewById(R.id.noteContents);
        time = itemView.findViewById(R.id.noteTime);

    }
}
