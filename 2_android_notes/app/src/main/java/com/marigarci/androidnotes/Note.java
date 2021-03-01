package com.marigarci.androidnotes;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.Date;

public class Note implements Serializable {
    String noteTitle;
    private String noteContent;
    private String lastEdit;


    public Note(){
        this.noteTitle = null;
        this.noteContent = null;
        this.lastEdit = null;
    }
    public Note(String title, String content, String date){
        this.noteTitle = title;
        this.noteContent = content;
        this.lastEdit = date;
    }

    //SET METHODS
    public void setNoteTitle(String noteTitle) { this.noteTitle = noteTitle; }
    public void setNoteContent(String noteContent) { this.noteContent = noteContent; }
    public void setLastEdit(Date date) { this.lastEdit = date.toString(); }
    public void setAll(String noteTitle, String noteContent, Date date){
        this.noteTitle = noteTitle;
        this.noteContent = noteContent;
        this.lastEdit = date.toString();
    }


    //GET METHODS
    public String getNoteTitle() {return noteTitle; }
    public String getNoteContent(){return noteContent;}
    public String getLastEdit() { return lastEdit; }

    //JSON
    public String toString(){
        try{
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getNoteTitle());
            jsonWriter.name("content").value(getNoteContent());
            jsonWriter.name("time").value(getLastEdit());
            jsonWriter.endObject();
            jsonWriter.close();
            return sw.toString();
        }
        catch (IOException e){
            e.printStackTrace();
        }
        return " ";
    }//end toString

}//end class
