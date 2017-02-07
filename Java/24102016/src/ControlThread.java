import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created by daksenik on 24.10.2016.
 */

class DrawableImage{
    BufferedImage img;
    int x,y;
    ReadWriteLock lock;
    DrawableImage(int x,int y,File in){
        try {
            img = ImageIO.read(in);
        }catch(Exception e){
            System.out.println(e);
        }
        lock = new ReentrantReadWriteLock();
        this.x = x;
        this.y = y;
    }

    void set(int x,int y){
        lock.writeLock().lock();
        try{
            this.x=x;
            this.y=y;
        }finally{
            lock.writeLock().unlock();
        }
    }

    Pair<Integer,Integer>get(){
        lock.readLock().lock();
        try{
            return new Pair<>(x,y);
        }finally{
            lock.readLock().unlock();
        }
    }

    void draw(Graphics g,int w,int h){
        g.clearRect(0,0,w,h);
        g.drawImage(img,x,y,null);
    }
}

class MotionThread extends Thread{
    DrawableImage img = null;
    byte motionStep = 0;
    Graphics g;
    JFrame pPanel;
    boolean state;
    MotionThread(DrawableImage img,JFrame pp){
        super();
        this.img = img;
        pPanel = pp;
        state = true;
    }

    public void run(){
        while(state) {
            try {
                Thread.sleep(5);
            }catch(Exception e){System.out.println(e);}
            int x, y, maxX = pPanel.getWidth(), maxY = pPanel.getHeight();
            Pair<Integer, Integer> temp = img.get();
            x = temp.getKey();
            y = temp.getValue();
            switch (motionStep) {
                case 0:
                    x++;
                    y++;
                    if (x > maxX || y > maxY) {
                        x = maxX;
                        y = 0;
                        motionStep = 1;
                    }
                    break;
                case 1:
                    x--;
                    y++;
                    if (x < 0 || y > maxY) {
                        x = 0;
                        y = 0;
                        motionStep = 0;
                    }
                    break;
                default:
                    break;
            }
            if (img != null && pPanel.getGraphics() != null) {
                img.set(x, y);
                img.draw(pPanel.getGraphics(),pPanel.getWidth(),pPanel.getHeight());
            }
        }
    }
}

class CheckThread extends Thread{
    DrawableImage img = null;
    PaintingPanel pp = null;
    int count = 0;
    CheckThread(DrawableImage img,PaintingPanel pp){
        super();
        this.img = img;
        this.pp = pp;
    }
    public void run(){
        while(count<5){
            if(img.get().getValue()==pp.getHeight()){
                count++;
                img.set(img.get().getKey(),pp.getHeight()+1);
                pp.setTitle(Integer.toString(count)+"/5");
            }
        }
    }
}

public class ControlThread {
    public static void main(String[] args) {
        DrawableImage img = new DrawableImage(0,30,new File("D:\\Programming\\BSU FAMCS\\Спецкурс\\24102016\\src\\image.jpg"));
        PaintingPanel pp = new PaintingPanel(img);
        MotionThread mt = new MotionThread(img,pp);
        CheckThread ct = new CheckThread(img,pp);
        mt.start();
        ct.start();
        try{
            ct.join();
            mt.state = false;
            mt.join();
            System.out.println("Threads finished");
        }catch(Exception e){
            System.out.println(e);
        }
    }
}

class PaintingPanel extends JFrame {
    DrawableImage img;
    public PaintingPanel(DrawableImage img){
        super();
        this.img = img;
        setTitle("0/5");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(500,500);
        setVisible(true);
    }

    public void paint(Graphics g){
        img.draw(g,getWidth(),getHeight());
    }
}
