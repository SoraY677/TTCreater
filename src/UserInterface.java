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

    public UserInterface(){
        /*Creating Menubar*/
        JMenuBar menubar = new JMenuBar();
        JMenu menu1 = new JMenu("menu1");
        JMenu menu2 = new JMenu("menu2");
        menubar.add(menu1);
        menubar.add(menu2);
        JMenuItem menuitem1 = new JMenuItem("New");
        JMenuItem menuitem2 = new JMenuItem("Open");
        JMenuItem menuitem3 = new JMenuItem("Close");
        menu1.add(menuitem1);
        menu1.add(menuitem2);
        menu1.add(menuitem3);
        setJMenuBar(menubar);

        JPanel panel = new JPanel();
        Container container = getContentPane();
        container.add(panel);

        menuitem1.addActionListener(this);
        menuitem2.addActionListener(this);
        menuitem3.addActionListener(this);

        setTitle("TTCreater");
        setBounds(0,0,640,480);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void CreatingPanel(){
        UserInterface mainframe = new UserInterface();
        mainframe.setVisible(true);
    }

    //メニューのイベント処理
    public void actionPerformed(ActionEvent e) {

        switch (e.getActionCommand()){
            case "New":
                System.out.println("Hello new System!");
                break;
        }
    }
}
