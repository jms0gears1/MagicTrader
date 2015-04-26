import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;
import rx.Observable;
import rx.Observable.Transformer;

public class CardFactory {

	static final String API_ENDPOINT = "http://scry.me.uk";
	static final String API_GET_CARD = "/api.php";

	static RestAdapter restAdapter;

	private CardFactory() {}
	
	/**
	 * Asynchronously returns a Card object from http://scry.me.uk/api.php
	 * @param card - the name of the card to search
	 * @return	a card object of the card with given name
	 */
	public static Observable<Card> getCard(String card) {
		restAdapter = new RestAdapter.Builder()
			.setEndpoint(API_ENDPOINT)
			.build();
		
		if(restAdapter!=null)
			return queryCard(card);
		
		return null;
	}

	private static Observable<Card> queryCard(String cardName) {
		CardInfoService service = restAdapter.create(CardInfoService.class);
		return service.getCardInfo(cardName.replace(" ", "%20"));
	}

	private interface CardInfoService {
		@GET(API_GET_CARD)
		Observable<Card> getCardInfo(@Query("name") String cardName);
	}
}
