package clueGUI;

import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

@SuppressWarnings("serial")
public class MyCards extends JPanel{
	public MyCards(){
		setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));
		setLayout(new GridLayout(0,1));
		PeoplePanel people = new PeoplePanel();
		RoomPanel rooms = new RoomPanel();
		WeaponPanel weapons = new WeaponPanel();
		add(people);
		add(rooms);
		add(weapons);
	}
	
	public class PeoplePanel extends JPanel {
		private JTextField people;
		
		public PeoplePanel() {
			people = new JTextField("test", 10);
			//people.setEditable(false);
			add(people);
			setBorder(new TitledBorder (new EtchedBorder(), "People"));
			setPreferredSize(new Dimension(10,10));
		}
	}
	
	public class RoomPanel extends JPanel {
		private JTextField rooms;
		
		public RoomPanel() {
			rooms = new JTextField(10);
			//people.setEditable(false);
			add(rooms);
			setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		}
	}
	
	public class WeaponPanel extends JPanel {
		private JTextField weapons;
		
		public WeaponPanel() {
			weapons = new JTextField(10);
			//people.setEditable(false);
			add(weapons);
			setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		}
	}
}
