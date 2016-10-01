package b_lam.github.io.notebook;

import com.orm.SugarRecord;

import java.util.ArrayList;

/**
 * Created by Brandon on 5/19/2016.
 */
public class Note extends SugarRecord{
    private String mTitle;
    private String mContent;
    private String mDate;
    private int mPage;

    public Note(){

    }

    public Note(String title, String content, String date){
        mTitle = title;
        mContent = content;
        mDate = date;
    }

    public String getTitle(){
        return mTitle;
    }

    public String getContent(){
        return mContent;
    }

    public String getDate(){
        return mDate;
    }

    public int getPage(){
        return mPage;
    }

    public static ArrayList<Note> createNewNoteList(int numNotes){
        ArrayList<Note> notes = new ArrayList<Note>();

        for(int i = 1; i <= numNotes; i++){
            notes.add(new Note("", "", ""));
        }

        return notes;
    }

    public void setTitle(String title){
        mTitle = title;
    }

    public void setContent(String content){
        mContent = content;
    }
}
