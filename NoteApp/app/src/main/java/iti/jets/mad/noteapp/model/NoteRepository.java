package iti.jets.mad.noteapp.model;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import java.util.List;

import iti.jets.mad.noteapp.model.Note;
import iti.jets.mad.noteapp.model.NoteDao;
import iti.jets.mad.noteapp.model.NoteDataBase;

public class NoteRepository {

    private NoteDao noteDao;
    private LiveData<List<Note>> allNotes;
    private SharedPreferences sharedpreferences;
    private String userID;
    public NoteRepository(Application application) {
        NoteDataBase noteDataBase = NoteDataBase.getInstance(application);
        sharedpreferences = PreferenceManager.getDefaultSharedPreferences(application);
        noteDao = noteDataBase.noteDao();
        userID=(sharedpreferences.getString("userID",null));
        allNotes = noteDao.getAllNotes(userID);
    }

    public void insert(Note note) {

        new InsertNoteAsyncTask(noteDao).execute(note);

    }

    public void update(Note note) {
        new UpdateNoteAsyncTask(noteDao).execute(note);
    }

    public void delete(Note note) {
        new DeleteNoteAsyncTask(noteDao).execute(note);
    }

    public void deleteAllNotes(String userID) {
        new DeleteAllNoteAsyncTask(noteDao).execute(userID);
    }

    public LiveData<List<Note>> getAllNotes() {
        return allNotes;
    }

    private static class InsertNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private InsertNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.insertNote(notes[0]);
            return null;
        }
    }

    private static class DeleteNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private DeleteNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.deleteNote(String.valueOf(notes[0].getId()));
            return null;
        }
    }

    private static class UpdateNoteAsyncTask extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        private UpdateNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.updateNotes(notes[0].getTitle(),notes[0].getContent(),String.valueOf(notes[0].getId()));
            return null;
        }
    }

    private static class DeleteAllNoteAsyncTask extends AsyncTask<String, Void, Void> {

        private NoteDao noteDao;

        private DeleteAllNoteAsyncTask(NoteDao noteDao) {
            this.noteDao = noteDao;
        }

        @Override
        protected Void doInBackground(String ... userID) {
            noteDao.deleteAllNotes(userID[0]);
            return null;
        }
    }

}
