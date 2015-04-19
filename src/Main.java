import java.util.ArrayList;
import java.util.Scanner;

import rx.Subscriber;

public class Main extends Subscriber<Card> {

	private static Scanner input = null;
	Collection collection;

	public static void main(String[] args) {
		Main main = new Main();
	}

	public Main() {

		input = new Scanner(System.in);
		collection = new Collection();
		while (true) {

			System.out
					.println("Enter Command (say help for list of commands): ");
			String[] cmd = input.nextLine().split("\\s+");

			if (cmd.length == 1) {
				if (cmd[0].equals("help")) {
					command();
				}else if(cmd[0].equals("exit"))
					System.exit(0);
			}else{
				if(cmd[0].equals("add")){
					CardFactory.getCard(cmd[1]).subscribe(new Subscriber<Card>(){

						@Override
						public void onCompleted() {}

						@Override
						public void onError(Throwable t) {
							System.out.println(t.toString());
						}

						@Override
						public void onNext(Card card) {
							collection.addCard(card);
						}
					});
				}else if(cmd[0].equals("remove")){
					CardFactory.getCard(cmd[1]).subscribe(new Subscriber<Card>(){

						@Override
						public void onCompleted() {}

						@Override
						public void onError(Throwable t) {
							System.out.println(t.toString());
						}

						@Override
						public void onNext(Card card) {
							collection.removeCard(card);
						}
						
					});
				}else if(cmd[0].equals("print") && cmd[1].equals("collection")){
					collection.printCollection();
				}
			}
		}
	}
	
	public void command(){
		System.out.println("add {card}");
		System.out.println("remove {card}");
		System.out.println("print collection");
		System.out.println("exit");
	}

	public void getCard(String cardName) {
		CardFactory.getCard(cardName).subscribe(this);
	}

	@Override
	public void onCompleted() {
		System.out.println("completed");
	}

	@Override
	public void onError(Throwable arg0) {
		System.out.println(arg0.toString());
	}

	@Override
	public void onNext(Card arg0) {
		System.out.println(arg0.getName());
		this.unsubscribe();
	}
}
