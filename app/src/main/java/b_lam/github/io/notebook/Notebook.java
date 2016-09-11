package b_lam.github.io.notebook;

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
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import Helper.SimpleItemTouchHelperCallback;

public class Notebook extends AppCompatActivity {

    ArrayList<Note> notes;
    AlertDialog dialogBuilder;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){

        }

        setContentView(R.layout.activity_notebook);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = (RecyclerView) findViewById(R.id.rvNotes);

        notes = Note.createNewNoteList(0);

        final NoteAdapter adapter = new NoteAdapter(this, notes);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
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
                notes.add(new Note("", "", notes.size()+1));
                adapter.notifyItemInserted(notes.size()-1);
                recyclerView.smoothScrollToPosition(notes.size()-1);

            }
        });

        //TODO Add saving
        //TODO Add updating page numbers
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
            gotoDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void gotoDialog(){
        LayoutInflater layoutInflater = LayoutInflater.from(this);
        View gotoView = layoutInflater.inflate(R.layout.go_to_dialog_layout, null);

        dialogBuilder = new AlertDialog.Builder(this).create();
        dialogBuilder.setTitle("Enter Page Number");
        dialogBuilder.setIcon(R.mipmap.ic_launcher);
        dialogBuilder.setView(gotoView);

        final EditText pageNumber = (EditText) gotoView.findViewById(R.id.editTextPageNumber);

        Button btnGo = (Button) gotoView.findViewById(R.id.go);
        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int goToPageNumber = 0;
                if(pageNumber.getText().length() > 0){
                    goToPageNumber = Integer.parseInt(pageNumber.getText().toString());
                }
                if(goToPageNumber <= notes.size() && goToPageNumber > 0){
                    recyclerView.smoothScrollToPosition(goToPageNumber-1);
                }else{
                    Toast.makeText(getApplicationContext(), "Please enter a valid page number", Toast.LENGTH_SHORT).show();
                }
                dialogBuilder.dismiss();
            }
        });

        dialogBuilder.show();
    }

    @Override
    public void onSaveInstanceState(Bundle bundle){

    }
}
