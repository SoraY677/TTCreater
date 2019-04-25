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

    //状態変数
    private int state = 0;
    private JPanel nowpanel = null;
    JPanel mainpanel = new JPanel();
    JPanel panel101 = new JPanel();
    JPanel panel102 = new JPanel();
    JPanel panel201 = new JPanel();
    Container container = getContentPane();
    private CardLayout clayout = new CardLayout();


    public UserInterface(){
        mainpanel.setLayout(clayout);
        /*Creating Menubar*/
        JMenuBar menubar = new JMenuBar();
        JMenu menu1 = new JMenu("menu1");
        JMenu menu2 = new JMenu("menu2");
        menubar.add(menu1);
        menubar.add(menu2);
        JMenuItem menuitem1 = new JMenuItem("Shift-Board");
        JMenuItem menuitem2 = new JMenuItem("Open");
        JMenuItem menuitem3 = new JMenuItem("Close");
        menu1.add(menuitem1);
        menu1.add(menuitem2);
        menu1.add(menuitem3);
        setJMenuBar(menubar);

        panel101.setBackground(Color.red);
        this.add(panel101);panel101.setVisible(true);
        panel102.setBackground(Color.white);
        this.add(panel102);panel102.setVisible(true);
        panel201.setBackground(Color.black);
        this.add(panel201);panel201.setVisible(true);
        mainpanel.add(panel101);
        mainpanel.add(panel102);
        mainpanel.add(panel201);
        container.add(mainpanel);


        menuitem1.addActionListener(this);
        menuitem2.addActionListener(this);
        menuitem3.addActionListener(this);

        state = 101;
        nowpanel = panel101;

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
            case "Shift-Board":
                state = 101;
                clayout.show(panel101.getParent(),panel101.getName());
                break;
            case "Open":
                state = 102;
                clayout.show(panel102.getParent(),panel102.getName());
                break;
            case "Close":
                state = 201;
                clayout.show(panel102.getParent(),panel102.getName());
                break;
            default :
                state = -1;
                System.out.println("error");
                break;
        }
    }

    //change state (char statenum)
    public void SwitchState(char state){
        switch(state){

        }
    }
}
