package b_lam.github.io.notebook;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import Helper.SimpleItemTouchHelperCallback;
import biz.kasual.materialnumberpicker.MaterialNumberPicker;

public class Notebook extends AppCompatActivity {

    ArrayList<Note> notes;
    RecyclerView recyclerView;
    NoteAdapter adapter;
    LinearLayoutManager linearLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            System.out.print("I'm back");
        }else{
            notes = Note.createNewNoteList(0);
        }

        setContentView(R.layout.activity_notebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rvNotes);


        adapter = new NoteAdapter(this, notes);
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(linearLayoutManager);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);

        ItemTouchHelper.Callback callback = new SimpleItemTouchHelperCallback(adapter, recyclerView);
        ItemTouchHelper touchHelper = new ItemTouchHelper(callback);
        touchHelper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date cDate = new Date();
                String fDate = new SimpleDateFormat("yyyy/MM/dd").format(cDate);
                Log.d("Current Date", fDate);
                notes.add(new Note("", "", fDate));
                adapter.notifyItemInserted(notes.size()-1);
                recyclerView.smoothScrollToPosition(notes.size()-1);

            }
        });



        //TODO Add saving
        //TODO Go to page by title (Scroll picker)
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
                .formatter(new NumberPicker.Formatter() {
                    @Override
                    public String format(int i) {
                        if(notes.get(i-1).getTitle().length() > 0){
                            return notes.get(i).getTitle();
                        }else{
                            return "Date: " + notes.get(i-1).getDate();
                        }
                    }
                })
                .build();

        new AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Choose Page")
                .setView(numberPicker)
                .setPositiveButton("Go", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        Snackbar.make(findViewById(R.id.coordLayout), "You picked : " + numberPicker.getValue(), Snackbar.LENGTH_LONG).show();
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
    }

    @Override
    public void onRestoreInstanceState(Bundle bundle){
        super.onRestoreInstanceState(bundle);
        adapter.notifyDataSetChanged();
    }
}
