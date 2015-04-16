
public class Main {
	Collection collection;

	public static void main(String[] args) {
		Main main = new Main();
	}

	public Main() {
		collection = new Collection();
		collection.addCard(CardFactory.getCard("animar"));
		collection.addCard(CardFactory.getCard("sol ring"));
		collection.addCard(CardFactory.getCard("Talrand, Sky Summoner"));
		
		collection.printCollection();
	}
}
