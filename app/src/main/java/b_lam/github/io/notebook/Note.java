package b_lam.github.io.notebook;

import java.util.ArrayList;

/**
 * Created by Brandon on 5/19/2016.
 */
public class Note {
    private String mTitle;
    private String mContent;
    private int mPage;

    public Note(String title, String content, int page){
        mTitle = title;
        mContent = content;
        mPage = page;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getContent(){
        return mContent;
    }

    public int getPage(){
        return mPage;
    }

    public static ArrayList<Note> createNewNoteList(int numNotes){
        ArrayList<Note> notes = new ArrayList<Note>();

        for(int i = 1; i <= numNotes; i++){
            notes.add(new Note("", "", 1));
        }

        return notes;
    }
}
