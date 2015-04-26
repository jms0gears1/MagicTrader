import java.util.ArrayList;


public class Collection {
	ArrayList<Card> collection = new ArrayList<Card>();
	
	public void addCard(Card c){
		if(contains(c.getName())){
			int index = indexOf(c.getName());
			Card card = collection.get(index);
			collection.remove(index);
			
			card.setNumber_of_cards(card.getNumber_of_cards() + c.getNumber_of_cards());
			
			collection.add(index, card);
		}else{
			collection.add(c);
		}
	}
	
	public void printCollection(){
		for(Card c: collection)
			System.out.println(c.getName());
	}
	
	public void priceCollection(){
		float price = 0;
		
		for(Card c : collection){
			String[] prices = c.getPrice();
			
			if(prices!=null)
				price += Float.parseFloat(prices[1].replace("$", "").replace("\u00a0", ""));
		}
		
		System.out.println("Price of collection : " + price);
	}
	
	public void removeCard(Card c){
		int index = indexOf(c.getName());
		collection.remove(index);
	}
	
	public void removeCard(Card c, int n){
		int index = indexOf(c.getName());
		
		Card card = collection.get(index);
		card.setNumber_of_cards(card.getNumber_of_cards() - n);
		
		collection.remove(index);
		collection.add(index, card);
	}
	
	public int indexOf(String name){
		for(int i = 0; i < collection.size(); ++i){
			if(collection.get(i).getName().equals(name))
				return i;
		}
		
		return -1;
	}
	
	public boolean contains(String name){
		return (indexOf(name)!=-1);
	}
}
