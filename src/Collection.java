import java.util.HashMap;
import java.util.Set;


public class Collection {
	private HashMap<String, Integer> collection;
	
	public Collection(){
		collection = new HashMap<String, Integer>();
	}
	
	public void addCard(String cardName, int number){
		if(collection.containsKey(cardName)){
			int curNum = collection.get(cardName);
			collection.put(cardName, new Integer(curNum+number));
		}else
			collection.put(cardName, new Integer(number));
	}
	
	public String getCollection(){
		Set<String> keys = collection.keySet();
		String returnString = "";
		
		for(String s:keys){
			returnString += s+":"+collection.get(s);
			returnString += "\n";
		}
		
		return returnString;
	}
	
	public void removeCard(String cardName){
		if(collection.containsKey(cardName)){
			this.removeCard(cardName, Integer.MAX_VALUE);
		}
	}
	
	public void removeCard(String cardName, int number){
		if(collection.containsKey(cardName)){
			int curNum = collection.get(cardName);
			if(number > curNum)
				collection.remove(cardName);
			else
				collection.put(cardName, curNum-number);
		}
	}
	
	public void dumpCards(){
		if(!collection.isEmpty())
			collection.clear();
	}
}
