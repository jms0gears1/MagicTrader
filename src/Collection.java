import java.util.ArrayList;

public class Collection {
	ArrayList<Card> collection = new ArrayList<Card>();

	/**
	 * Adds Card c to the your colection of cards
	 * 
	 * @param c
	 */
	public void addCard(Card c) {
		if (contains(c.getName())) {
			int index = indexOf(c.getName());
			Card card = collection.get(index);
			collection.remove(index);

			card.setNumber_of_cards(card.getNumber_of_cards()
					+ c.getNumber_of_cards());

			collection.add(index, card);
		} else {
			collection.add(c);
		}
	}

	public ArrayList<Card> getCollection() {
		return collection;
	}


	public void setCollection(ArrayList<Card> collection) {
		this.collection = collection;
	}


	/**
	 * prints the entire collection of cards with the cards price
	 */
	public void printCollection() {
		for (Card c : collection)
			System.out.println(c.getName() + " : {" + c.getPrice()[0] + ","
					+ c.getPrice()[1] + "," + c.getPrice()[2] + "}");
	}
	
	/**
	 * Prints the aggregate price of your collection
	 */
	public void priceCollection() {
		float price = 0;

		for (Card c : collection) {
			String[] prices = c.getPrice();

			if (prices != null)
				price += Float.parseFloat(prices[1].replace("$", "").replace(
						"\u00a0", ""));
		}

		System.out.println("Price of collection : " + price);
	}
	
	/**
	 * Removes Card c from the collection
	 * @param c - Card to remove
	 */
	public void removeCard(Card c) {
		int index = indexOf(c.getName());
		collection.remove(index);
	}
	
	/**
	 * Removes n number of Card c from your collection
	 * @param c - Card to remove
	 * @param n - Number to remove
	 */
	public void removeCard(Card c, int n) {
		int index = indexOf(c.getName());

		Card card = collection.get(index);
		card.setNumber_of_cards(card.getNumber_of_cards() - n);

		collection.remove(index);
		collection.add(index, card);
	}

	private int indexOf(String name) {
		for (int i = 0; i < collection.size(); ++i) {
			if (collection.get(i).getName().equals(name))
				return i;
		}

		return -1;
	}

	private boolean contains(String name) {
		return (indexOf(name) != -1);
	}
}
