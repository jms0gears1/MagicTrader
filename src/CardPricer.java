import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

public class CardPricer {

	public final static String PRICE_GUIDE = "http://magic.tcgplayer.com/magic_price_guides.asp";
	public final static String CARD_PRICE_GUIDE = "http://magic.tcgplayer.com/db/price_guide.asp?setname=";
	ArrayList<String> expansions = new ArrayList<String>();
	HashMap<String, String> cards = new HashMap<String, String>();

	public CardPricer() {
		// getExpansionList();
		getExpoLambdas();
	}

	/**
	 * Gets an ArrayList<String> containing every expansion name
	 */
	private void getExpoLambdas() {
		getAllExpansionNames()
		.observeOn(Schedulers.newThread())
		.flatMap(sets -> matchCardsWithSetsLambdas(sets))
		.subscribe(cardSets -> {
			cards = cardSets;
			System.out.println("data loaded");
			});
	}

	/**
	 * Takes a card object and gets the {High, Median, Low} price values for
	 * card
	 * 
	 * @param card
	 *            - Card object that needs pricing information
	 * @return Card Object with pricing information
	 */
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
						String name = tr.select("td").get(0).text()
								.replace("\u00a0", "");
						if (name.equalsIgnoreCase(card.getName())) {
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
	
	private Observable<HashMap<String, String>> matchCardsWithSetsLambdas(
			ArrayList<String> expos) {
		return Observable.create(subscriber -> {

			Document doc = null;
			HashMap<String, String> temp = new HashMap<String, String>();

			for (String s : expos) {

				try {
					doc = Jsoup.connect(CARD_PRICE_GUIDE + s).get();
				} catch (Exception e) {
					// TODO Auto-generated catch block
				e.printStackTrace();
			}

			if (doc != null) {
				Elements table = doc.select("table").get(2).select("tr");

				for (Element tr : table) {

					temp.put(tr.select("td").get(0).text()
							.replace("\u00a0", "").toLowerCase(),
							s.toLowerCase());
				}
			} else {
				subscriber.onNext(null);
				if (!subscriber.isUnsubscribed())
					subscriber.onCompleted();
			}
		}

		subscriber.onNext(temp);
		if (!subscriber.isUnsubscribed())
			subscriber.onCompleted();
	})	;
	}

	private Observable<ArrayList<String>> getAllExpansionNames() {
		return Observable
				.create(new Observable.OnSubscribe<ArrayList<String>>() {

					ArrayList<String> temp;
					Document doc;

					@Override
					public void call(Subscriber<? super ArrayList<String>> sub) {

						temp = new ArrayList<String>();
						doc = null;

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
