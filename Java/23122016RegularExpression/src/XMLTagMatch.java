import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.io.File;
import java.io.FileReader;
import java.util.Scanner;

/**
 * Created by user on 23.12.2016.
 */
public class XMLTagMatch extends JFrame {
    
    JTextArea xmlText = new JTextArea();
    JTextArea selectedXML = new JTextArea();
    JButton openFileBut = new JButton("Open");
    JButton findClosingTagBut = new JButton("Find");
    String currentText = "";
    int start = -1;
    
    public XMLTagMatch(){
        super();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setTitle("XMLTagMatch");
        setSize(500,500);
        
        xmlText.setEditable(false);
        selectedXML.setEditable(false);
        findClosingTagBut.setEnabled(false);
        
        findClosingTagBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                findClosingTag();
            }
        });
        
        xmlText.addCaretListener(new CaretListener() {
            @Override
            public void caretUpdate(CaretEvent e) {
                if(xmlText.getSelectedText() != null && isInsideOpenTag()) {
                    findClosingTagBut.setEnabled(true);
                    start = xmlText.getSelectionStart();
                } else findClosingTagBut.setEnabled(false);
            }
        });
        
        openFileBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openFile();
            }
        });
        
        JPanel temp = new JPanel(new GridLayout(1,2));
        temp.add(xmlText);
        temp.add(selectedXML);
        selectedXML.setBackground(new Color(206,206,206));
        add(temp,BorderLayout.CENTER);
        temp = new JPanel();
        temp.add(openFileBut);
        add(temp, BorderLayout.NORTH);
        temp = new JPanel();
        temp.add(findClosingTagBut);
        add(temp, BorderLayout.SOUTH);
        setVisible(true);
    }
    
    void openFile(){
        JFileChooser jfc = new JFileChooser("D:\\Programming\\BSU FAMCS\\Спецкурс\\23122016RegularExpression");
        if(jfc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION){
            File file = jfc.getSelectedFile();
            if(file == null)return;
            
            StringBuilder sb = new StringBuilder("");
            try{
                Scanner input = new Scanner(new FileReader(file));

                boolean firstRow = true;
                while(input.hasNext()){
                    if(!firstRow)sb.append('\n');
                    sb.append(input.nextLine());
                    firstRow = false;
                }
                currentText = sb.toString();
                xmlText.setText(currentText);
                xmlText.setVisible(true);
                input.close();
            }catch(Exception e){
                JOptionPane.showMessageDialog(this,e.getMessage());
            }
        }
    }
    
    boolean isInsideOpenTag(){
        int startTemp = xmlText.getSelectionStart();
        boolean found = false;
        for(int i=startTemp;i>=0;i--){
            if(currentText.charAt(i) == '<'){
                found = true;
                break;
            }else if(currentText.charAt(i) == '>' || currentText.charAt(i) == '/')return false;
        }
        if(!found)return false;
        for(int i=startTemp;i<currentText.length();i++){
            if(currentText.charAt(i) == '>')return true;
            if(currentText.charAt(i) == '<' || currentText.charAt(i) == '/')return false;
        }
        return false;
    }
    
    void findClosingTag(){
        if(start < 0)return;
        int depth = 1;
        for(int i=start+1;i<currentText.length();i++) if(currentText.charAt(i) == '<' && i < currentText.length()-1){
            if(currentText.charAt(i+1) == '/')depth--;else depth++;
            if(depth==0){
                int en = i,st = start;
                while(st > 0 && currentText.charAt(st) != '<')st--;
                while(currentText.charAt(en) != '>' && en < currentText.length()) en++;
                selectedXML.setText(currentText.substring(st,en+1));
                break;
            }
        }
    }
    
    public static void main(String[] args) {
        new XMLTagMatch();
    }
}
