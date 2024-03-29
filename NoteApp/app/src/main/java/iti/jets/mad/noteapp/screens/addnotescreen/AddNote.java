package iti.jets.mad.noteapp.screens.screens.addnotescreen;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import iti.jets.mad.noteapp.R;

public class AddNote extends AppCompatActivity {

    private EditText titleEditText, contentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);
        titleEditText = findViewById(R.id.titleEditText);
        contentEditText = findViewById(R.id.contentEditText);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_close_black);

        Intent intent=getIntent();
        if(intent.hasExtra("note_id"))
        {
            setTitle(R.string.EditNote);
            titleEditText.setText(intent.getStringExtra("note_title"));
            contentEditText.setText(intent.getStringExtra("note_content"));
        }
        else {
            setTitle(R.string.AddNote);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_note, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_menu_item:
                addNote();
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }


    }
    private void addNote()
    {
        String title=titleEditText.getText().toString();
        String content=contentEditText.getText().toString();
        if(title.isEmpty() || content.isEmpty()){

        Toast.makeText(this, getString(R.string.titleContent), Toast.LENGTH_SHORT).show();
        return;
    }
        Intent intent=new Intent();
        int id = getIntent().getIntExtra("note_id",-1);
        intent.putExtra("note_title",title);
        intent.putExtra("note_content",content);
        if(id!=-1)
        {
            intent.putExtra("note_id",id);
        }
        setResult(RESULT_OK,intent);
        finish();

    }
}
