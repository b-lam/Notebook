package b_lam.github.io.notebook;

import com.orm.SugarRecord;

/**
 * Created by Brandon on 5/19/2016.
 */
public class Note extends SugarRecord{
    String title, content, date;

    public Note(){

    }

    public Note(String title, String content, String date){
        this.title = title;
        this.content = content;
        this.date = date;
    }

    public String getTitle(){
        return title;
    }

    public String getContent(){
        return content;
    }

    public String getDate(){
        return date;
    }

    public void setTitle(String title){
        this.title = title;
    }

    public void setContent(String content){
        this.content = content;
    }

    public void setDate(String date){
        this.date = date;
    }

}
