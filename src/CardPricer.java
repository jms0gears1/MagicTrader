import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class CardPricer {

	public final static String PRICE_GUIDE = "http://magic.tcgplayer.com/magic_price_guides.asp";
	public final static String CARD_PRICE_GUIDE = "http://magic.tcgplayer.com/db/price_guide.asp?setname=";
	ArrayList<String> expansions = new ArrayList<String>();
	HashMap<String, String> cards = new HashMap<String, String>();

	public CardPricer() {
		getAllExpansionNames().observeOn(Schedulers.newThread()).subscribe(
				new Subscriber<ArrayList<String>>() {

					@Override
					public void onCompleted() {
						if (expansions != null) {
							matchCards(expansions);
							this.unsubscribe();
						}
					}

					@Override
					public void onError(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNext(ArrayList<String> arg0) {
						expansions = arg0;
					}

				});
	}
	
	public void printSets(){
		Set<String> keys = cards.keySet();
		
		for(String key : keys)
			if(key.trim().equalsIgnoreCase("animar, soul of elements"))
				System.out.println("matched");
	}

	private void matchCards(ArrayList<String> expansions) {
		matchCardsWithSets(expansions).observeOn(Schedulers.newThread())
				.subscribe(new Subscriber<HashMap<String, String>>() {

					@Override
					public void onCompleted() {
						System.out.println("finished loading data");
						this.unsubscribe();
					}

					@Override
					public void onError(Throwable arg0) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onNext(HashMap<String, String> arg0) {
						cards = arg0;
					}

				});
	}

	public Observable<Card> getPrice(final Card card) {
		return Observable.create(new Observable.OnSubscribe<Card>() {

			String set = "";
			String[] prices = new String[3];
			Document doc = null;

			@Override
			public void call(Subscriber<? super Card> sub) {
				try {
					set = cards.get(card.getName().toLowerCase());
					String url = CARD_PRICE_GUIDE + set;
					doc = Jsoup.connect(url).get();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (doc != null) {
					for (Element tr : doc.select("table").get(2).select("tr")) {
						String name = tr.select("td").get(0).text().replace("\u00a0", "");
						if(name.equalsIgnoreCase(card.getName())){
							prices[0] = tr.select("td").get(5).text();
							prices[1] = tr.select("td").get(6).text();
							prices[2] = tr.select("td").get(7).text();
							break;
						}
					}
					
					card.setPrice(prices);
					sub.onNext(card);
					if (!sub.isUnsubscribed())
						sub.onCompleted();
				} else {
					sub.onNext(null);
					if (!sub.isUnsubscribed())
						sub.onCompleted();
				}
			}
		});
	}

	public Observable<HashMap<String, String>> matchCardsWithSets(
			final ArrayList<String> expansions) {
		return Observable
				.create(new Observable.OnSubscribe<HashMap<String, String>>() {

					@Override
					public void call(
							Subscriber<? super HashMap<String, String>> sub) {
						HashMap<String, String> temp = new HashMap<String, String>();
						Document doc = null;

						for (String s : expansions) {
							try {
								doc = Jsoup.connect(CARD_PRICE_GUIDE + s).get();
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							if (doc != null) {
								for (Element tr : doc.select("table").get(2)
										.select("tr")) {
									temp.put(tr.select("td").get(0).text().replace("\u00a0", "").toLowerCase(), s.toLowerCase());
								}

							} else {
								sub.onNext(null);
								if (!sub.isUnsubscribed())
									sub.onCompleted();
							}
						}
						
						sub.onNext(temp);
						if (!sub.isUnsubscribed())
							sub.onCompleted();
					}
				});
	}

	public Observable<ArrayList<String>> getAllExpansionNames() {
		return Observable
				.create(new Observable.OnSubscribe<ArrayList<String>>() {

					@Override
					public void call(Subscriber<? super ArrayList<String>> sub) {
						ArrayList<String> temp = new ArrayList<String>();
						Document doc = null;

						try {
							doc = Jsoup.connect(PRICE_GUIDE).get();
						} catch (IOException e) {
							e.printStackTrace();
						}

						if (doc != null) {
							for (Element link : doc.select("a[href]")) {
								String str = link.attr("href");
								if (str.contains("setName=")) {
									temp.add(link.text());
								}
							}
							sub.onNext(temp);
							if (!sub.isUnsubscribed())
								sub.onCompleted();
						} else {
							sub.onNext(null);
							if (!sub.isUnsubscribed())
								sub.onCompleted();
						}
					}
				});

	}
}
