package iti.jets.mad.noteapp;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName ="note_table")
public class Note {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String userID;
    private String title;
    private String content;

    public void setId(int id) {
        this.id = id;
    }

    public Note(String title, String content,String userID) {
        this.title = title;
        this.content = content;
        this.userID=userID;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getUserID() {
        return userID;
    }
}
