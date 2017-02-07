import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Created by daksenik on 13.11.2016.
 */

class FileRepresentation{
    String filePath = "";
    int fileSize = 0;
    String name;
    public FileRepresentation(String fileInfo){
        fileSize = Integer.parseInt(fileInfo.substring(0,fileInfo.indexOf('\\')));
        filePath = fileInfo.substring(fileInfo.indexOf('\\')+1);
        int idx;
        while((idx = fileInfo.indexOf('\\')) != -1)fileInfo = fileInfo.substring(idx+1);
        name = fileInfo;
    }

    @Override
    public String toString(){
        String size = Integer.toString(fileSize);
        while(size.length() < 10)size = " "+size;
        return "[" + size + " B] " + name;
    }
}

public class MainWindow {
    public static void main(String[] args) {

        new UserWindow();
    }
}

class UserWindow extends JFrame {
    JFrame frame = this;
    ArrayList<FileRepresentation>dirTree = new ArrayList<>();
    public UserWindow() {
        super();

        getDirectoryTree();

        setLayout(new BorderLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,600);
        addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {

            }

            @Override
            public void windowClosing(WindowEvent e) {
                System.exit(0);
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

        DefaultListModel<FileRepresentation>lm = new DefaultListModel<>();
        for(FileRepresentation fr : dirTree)lm.addElement(fr);
        JList<FileRepresentation>list = new JList<>(lm);
        add(list,BorderLayout.CENTER);

        JPanel jp = new JPanel();
        JButton downloadBut = new JButton("Download");
        downloadBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int[] selected = list.getSelectedIndices();
                for(int i : selected)new BeginDownload(dirTree.get(i),frame).start();
            }
        });
        jp.add(downloadBut);
        JButton clearBut = new JButton("Clear");
        clearBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                list.clearSelection();
            }
        });
        jp.add(clearBut);
        add(jp,BorderLayout.SOUTH);
        setVisible(true);
    }

    void getDirectoryTree(){
        try {
            Socket treeConn = new Socket("localhost", 8000);
            new ObjectOutputStream(treeConn.getOutputStream()).writeObject("\\GETDIRTREE\\");
            while(treeConn.getInputStream().available() <= 0)Thread.sleep(1);
            ArrayList<String>ar = (ArrayList<String>)(new ObjectInputStream(treeConn.getInputStream()).readObject());
            for(String s : ar)dirTree.add(new FileRepresentation(s));
            treeConn.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(this,"ERROR: Couldn't connect to the server.");
            System.exit(0);
        }
    }
}

class BeginDownload extends Thread{
    FileRepresentation fr;
    File file;
    JFileChooser jfc = new JFileChooser("D:\\Programming\\BSU FAMCS\\Спецкурс\\13112016SocketsTask3\\Temp");
    public BeginDownload(FileRepresentation fileRepresentation, JFrame jFr){
        super();
        fr = fileRepresentation;
        jfc.setDialogTitle(fr.name);
        if(jfc.showSaveDialog(jFr.getParent()) == JFileChooser.APPROVE_OPTION) file = jfc.getSelectedFile();
    }

    @Override
    public void run(){
        if(file == null)return;
        LoadProgress lp = new LoadProgress(fr.name);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            Socket sck = new Socket("localhost", 8000);
            new ObjectOutputStream(sck.getOutputStream()).writeObject(fr.filePath);
            InputStream in = sck.getInputStream();
            byte[]bytes = new byte[1000];
            int len = 0;
            int comm = 0;
            while(true) {
                if (sck.getInputStream().available() <= 0) Thread.sleep(10);
                len = in.read(bytes);
                comm += len;
                lp.setLoadPercent(comm*100/fr.fileSize);
                if(len == -1 || !lp.isVisible()){
                    lp.setVisible(false);
                    lp.setLoadPercent(100);
                    fos.close();
                    break;
                }
                for(int i=0;i<len;i++)fos.write(bytes[i]);
            }
            sck.close();
        }catch(Exception e){
            JOptionPane.showMessageDialog(lp,"ERROR: Couldn't download the file:\n"+e);
        }finally{
            lp.setVisible(false);
        }
    }
}

class LoadProgress extends JFrame{
    String title;
    JProgressBar pb = new JProgressBar(0,500);
    public LoadProgress(String t){
        super();
        title = t;
        setTitle(title);
        setSize(500,60);
        pb.setValue(0);
        pb.setStringPainted(true);
        add(pb);
        setVisible(true);
    }

    void setLoadPercent(int pr){
        setTitle(title + " [" + pr + "/100]");
        pb.setValue(pr*5);
    }
}