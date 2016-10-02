package b_lam.github.io.notebook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class EditActivity extends AppCompatActivity {

    EditText etTitle, etContent;
    boolean editingNote;
    String title, content, date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        this.getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white_24dp);
        this.getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        etTitle = (EditText) findViewById(R.id.etTitle);
        etContent = (EditText) findViewById(R.id.etContent);

        //Get data from intent
        editingNote = getIntent().getBooleanExtra("isEditing", false);
        if(editingNote){
            title = getIntent().getStringExtra("note_title");
            content = getIntent().getStringExtra("note_content");
            date = getIntent().getStringExtra("note_date");

            etTitle.setText(title);
            etContent.setText(content);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String newTitle = etTitle.getText().toString();
                String newContent = etContent.getText().toString();
                String newDate = getDate();

                if (!editingNote) {
                    Log.d("Note", "Saving");
                    Note note = new Note(newTitle, newContent, newDate);
                    note.save();
                } else {
                    Log.d("Note", "Updating");

                    List<Note> notes = Note.find(Note.class, "title = ?", title);
                    if (notes.size() > 0) {

                        Note note = notes.get(0);
                        Log.d("Got Note", "Note: " + note.title);
                        note.title = newTitle;
                        note.content = newContent;
                        note.date = newDate;

                        note.save();
                    }

                }

                finish();
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @SuppressLint("SimpleDateFormat")
    public String getDate(){
        return new SimpleDateFormat("yyyy/MM/dd").format(new Date());
    }

}
