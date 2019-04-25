/*UserInterface.class*/
/*setting userInterface*/
//author : SoraY677
//write begin : 2019.4.25
//write finish : 2019.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class UserInterface extends JFrame implements ActionListener{

    //定数
    private static final String st101 = "Shift-Borad";
    private static final String st102 = "Open";
    private static final String st201 = "Close";

    JPanel mainpanel = new JPanel();
    JPanel panel101 = new JPanel();
    JPanel panel102 = new JPanel();
    JPanel panel201 = new JPanel();
    Container container = getContentPane();
    private CardLayout clayout = new CardLayout();


    public UserInterface(){
        /*Creating Menubar*/
        JMenuBar menubar = new JMenuBar();
        JMenu menu1 = new JMenu("menu1");
        JMenu menu2 = new JMenu("menu2");
        menubar.add(menu1);
        menubar.add(menu2);
        JMenuItem menuitem1 = new JMenuItem(st101);
        JMenuItem menuitem2 = new JMenuItem(st102);
        JMenuItem menuitem3 = new JMenuItem(st201);
        menu1.add(menuitem1);
        menu1.add(menuitem2);
        menu1.add(menuitem3);
        setJMenuBar(menubar);

        mainpanel.setLayout(clayout);

        panel101.setBackground(Color.red);
        panel101.setVisible(true);
        panel102.setBackground(Color.white);
        panel102.setVisible(true);
        panel201.setBackground(Color.black);
        panel201.setVisible(true);
        mainpanel.add(panel101,st101);
        mainpanel.add(panel102,st102);
        mainpanel.add(panel201,st201);
        container.add(mainpanel);

        menuitem1.addActionListener(this);
        menuitem2.addActionListener(this);
        menuitem3.addActionListener(this);

        setTitle("TTCreater");
        setBounds(0,0,640,480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    //creating panel
    public void ReCreatingPanel(){
        UserInterface mainframe = new UserInterface();
        mainframe.setVisible(true);
    }

    //action event on menuitem
    public void actionPerformed(ActionEvent e) {
        switch (e.getActionCommand()){
            case st101:
                clayout.show(panel101.getParent(),e.getActionCommand());
                break;
            case st102:
                clayout.show(panel102.getParent(),e.getActionCommand());
                break;
            case st201:
                clayout.show(panel102.getParent(),e.getActionCommand());
                break;
            default :
                System.out.println("error: page no expected ");
                break;
        }
    }

    //change state (char statenum)
    public void SwitchState(char state){
        switch(state){

        }
    }
}
