import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import rx.Observable;
import rx.Subscriber;

public class CardPricer {

	public final static String PRICE_GUIDE = "http://magic.tcgplayer.com/magic_price_guides.asp";
	public final static String CARD_PRICE_GUIDE = "http://magic.tcgplayer.com/db/price_guide.asp?setname=";
	ArrayList<String> sets = new ArrayList<String>();
	HashMap<String, String> cards = new HashMap<String, String>();

	public CardPricer() {
		
	}
	
	public void getPrice(String cardName) throws IOException{
		String set = cards.get(cardName);
		
		Document doc = Jsoup.connect(CARD_PRICE_GUIDE + set).get();
		
		for(Element tr : doc.select("table").get(2).select("tr")){
			if(tr.select("td").get(0).text().equalsIgnoreCase(cardName)){
				System.out.println(tr.select("td").get(0).text() + " : " +  tr.select("td").get(6).text());
			}
		}
	}
	
	public Observable<HashMap<String, String>> matchCardsWithSets(final ArrayList<String> expansions){
		return Observable.create(new Observable.OnSubscribe<HashMap<String, String>>() {

			@Override
			public void call(Subscriber<? super HashMap<String, String>> sub) {
				HashMap<String, String> temp = new HashMap<String, String>();
				Document doc = null;
				
				for (String s : expansions) {
					try {
						doc = Jsoup.connect(CARD_PRICE_GUIDE + s).get();
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
					if(doc!=null){
						for(Element tr : doc.select("table").get(2).select("tr")){
							temp.put(tr.select("td").get(0).text(), s);
						}
						
						sub.onNext(temp);
						if(!sub.isUnsubscribed())
							sub.onCompleted();
					}else{
						sub.onNext(null);
					}
				}
			}
		});
	}

	public HashMap<String, String> matchCardsWithSetsDeprecated(
			ArrayList<String> expansions) throws IOException {
		HashMap<String, String> temp = new HashMap<String, String>();

		Document doc;
		System.out.println(expansions.size());

		for (String s : expansions) {
			doc = Jsoup.connect(CARD_PRICE_GUIDE + s).get();
			
			for(Element tr : doc.select("table").get(2).select("tr")){
				temp.put(tr.select("td").get(0).text(), s);
			}
		}
		
		return temp;
	}
	
	public Observable<ArrayList<String>> getAllExpansionNames(){
		return Observable.create(new Observable.OnSubscribe<ArrayList<String>>() {

			@Override
			public void call(Subscriber<? super ArrayList<String>> sub) {
				ArrayList<String> temp = new ArrayList<String>();
				Document doc = null;
				
				try {
					doc = Jsoup.connect(PRICE_GUIDE).get();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				if(doc!=null){
					for (Element link : doc.select("a[href]")) {
						String str = link.attr("href");
						if (str.contains("setName=")){
							temp.add(link.text());
						}
					}
					sub.onNext(temp);
					if(!sub.isUnsubscribed())
						sub.onCompleted();
				}else{
					sub.onNext(null);
				}			
			}
		});
		
	}

	public ArrayList<String> getAllExpansionNamesDeprecated() throws IOException {
		ArrayList<String> temp = new ArrayList<String>();

		Document doc = Jsoup.connect(PRICE_GUIDE).get();

		for (Element link : doc.select("a[href]")) {
			String str = link.attr("href");
			if (str.contains("setName=")){
				temp.add(link.text());
			}
		}

		return temp;
	}
}
