package iti.jets.mad.noteapp.screens.screens.notelistscreen;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.facebook.login.LoginManager;

import java.util.List;

import iti.jets.mad.noteapp.R;
import iti.jets.mad.noteapp.model.Note;
import iti.jets.mad.noteapp.model.NoteAdapter;
import iti.jets.mad.noteapp.screens.screens.splashscreen.SplashScreenActivity;
import iti.jets.mad.noteapp.screens.screens.addnotescreen.AddNote;
import iti.jets.mad.noteapp.viewmodel.NoteViewModel;

public class NoteListActivity extends AppCompatActivity {
    public static final int ADD_NOTE_REQUEST = 1;
    public static final int EDIT_NOTE_REQUEST = 2;
    private SharedPreferences sharedpreferences;
    public static final String MY_PERFERENCES = "MyPerf";
    private SharedPreferences.Editor editor;
    private NoteViewModel noteViewModel;
    private RecyclerView recyclerView;
    private NoteAdapter noteAdapter;
    private FloatingActionButton floatingActionButton;
    private String userID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_list);
        floatingActionButton = findViewById(R.id.addNoteFB);
       // sharedpreferences = getSharedPreferences(MY_PERFERENCES, Context.MODE_PRIVATE);
        sharedpreferences= PreferenceManager.getDefaultSharedPreferences(this);
        userID=(sharedpreferences.getString("userID",null));
        editor = sharedpreferences.edit();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(NoteListActivity.this, AddNote.class), ADD_NOTE_REQUEST);


            }
        });
        recyclerView = findViewById(R.id.noteRecycleView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noteAdapter = new NoteAdapter();
        recyclerView.setAdapter(noteAdapter);
        noteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);
        noteViewModel.getAllNotesLiveData().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(@Nullable List<Note> notes) {
                // update recycle view
                noteAdapter.setAllNotes(notes);
            }
        });
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                noteViewModel.delete(noteAdapter.getSpecificNote(viewHolder.getAdapterPosition()));
                Toast.makeText(NoteListActivity.this, getString(R.string.DeletedToast), Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);
        noteAdapter.setOnItemClickListener(new NoteAdapter.onItemClickListener() {
            @Override
            public void onItemClick(Note note) {
                        Intent intent =new Intent(NoteListActivity.this,AddNote.class);
                        intent.putExtra("note_id",note.getId());
                        intent.putExtra("note_title",note.getTitle());
                        intent.putExtra("note_content",note.getContent());
                        startActivityForResult(intent,EDIT_NOTE_REQUEST);
            }
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_NOTE_REQUEST && resultCode == RESULT_OK) {
            String noteTitle = data.getStringExtra("note_title");
            String noteContent = data.getStringExtra("note_content");
            noteViewModel.insert(new Note(noteTitle, noteContent,sharedpreferences.getString("userID",null)));
            Toast.makeText(this, getString(R.string.NoteSavedSucessfullyToast), Toast.LENGTH_SHORT).show();
        }
        else if(requestCode == EDIT_NOTE_REQUEST && resultCode == RESULT_OK)
        {
            int id=data.getIntExtra("note_id",-1);
            if(id==-1)
            {
                Toast.makeText(this, getString(R.string.updateNoteFailToast), Toast.LENGTH_SHORT).show();
                return;
            }
            String noteTitle = data.getStringExtra("note_title");
            String noteContent = data.getStringExtra("note_content");
            Note note=new Note(noteTitle, noteContent,sharedpreferences.getString("userID",null));
            note.setId(id);
            noteViewModel.update(note);

            Toast.makeText(this, getString(R.string.updateNoteToast), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, getString(R.string.notenotSavedToast), Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.deleteall_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.deletes_notes_item:
                noteViewModel.deleteall(userID);
                Toast.makeText(this, getString(R.string.deleteNoteToast), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.logout_item:
                new AlertDialog.Builder(this)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle(R.string.logout)
                        .setMessage(R.string.logoutDialoug)
                        .setPositiveButton(R.string.yes, new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                LoginManager.getInstance().logOut();
                                editor.putString("userID","");
                                editor.commit();
                                String usrr= sharedpreferences.getString("userID",null);
                                Toast.makeText(NoteListActivity.this, getString(R.string.loggedoutToast), Toast.LENGTH_SHORT).show();
                                //Toast.makeText(NoteListActivity.this, sharedpreferences.getString("userID","no"), Toast.LENGTH_SHORT).show();
                                finish();
                            }

                        })
                        .setNegativeButton(R.string.no, null)
                        .show();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }

    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

            ConnectivityManager cm = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);

            NetworkInfo netInfo = cm.getActiveNetworkInfo();

            if (netInfo != null && netInfo.isConnectedOrConnecting()) {

                menu.findItem(R.id.logout_item).setEnabled(true);
                return true;

            } else {

                menu.findItem(R.id.logout_item).setEnabled(false);
                return true;

            }


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, SplashScreenActivity.class));
    }
}
