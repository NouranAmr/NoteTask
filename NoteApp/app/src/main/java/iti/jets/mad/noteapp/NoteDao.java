package iti.jets.mad.noteapp;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NoteDao {

    @Insert
    void insertNote(Note note);

    @Query("DELETE from note_table WHERE id=:noteID")
    void deleteNote( String noteID);

    @Query("UPDATE note_table set content =:content , title=:title WHERE id=:noteID")
    void updateNotes(String title , String content,String noteID);

    @Query("DELETE from note_table WHERE userID =:userID")
    void deleteAllNotes(String userID);

    @Query("SELECT * from note_table WHERE userID =:userID ")
    LiveData<List<Note>> getAllNotes(String userID);

}
