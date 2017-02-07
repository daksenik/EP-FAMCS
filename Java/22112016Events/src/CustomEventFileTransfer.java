import javax.swing.*;
import javax.swing.event.EventListenerList;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.EventListener;
import java.util.Random;

/**
 * Created by user on 11.12.2016.
 */
public class CustomEventFileTransfer extends JFrame {
    JFrame thisFrame;
    File copyFileSrc = null, copyFileTarget = null;
    EventGenerator generator;

    public CustomEventFileTransfer(){
        super();
        thisFrame = this;
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("CustomEvent file transfer");
        setSize(400,100);
        generator = new EventGenerator();
        generator.addTransferListener(new FileTransferListener());

        JPanel mainPanel = new JPanel(new FlowLayout());

        JButton downloadFile = new JButton("Copy file");

        downloadFile.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jfc = new JFileChooser("D:\\Programming\\BSU FAMCS\\Спецкурс\\22112016Events\\Temp");
                if (jfc.showOpenDialog(thisFrame) == JFileChooser.APPROVE_OPTION) copyFileSrc = jfc.getSelectedFile();
                if (jfc.showSaveDialog(thisFrame) == JFileChooser.APPROVE_OPTION) copyFileTarget = jfc.getSelectedFile();
                CopyThread copy = new CopyThread(copyFileSrc,copyFileTarget,generator);
            }
        });

        add(mainPanel);
        mainPanel.add(downloadFile);

        setVisible(true);
    }
    
    public static void main(String[] args) {
        new CustomEventFileTransfer();
    }
}

class EventGenerator extends JComponent{
    private EventListenerList listeners = new EventListenerList();
    
    public EventGenerator(){
        super();
    }
    
    void startEvent(){
        TransferEvent event = new TransferEvent(this,"Start");
        EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        queue.postEvent(event);
    }

    void finishEvent(){
        TransferEvent event = new TransferEvent(this,"Finish");
        EventQueue queue = Toolkit.getDefaultToolkit().getSystemEventQueue();
        queue.postEvent(event);
    }

    public void processEvent(AWTEvent event){
        if(event instanceof TransferEvent){
            if(((TransferEvent)event).message.equals("Start")){
                EventListener[] startList = listeners.getListeners(TransferListener.class);
                for(int i=0;i<startList.length;i++)((TransferListener)startList[i]).transferStarted((TransferEvent)event);
            }else{
                EventListener[] finishList = listeners.getListeners(TransferListener.class);
                for(int i=0;i<finishList.length;i++)((TransferListener)finishList[i]).transferFinished((TransferEvent)event);
            }
        }else{
            super.processEvent(event);
        }
    }

    public void addTransferListener(TransferListener l){
        listeners.add(TransferListener.class,l);
    }

    public void removeTransferListener(TransferListener l){
        listeners.remove(TransferListener.class,l);
    }

}

class TransferEvent extends AWTEvent{
    final String message;
    public TransferEvent(JComponent t,String message){
        super(t,TRANSFER_START_EVENT);
        this.message = message;
    }
    public static final int TRANSFER_START_EVENT = AWTEvent.RESERVED_ID_MAX + 5555;
}

interface TransferListener extends EventListener {
    void transferStarted(TransferEvent event);
    void transferFinished(TransferEvent event);
}

class WaitingWindow extends JFrame {
    JLabel text = new JLabel();
    WaitingWindow(){
        super();
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(500,100);
        setResizable(false);
        add(text);
        new RepaintThread(this);
    }
}

class RepaintThread extends Thread{

    WaitingWindow w;
    Random random = new Random();

    RepaintThread(WaitingWindow w){
        super();
        this.w = w;
        start();
    }

    public void run(){
        while(true) {
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
            }
            w.text.setText(Math.abs(random.nextInt())%10 + "");
        }
    }

}

class FileTransferListener implements TransferListener{
    WaitingWindow ww = new WaitingWindow();

    public FileTransferListener(){
        ww.setVisible(false);
    }

    public void transferStarted(TransferEvent event){
        ww.setVisible(true);
    }
    public void transferFinished(TransferEvent event){
        ww.setVisible(false);
    }
}

class CopyThread extends Thread{
    File src,tgt;
    EventGenerator transfer;

    CopyThread(File s,File t,EventGenerator tr){
        super();
        src = s;
        tgt = t;
        transfer = tr;
        start();
    }

    public void run(){
        transfer.startEvent();
        try {
            char c;
            FileReader in = new FileReader(src);
            FileWriter out = new FileWriter(tgt);
            while((byte)(c = (char)in.read()) != -1) out.write(c);
            out.close();
        }catch(IOException exc) {
            JOptionPane.showMessageDialog(transfer,exc.getMessage());
        }
        transfer.finishEvent();

    }
}