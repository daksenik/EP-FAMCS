/**
 * Created by daksenik on 11.09.2016.
 */
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;

public class TextEditor extends JFrame{
    File editingFile = null;
    TextArea text;
    JScrollPane scrollText;
    MenuBar menuBar;
    Menu file;
    MenuItem open, save, exit;
    JFileChooser fileChooser;
    String firstContent = "";



    private final static String NO_SPACES_BEFORE = " ,.;:?!)»\n";
    private final static String NO_SPACES_AFTER = " «(\n";
    public TextEditor(){
        super();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);

        setTitle("Text editor");
        setLayout(new BorderLayout());

        text = new TextArea();
        text.setEditable(true);
        scrollText = new JScrollPane(text);

        menuBar = new MenuBar();
        file = new Menu("File");
        open = new MenuItem("Open");
        save = new MenuItem("Save");
        exit = new MenuItem("Exit");

        fileChooser = new JFileChooser();
        fileChooser.setMultiSelectionEnabled(false);

        open.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openMenu();
            }
        });
        save.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveMenu();
            }
        });
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                closeWindow();
            }
        });

        file.add(open);
        file.add(save);
        file.addSeparator();
        file.add(exit);

        menuBar.add(file);


        setMenuBar(menuBar);

        JButton correctTextBut = new JButton("Correct text");
        correctTextBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                text.setText(correctText(text.getText()));
            }
        });

        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent event) {
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

        add(scrollText, BorderLayout.CENTER);
        JPanel jp = new JPanel();
        jp.setLayout(new FlowLayout());
        jp.add(correctTextBut);
        add(jp,BorderLayout.SOUTH);
        setSize(600,300);
        setVisible(true);
    }

    void closeWindow(){
        if(fileChanged()){
            int n = JOptionPane.showConfirmDialog(null, "File was changed. Do you want to save the file?");
            if(n == JOptionPane.YES_OPTION){
                if(!saveMenu())return;
            }else if(n == JOptionPane.CANCEL_OPTION){
                return;
            }
        }
        setVisible(false);
        System.exit(0);
    }

    boolean saveMenu(){
        if(fileChooser.showSaveDialog(getParent()) == JFileChooser.APPROVE_OPTION){
            editingFile = fileChooser.getSelectedFile();
            writeFile();
            return editingFile != null;
        }
        return false;
    }

    void openMenu(){
        if(fileChooser.showOpenDialog(getParent()) == JFileChooser.APPROVE_OPTION){
            editingFile = fileChooser.getSelectedFile();
            text.setText(readFile());
        }
    }

    void writeFile(){
        if(editingFile == null)return;
        try{
            PrintWriter pw = new PrintWriter(new FileWriter(editingFile));
            String temp = text.getText();
            char c;
            int idx = 0;
            firstContent = text.getText();
            while(idx != temp.length()){
                c = temp.charAt(idx++);
                if(c != '\n')pw.print(c);else pw.println();
            }
            pw.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error writing file:\n" + e.getMessage());
        }
    }

    String readFile(){
        if(editingFile == null)return "";
        StringBuilder sb = new StringBuilder();
        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(editingFile)));
            String temp;
            while ((temp = in.readLine()) != null) {
                if(sb.length() != 0)sb.append('\n');
                sb.append(temp);
            }
            firstContent = sb.toString();
            in.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Error reading file:\n" + e.getMessage());
            return "";
        }
        return sb.toString();
    }

    static String correctText(String src){
        StringBuilder sb = new StringBuilder(src);
        sb.append(' ');
        sb.insert(0,' ');
        boolean quotesOpened = false;
        int idx = 1;
        while(idx < sb.length()-1){
            if(sb.charAt(idx) == ' ' &&
               (NO_SPACES_BEFORE.indexOf(sb.charAt(idx+1)) != -1 ||
                       (quotesOpened && sb.charAt(idx+1) == '\"') ||
                       NO_SPACES_AFTER.indexOf(sb.charAt(idx-1)) != -1 ||
                       (quotesOpened && sb.charAt(idx-1) == '\"')

               )){
                sb.deleteCharAt(idx);
                continue;
            }
            if((NO_SPACES_BEFORE.indexOf(sb.charAt(idx)) > 0 && NO_SPACES_BEFORE.indexOf(sb.charAt(idx+1)) == -1) ||
                    sb.charAt(idx+1) == '(' && NO_SPACES_BEFORE.indexOf(sb.charAt(idx)) == -1)
                sb.insert(idx+1,' ');
            idx++;
        }
        sb.deleteCharAt(0);
        sb.deleteCharAt(sb.length()-1);
        return sb.toString();
    }

    boolean fileChanged(){
        return !(firstContent.equals(text.getText()));
    }

    public static void main(String[] args) {
        new TextEditor();
    }
}
