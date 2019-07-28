package iti.jets.mad.noteapp.viewmodel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import iti.jets.mad.noteapp.model.Note;
import iti.jets.mad.noteapp.model.NoteRepository;

public class NoteViewModel extends AndroidViewModel {
    private NoteRepository noteRepository;
    private LiveData<List<Note>> allNotesLiveData;
    public NoteViewModel(@NonNull Application application) {
        super(application);
        noteRepository = new NoteRepository(application);
        allNotesLiveData=noteRepository.getAllNotes();
    }
    public void insert(Note note)
    {
        noteRepository.insert(note);
    }
    public void update(Note note)
    {
        noteRepository.update(note);
    }
    public void delete(Note note)
    {
        noteRepository.delete(note);
    }
    public void deleteall(String userID)
    {
        noteRepository.deleteAllNotes(userID);
    }

    public LiveData<List<Note>> getAllNotesLiveData() {
        return allNotesLiveData;
    }
}
