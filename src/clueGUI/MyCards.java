package clueGUI;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import clueGame.Card;
import clueGame.CardType;
import clueGame.Player;
import clueGame.Board;

@SuppressWarnings("serial")
public class MyCards extends JPanel{
	private Player human;
	private Set<Card> peopleCards;
	private Set<Card> roomCard;
	private Set<Card> weaponCards;
	public MyCards(Player player){
		human = player;
		peopleCards = new HashSet<Card>();
		roomCard = new HashSet<Card>();
		weaponCards = new HashSet<Card>();
		for (Card cards : human.getCards()){
			if (cards.getCardType().equals(CardType.PERSON))
				peopleCards.add(cards);
			if (cards.getCardType().equals(CardType.ROOM))
				roomCard.add(cards);
			if (cards.getCardType().equals(CardType.WEAPON))
				weaponCards.add(cards);
		}
		setBorder(new TitledBorder (new EtchedBorder(), "My Cards"));
		setLayout(new GridLayout(0,1));
		PeoplePanel people = new PeoplePanel(peopleCards);
		RoomPanel rooms = new RoomPanel(roomCard);
		WeaponPanel weapons = new WeaponPanel(weaponCards);
		add(people);
		add(rooms);
		add(weapons);
	}
	
	
	public class PeoplePanel extends JPanel {
		
		public PeoplePanel(Set<Card> people) {
			setLayout(new GridLayout(0,1));
			for (Card peoples : people){
				JTextField peopleDrawn = new JTextField(peoples.getCardName(), 10);
				add(peopleDrawn);
			}
			//people.setEditable(false);
			setBorder(new TitledBorder (new EtchedBorder(), "People"));
			setPreferredSize(new Dimension(10,10));
		}
	}
	
	public class RoomPanel extends JPanel {
		
		public RoomPanel(Set<Card>room) {
			setLayout(new GridLayout(0,1));
			for (Card rooms : room){
				JTextField roomsDrawn = new JTextField(rooms.getCardName(), 10);
				add(roomsDrawn);
			}
			setBorder(new TitledBorder (new EtchedBorder(), "Rooms"));
		}
	}
	
	public class WeaponPanel extends JPanel {
		
		public WeaponPanel(Set<Card> weapon) {
			setLayout(new GridLayout(0,1));
			for (Card weaponI : weapon){
				JTextField weapons = new JTextField(weaponI.getCardName(), 10);
				add(weapons);
			}
			setBorder(new TitledBorder (new EtchedBorder(), "Weapons"));
		}
	}
}
