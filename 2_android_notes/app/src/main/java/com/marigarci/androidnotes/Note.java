package com.marigarci.androidnotes;

public class Note {
    public String noteTitle;
    public String noteContent;

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
    


}
