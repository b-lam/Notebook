package b_lam.github.io.notebook;

import java.util.ArrayList;

/**
 * Created by Brandon on 5/19/2016.
 */
public class Note {
    private String mTitle;
    private String mContent;

    public Note(String title, String content){
        mTitle = title;
        mContent = content;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getContent(){
        return mContent;
    }

    public static ArrayList<Note> createNewNoteList(int numNotes){
        ArrayList<Note> notes = new ArrayList<Note>();

        for(int i = 1; i <= numNotes; i++){
            notes.add(new Note("", ""));
        }

        return notes;
    }
}
