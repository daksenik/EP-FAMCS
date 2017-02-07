import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MainWindow extends JFrame {
    public MainWindow(){
        super();
        JPanel panel = new JPanel();
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        add(panel);
        setSize(500,500);
        panel.setSize(500,500);
        panel.setLayout(new BorderLayout());
        JTextArea jta = new JTextArea("Test");
        JButton btn = new JButton("<html><b><u>Test</u></b></html>");
        jta.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                btn.setText(jta.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                btn.setText(jta.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                btn.setText(jta.getText());
            }
        });

        panel.add(jta,BorderLayout.CENTER);
        panel.add(btn,BorderLayout.SOUTH);
        setVisible(true);
    }

    public static void main(String[] args) {
        new MainWindow();
    }
}