import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created by daksenik on 24.09.2016.
 */
public class ControlPanel extends JFrame {
    File inputFile = null;
    JFileChooser fileChooser = new JFileChooser();
    boolean fileEdited = false;
    JTextField surname,group,marks;
    JButton filterStudents, clearFilter, addStudent;
    JTable table;
    DefaultTableModel model;
    ObjectInputStream is;
    ObjectOutputStream os;
    ArrayList<Student> students;
    MenuBar menuBar = new MenuBar();
    Menu file = new Menu("File");
    MenuItem open = new MenuItem("Open...");
    MenuItem save = new MenuItem("Save...");
    MenuItem exit = new MenuItem("Exit");

    public ControlPanel(){
        super();

        menuBar.add(file);
        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(exit);
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });
        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveFile();
            }
        });

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
                addStudentToList(surname.getText(),Integer.parseInt(group.getText()),marks.getText(),true);
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

        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                closeWindow();
            }

            @Override
            public void windowClosed(WindowEvent e) {

            }

            @Override
            public void windowIconified(WindowEvent e) {

            }

            @Override
            public void windowDeiconified(WindowEvent e) {

            }

            @Override
            public void windowActivated(WindowEvent e) {

            }

            @Override
            public void windowDeactivated(WindowEvent e) {

            }
        });

        setTitle("Students list");
        setLayout(new BorderLayout());
        setMenuBar(menuBar);

        add(addStudentPanel,BorderLayout.NORTH);

        JPanel jp = new JPanel(new FlowLayout());
        jp.add(filterStudents);
        jp.add(clearFilter);
        add(jp,BorderLayout.SOUTH);
        add(scrollTable,BorderLayout.CENTER);

        setSize(600,300);
        setVisible(true);
    }

    void addStudentToList(String studentSurname,int groupNumber,String studentMarks,boolean addToArray){
        fileEdited = fileEdited || addToArray;
        Object[]newRow = {studentSurname, groupNumber, studentMarks};
        model.addRow(newRow);
        if(addToArray)students.add(new Student(studentSurname,groupNumber,studentMarks));
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
            addStudentToList(temp.getSurname(),temp.getGroup(),marksString.toString(),false);
        }
    }

    void restoreList(){
        int rowCount = model.getRowCount();
        for(int i=rowCount-1;i>=0;i--)model.removeRow(i);
        for(int i=0;i<students.size();i++){
            StringBuilder mks = new StringBuilder("");
            for(int j=0;j<students.get(i).getMarks().size();j++)mks.append(students.get(i).getMark(j)+" ");
            addStudentToList(students.get(i).getSurname(),students.get(i).getGroup(),mks.toString(),false);
        }
    }

    void openFile(){
        fileEdited = false;
        if(fileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION){
            inputFile = fileChooser.getSelectedFile();
            if(inputFile == null)return;
            try{
                is = new ObjectInputStream(new FileInputStream(inputFile));
                while(true){
                    students.add((Student)is.readObject());
                }
            }catch(Exception e){
                if(e instanceof EOFException){
                    restoreList();
                    return;
                }
                JOptionPane.showMessageDialog(null, "Error reading file:\n" + e.getMessage());
            }
        }
    }
    boolean saveFile(){
        if(fileChooser.showSaveDialog(getParent()) == JFileChooser.APPROVE_OPTION){
            inputFile = fileChooser.getSelectedFile();
            if(inputFile == null)return false;

            try{
                os = new ObjectOutputStream(new FileOutputStream(inputFile));
                for(int i=0;i<students.size();i++)os.writeObject(students.get(i));
                os.close();
            }catch(Exception e){
                JOptionPane.showMessageDialog(null, "Error writing file:\n" + e.getMessage());
                return false;
            }

            fileEdited = false;
            return true;
        }
        return false;
    }

    void closeWindow(){
        if(fileEdited){
            int n = JOptionPane.showConfirmDialog(null, "File was changed. Do you want to save the file?");
            if(n == JOptionPane.YES_OPTION){
                if(!saveFile())return;
            }else if(n == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        setVisible(false);
        System.exit(0);
    }

    public static void main(String[] args) {
        new ControlPanel();
    }
}
