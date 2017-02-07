import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by daksenik on 24.09.2016.
 */
public class Student implements Serializable {
    private String surname;
    private int groupNumber;
    private ArrayList<Integer>marks = new ArrayList<>();

    public Student(String sname,int group,String ms){
        surname = sname;
        groupNumber = group;
        String[]mrks = ms.split(" ");
        for(int i=0;i<mrks.length;i++)marks.add(Integer.parseInt(mrks[i]));
    }

    public void addMark(int mark){
        marks.add(mark);
    }

    public String getSurname(){ return surname; }
    public int getGroup(){ return groupNumber; }

    public final int getMark(int id){
        if(id < 0 || id >= marks.size())return -1;
            else return marks.get(id);
    }

    public final ArrayList<Integer> getMarks(){
        return marks;
    }
}
