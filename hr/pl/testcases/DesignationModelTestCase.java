import java.awt.*;
import javax.swing.*;
import com.thinking.machines.hr.pl.model.*;
import com.thinking.machines.hr.bl.exceptions.*;
class DesignationModelTestCase extends JFrame
{
private JTable tb;
private DesignationModel designationModel;
private JScrollPane jsp;
private Container container;
DesignationModelTestCase()
{
designationModel=new DesignationModel();
tb=new JTable(designationModel);
jsp=new JScrollPane(tb,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
container=getContentPane();
container.setLayout(new BorderLayout());
container.add(jsp);
setLocation(100,200);
setSize(500,400);
setVisible(true);  	 	
setDefaultCloseOperation(EXIT_ON_CLOSE);
}
public static void main(String gg[])
{
DesignationModelTestCase dmtc=new DesignationModelTestCase();
}
}