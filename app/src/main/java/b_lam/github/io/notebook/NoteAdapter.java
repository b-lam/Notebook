package b_lam.github.io.notebook;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.List;


/**
 * Created by Brandon on 9/8/2016.
 */
public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.ViewHolder> {

    public static class ViewHolder extends RecyclerView.ViewHolder{
        public TextView tvTitle;
        public TextView tvContent;

        public ViewHolder(View itemView){
            super(itemView);

            tvTitle = (TextView) itemView.findViewById(R.id.note_title);
            tvContent = (TextView) itemView.findViewById(R.id.note_content);
        }
    }

    private List<Note> mNote;
    private Context mContext;

    public NoteAdapter(Context context, List<Note> note){
        mContext = context;
        mNote = note;
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
        Note note = mNote.get(position);

        TextView tvTitle = viewHolder.tvTitle;
        TextView tvContent = viewHolder.tvContent;

        tvTitle.setText(note.getTitle());
        tvContent.setText(note.getContent());
    }

    @Override
    public int getItemCount(){
        return mNote.size();
    }
}
