package b_lam.github.io.notebook;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.orm.SugarRecord;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import Helper.SimpleItemTouchHelperCallback;
import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class Notebook extends AppCompatActivity {

    List<Note> notes = new ArrayList<>();
    RecyclerView recyclerView;
    NoteAdapter adapter;
    LinearLayoutManager linearLayoutManager;
    static long initialCount;
    static int modifyPos = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null)
            modifyPos = savedInstanceState.getInt("modify");

        setContentView(R.layout.activity_notebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rvNotes);

        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        initialCount = Note.count(Note.class);

        if(initialCount >= 0){

            notes = Note.listAll(Note.class);

            adapter = new NoteAdapter(Notebook.this, notes);
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new NoteAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {

                    Intent i = new Intent(Notebook.this, EditActivity.class);
                    i.putExtra("isEditing", true);
                    i.putExtra("note_title", notes.get(position).title);
                    i.putExtra("note_content", notes.get(position).content);
                    i.putExtra("note_date", notes.get(position).date);

                    modifyPos = position;

                    startActivity(i);
                }
            });
        }

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter, recyclerView);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditActivity.class));

            }
        });



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_notebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_go_to) {
            if(notes.size() > 0){
                gotoDialog();
            }else{
                noNotesDialog();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoDialog(){

        final MaterialNumberPicker numberPicker = new MaterialNumberPicker.Builder(this)
                .minValue(1)
                .maxValue(notes.size())
                .defaultValue(1)
                .backgroundColor(Color.WHITE)
                .separatorColor(Color.TRANSPARENT)
                .textColor(Color.BLACK)
                .textSize(20)
                .enableFocusability(false)
                .wrapSelectorWheel(true)
                .build();

        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Choose Page")
                .setView(numberPicker)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Snackbar.make(findViewById(R.id.coordLayout), "You picked page : " + numberPicker.getValue(), Snackbar.LENGTH_LONG).show();
                        recyclerView.smoothScrollToPosition(numberPicker.getValue()-1);
                    }
                })
                .show();

    }

    private void noNotesDialog(){
        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("No Pages Found")
                .setMessage("Try creating some notes using the plus button.")
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){
        super.onSaveInstanceState(bundle);

        bundle.putInt("modify", modifyPos);
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle){
        super.onRestoreInstanceState(bundle);

        modifyPos = bundle.getInt("modify");
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onResume(){
        super.onResume();

        final long newCount = Note.count(Note.class);

        if (newCount > initialCount) {
            // A note is added
            // Just load the last added note (new)
            Note note = Note.last(Note.class);
            notes.add(note);
            adapter.notifyItemInserted((int) newCount);
            initialCount = newCount;

            final Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    recyclerView.smoothScrollToPosition(notes.size()-1);
                }
            }, 200);
        }

        if (modifyPos != -1 && notes.size() != 0) {
            notes.set(modifyPos, Note.listAll(Note.class).get(modifyPos));
            adapter.notifyItemChanged(modifyPos);
        }
    }
}
