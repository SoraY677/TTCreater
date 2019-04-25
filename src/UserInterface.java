/*UserInterface.class*/
/*setting userInterface*/
//author : SoraY677
//write begin : 2019.4.25
//write finish : 2019.

import javax.swing.JMenu;
import javax.swing.JMenuBar;

public class UserInterface{

    public UserInterface(){
        JMenuBar menubar = new JMenuBar();
        JMenu menu1 = new JMenu("menu1");
        JMenu menu2 = new JMenu("menu2");
        menubar.add(menu1);
        menubar.add(menu2);
    }

}
