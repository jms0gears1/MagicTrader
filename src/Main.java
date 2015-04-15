import retrofit.RestAdapter;
import retrofit.http.GET;
import retrofit.http.Query;

public class Main {

	static final String API_ENDPOINT = "http://scry.me.uk";
	static final String API_GET_CARD = "/api.php";
	
	RestAdapter restAdapter;
	Collection collection;

	public static void main(String[] args) {
		Main main = new Main();
	}

	public Main() {
		restAdapter = new RestAdapter.Builder().setEndpoint(API_ENDPOINT)
				.build();
	}

	public Card queryCard(String cardName) {
		CardInfoService service = restAdapter.create(CardInfoService.class);
		return service.getCardInfo(cardName);
	}

	public interface CardInfoService {
		@GET(API_GET_CARD)
		Card getCardInfo(@Query("name") String cardName);
	}

	public class CardImage {
		String id;
		String name;
	}
}
