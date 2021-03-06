package b_lam.github.io.notebook;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import Helper.ItemTouchHelperAdapter;


/**
 * Created by Brandon on 9/8/2016.
 */

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> implements ItemTouchHelperAdapter {

    OnItemClickListener clickListener;

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        public TextView tvTitle;
        public TextView tvContent;
        public TextView tvDate;

        public ViewHolder(final View itemView){
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.note_title);
            tvContent = (TextView) itemView.findViewById(R.id.note_content);
            tvDate = (TextView) itemView.findViewById(R.id.note_date);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    private static List<Note> mNotes;
    private Context mContext;
    private List<Note> mNotesToDelete = new ArrayList<>();


    public NoteAdapter(Context context, List<Note> note){
        mContext = context;
        mNotes = note;
    }

    public Context getContext(){
        return mContext;
    }

    @Override
    public NoteAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_note, parent, false);
        contactView.setPaddingRelative(0,0,0,20);

        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(NoteAdapter.ViewHolder viewHolder, int position){
        Note note = mNotes.get(position);

        TextView tvTitle = viewHolder.tvTitle;
        TextView tvContent = viewHolder.tvContent;
        TextView tvPage = viewHolder.tvDate;

        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());
        tvPage.setText(note.getDate());
    }

    @Override
    public int getItemCount(){
        return mNotes.size();
    }

    @Override
    public void onItemDismiss(final int position, final RecyclerView recyclerView){
        final Note mNote = mNotes.get(position);

        Snackbar.make(recyclerView,"Note deleted", Snackbar.LENGTH_LONG).setAction("UNDO", new View.OnClickListener(){
            @Override
            public void onClick(View view){
                mNotes.add(position, mNote);
                notifyItemInserted(position);
                recyclerView.scrollToPosition(position);
                mNote.save();
                mNotesToDelete.remove(mNote);
                Notebook.initialCount += 1;
            }
        }).show();

        mNotes.remove(position);
        notifyItemRemoved(position);
        mNotesToDelete.add(mNote);
        mNote.delete();
        Notebook.initialCount -= 1;
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition){
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(mNotes, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(mNotes, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener){
        this.clickListener = itemClickListener;
    }
}
