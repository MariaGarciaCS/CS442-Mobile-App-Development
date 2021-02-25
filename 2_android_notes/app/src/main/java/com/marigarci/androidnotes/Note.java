package com.marigarci.androidnotes;

import android.util.JsonWriter;

import java.io.IOException;
import java.io.Serializable;
import java.io.StringWriter;

public class Note implements Serializable {
    private String noteTitle;
    private String noteContent;
    //TODO: Time

    public Note(){
        this.noteTitle = "";
        this.noteContent = "";
    }

    public Note(String t, String c){
        this.noteTitle = t;
        this.noteContent = c;
    }

    //GET METHODS
    public String getNoteTitle() {return noteTitle; }
    public String getNoteContent(){return noteContent;}

    //JSON
    public String toString(){
        try{
            StringWriter sw = new StringWriter();
            JsonWriter jsonWriter = new JsonWriter(sw);
            jsonWriter.setIndent(" ");
            jsonWriter.beginObject();
            jsonWriter.name("title").value(getNoteTitle());
            jsonWriter.name("content").value(getNoteContent());
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
