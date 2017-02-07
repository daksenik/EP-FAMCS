import javax.annotation.Resource;
import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.text.DateFormat;
import java.util.*;

/**
 * Created by user on 11.12.2016.
 */
public class ReflectionDemo extends JFrame {
    JTextField currentClass = new JTextField("java.lang.Integer");
    JTextField obj = new JTextField("1",10);
    JList<Method> methodList = new JList<>();
    Class currentClassC;
    Date nowDate = new Date();
    JScrollPane listScroll = new JScrollPane(methodList);
    Method[] methods;
    JFrame frame;
    DefaultListModel model = new DefaultListModel();
    JPanel methodPane = new JPanel(new GridLayout(0,1));
    ArrayList<JTextField>args = new ArrayList<>();
    JButton invokeBut = new JButton("Invoke");
    JButton switchLang = new JButton("Рус");
    Locale curLocale = new Locale("en","US");
    ResourceBundle messages = ResourceBundle.getBundle("MessagesBundle",curLocale);
    JLabel currentDate = new JLabel(DateFormat.getDateInstance(DateFormat.LONG,curLocale).format(new Date()));
    JLabel currentCurrency = new JLabel(Currency.getInstance(curLocale).toString());
    
    public ReflectionDemo(){
        super();
        frame = this;
        currentClass.setColumns(20);
        methodList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        methodList.setModel(model);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setTitle("Java reflection");
        setLayout(new BorderLayout());
        JButton selectClass = new JButton("Select");
        JPanel north = new JPanel(new FlowLayout());
        north.add(currentClass);
        north.add(selectClass);
        invokeBut.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Method chosenMethod = methodList.getModel().getElementAt(methodList.getSelectedIndex());
                try {
                    Object[] arguments = new Object[args.size()];
                    int id = 0;
                    String temp;
                    for(JTextField jtf : args) {
                        temp = chosenMethod.getParameters()[id].getType().toString();
                        if(temp.equals("int") || temp.equals("class java.lang.Integer"))
                            arguments[id] = Integer.parseInt(jtf.getText());
                        else if(temp.equals("double") || temp.equals("class java.lang.Double"))
                            arguments[id] = Double.parseDouble(jtf.getText());
                        else if(temp.equals("boolean") || temp.equals("class java.lang.Boolean"))
                            arguments[id] = Boolean.parseBoolean(jtf.getText());
                        else arguments[id] = jtf.getText();
                        id++;
                    }
                    Object tempobj = currentClassC.getConstructor(new String(obj.getText()).getClass()).newInstance(new String(obj.getText()));
                    System.out.println(chosenMethod.invoke(tempobj, arguments));
                }catch(Exception exc){
                    JOptionPane.showMessageDialog(frame,exc.getMessage());
                }
            }
        });
        
        methodList.addListSelectionListener(new ListSelectionListener() {
            @Override
            public void valueChanged(ListSelectionEvent e) {
                Method chosenMethod = methodList.getModel().getElementAt(methodList.getSelectedIndex());
                fillMethodPane(chosenMethod);
            }
        });
        
        selectClass.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    fillList(Class.forName(currentClass.getText()));
                }catch(Exception exc){
                    JOptionPane.showMessageDialog(frame,exc.getMessage());
                }
            }
        });
        
        switchLang.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(curLocale.getLanguage().equals("en")) {
                    //current is Russian
                    curLocale = new Locale("ru","RU");
                    messages = ResourceBundle.getBundle("MessagesBundle",curLocale);
                } else{
                    //current is English
                    curLocale = new Locale("en","US");
                    messages = ResourceBundle.getBundle("MessagesBundle",curLocale);
                }
                invokeBut.setText(messages.getString("invoke"));
                switchLang.setText(messages.getString("langbut"));
                selectClass.setText(messages.getString("select"));
                currentDate.setText(DateFormat.getDateInstance(DateFormat.LONG,curLocale).format(nowDate));
                currentCurrency.setText(Currency.getInstance(curLocale).toString());
            }
        });
        
        add(north, BorderLayout.NORTH);
        add(listScroll, BorderLayout.WEST);
        add(methodPane, BorderLayout.CENTER);
        JPanel temp = new JPanel();
        temp.add(switchLang);
        temp.add(currentDate);
        temp.add(currentCurrency);
        add(temp,BorderLayout.SOUTH);
        setSize(800,500);
        setVisible(true);
    }
    
    void fillList(Class c){
        currentClassC = c;
        methods = c.getMethods();
        model.clear();
        for(Method m : methods) if(m.getDeclaringClass() == c) model.addElement(m);
    }
    
    void fillMethodPane(Method m){
        methodPane.removeAll();
        args.clear();
        Parameter[] pars = m.getParameters();
        JPanel temp = new JPanel();
        temp.add(obj);
        methodPane.add(temp);
        for(Parameter p : pars){
            args.add(new JTextField(10));
            Class parClass = p.getType();
            String name = p.getName();
            temp = new JPanel();
            temp.add(new JLabel(parClass + " " + name));
            temp.add(args.get(args.size()-1));
            methodPane.add(temp);
        }
        temp = new JPanel();
        temp.add(invokeBut);
        methodPane.add(temp);
        methodPane.updateUI();
    }
    
    public static void main(String[] args) {
        new ReflectionDemo();
    }
}
