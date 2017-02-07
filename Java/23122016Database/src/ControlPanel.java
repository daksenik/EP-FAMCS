import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.io.*;
import java.util.ArrayList;

import com.mysql.fabric.jdbc.FabricMySQLDriver;

/**
 * Created by daksenik on 24.09.2016.
 */
public class ControlPanel extends JFrame {
    File inputFile = null;
    JFileChooser fileChooser = new JFileChooser();
    JTextField surname,group,marks;
    JButton filterStudents, clearFilter, addStudent;
    JTable table;
    DefaultTableModel model;
    ObjectInputStream is;
    ObjectOutputStream os;
    ArrayList<Student> students;

    static final String DB_URL = "jdbc:mysql://localhost:3306/studentsdb";
    static final String SELECT = "select * from students";
    static private final String USERNAME = "root";
    static private final String PASSWORD = "12345";
    Connection connection;
    Driver driver;
    
    public ControlPanel(){
        super();
        
        //
        try {
            driver = new FabricMySQLDriver();
            DriverManager.registerDriver(driver);
            connection = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
        
        //
        
        students = new ArrayList<>();

        surname = new JTextField("Surname");
        surname.setColumns(10);
        surname.setToolTipText("Student surname");

        group = new JTextField("Group");
        group.setColumns(5);
        group.setToolTipText("Group number");

        marks = new JTextField("Student marks");
        marks.setColumns(15);
        marks.setToolTipText("Student marks");

        addStudent = new JButton("Add student");
        addStudent.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                addStudentToList(surname.getText(),Integer.parseInt(group.getText()),marks.getText(),true,true);
            }
        });

        filterStudents = new JButton("Filter students");
        filterStudents.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filterStudentsList();
            }
        });

        clearFilter = new JButton("Clear filtering");
        clearFilter.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                restoreList();
            }
        });

        JPanel addStudentPanel = new JPanel(new FlowLayout());
        addStudentPanel.add(surname);
        addStudentPanel.add(group);
        addStudentPanel.add(marks);
        addStudentPanel.add(addStudent);

        Object[]columnNames = {"Surname","Group","Marks"};
        model = new DefaultTableModel(columnNames,0);
        table = new JTable(model);
        JScrollPane scrollTable = new JScrollPane(table);
        table.setFillsViewportHeight(true);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setTitle("Students list");
        setLayout(new BorderLayout());

        add(addStudentPanel,BorderLayout.NORTH);

        JPanel jp = new JPanel(new FlowLayout());
        jp.add(filterStudents);
        jp.add(clearFilter);
        add(jp,BorderLayout.SOUTH);
        add(scrollTable,BorderLayout.CENTER);

        setSize(600,300);
        loadStudents();
        setVisible(true);
    }

    void addStudentToList(String studentSurname,int groupNumber,String studentMarks,boolean addToArray,boolean insertToDB){
        Object[]newRow = {studentSurname, groupNumber, studentMarks};
        model.addRow(newRow);
        if(addToArray) students.add(new Student(studentSurname,groupNumber,studentMarks));
        if(insertToDB){
            String query = "insert into students values ('" + studentSurname + "'," + groupNumber + ",'" + studentMarks + "')";
            try {
                Statement statement = connection.createStatement();
                statement.execute(query);
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage());
            }
        }
    }

    void filterStudentsList(){
        Object[]result = students.stream().filter(a->(a.getMarks().stream().filter(b->(b<4)).count()>0)).
                sorted((a,b)->a.getSurname().compareTo(b.getSurname())).toArray();
        int rowCount = model.getRowCount();
        for(int i=rowCount-1;i>=0;i--)model.removeRow(i);
        for(int i=0;i<result.length;i++){
            Student temp = (Student)result[i];
            StringBuilder marksString = new StringBuilder("");
            int marksCount = temp.getMarks().size();
            for(int j=0;j<marksCount;j++)marksString.append(temp.getMark(j)+" ");
            addStudentToList(temp.getSurname(),temp.getGroup(),marksString.toString(),false,false);
        }
    }

    void restoreList(){
        int rowCount = model.getRowCount();
        for(int i=rowCount-1;i>=0;i--)model.removeRow(i);
        for(int i=0;i<students.size();i++){
            StringBuilder mks = new StringBuilder("");
            for(int j=0;j<students.get(i).getMarks().size();j++)mks.append(students.get(i).getMark(j)+" ");
            addStudentToList(students.get(i).getSurname(),students.get(i).getGroup(),mks.toString(),false,false);
        }
    }
    
    void loadStudents(){
        //read students
        try{
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery(SELECT);
            StringBuilder marks = new StringBuilder("");
            while(result.next()){
                Student temp = new Student(result.getString("studentName"),
                                           result.getInt("groupNumber"),
                                           result.getString("marks"));
                for(int mark : temp.getMarks())marks.append(mark+" ");
                addStudentToList(temp.getSurname(),temp.getGroup(),marks.toString(),true,false);
                marks.delete(0,marks.length());
            }
            
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,e.getMessage());
        }
        
    }

    public static void main(String[] args) {
        new ControlPanel();
    }
}
