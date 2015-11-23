package clueGame;

public class Solution {
	public String person;
	public String room;
	public String weapon;
	
	public Solution(String person, String room, String weapon) {
		super();
		this.person = person;
		this.room = room;
		this.weapon = weapon;
	}
	
	public String toString(){
		return person + " in the " + room + " with the " + weapon;
	}
	
}
