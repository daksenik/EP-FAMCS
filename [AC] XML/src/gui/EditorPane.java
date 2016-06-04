package gui;

import shapes.Shape;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by daksenik on 02.05.2016.
 */
public class EditorPane extends JPanel {
    public JTextArea textArea;
    public JButton applyBut,resetBut;
    Shape curShape;

    JPanel viewer;

    public EditorPane(JPanel view){
        viewer = view;
        setLayout(new BorderLayout());
        textArea = new JTextArea(18,15);
        add(textArea,BorderLayout.CENTER);
        applyBut = new JButton("Apply");
        resetBut = new JButton("Reset");
        JPanel botPan = new JPanel();
        botPan.setLayout(new GridLayout());
        botPan.add(applyBut);
        botPan.add(resetBut);
        applyBut.setEnabled(false);
        resetBut.setEnabled(false);
        add(botPan,BorderLayout.SOUTH);
        add(new JSeparator(SwingConstants.VERTICAL),BorderLayout.WEST);

        applyBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                curShape.setParameterString(textArea.getText());
                viewer.repaint();
            }
        });
        resetBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                textArea.setText(curShape.getParameterString());
            }
        });
        setVisible(true);
    }

    public void setShape(Shape sh){
        if(sh==null)return;
        applyBut.setEnabled(true);
        resetBut.setEnabled(true);
        curShape = sh;
        textArea.setText(sh.getParameterString());
    }
}
