package clueGame;

public class Card {
	private String cardName;
	private CardType type;
	
	public Card(String cardName, CardType type) {
		this.cardName = cardName;
		this.type = type;
	}

	public Card() {
		this.cardName = null;
		this.type = null;
	}
	
	@Override
	public String toString() {
		return "Card [cardName=" + cardName + ", type=" + type + "]";
	}

	public boolean equals(Card other) {
		if(this.cardName == other.cardName && this.type == other.type) {
			return true;
		}
		return false;
	}

	public String getCardName() {
		return cardName;
	}
	
	public CardType getCardType() {	// For testing purposes
		return type;
	}
}
