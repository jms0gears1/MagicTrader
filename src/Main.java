import java.util.Scanner;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class Main {

	private static Scanner input = null;
	private CardPricer pricer;
	private Collection collection;

	private String[] commands = { "add", "remove", "print", "find white",
			"find blue", "find black", "find red", "find green", "exit",
			"help", "price", "sets"};

	public static void main(String[] args) {
		Main main = new Main();
		main.run();
	}

	public void run() {

		input = new Scanner(System.in);
		pricer = new CardPricer();
		collection = new Collection();
		while (true) {
			System.out
					.println("Enter Command (say help for list of commands): ");
			String cmd = input.nextLine();

			if (cmd.length() >= commands[0].length()
					&& cmd.substring(0, commands[0].length()).equalsIgnoreCase(
							commands[0])) {

				String cardName = cmd.substring(commands[0].length() + 1,
						cmd.length());
				CardFactory.getCard(cardName).observeOn(Schedulers.newThread())
						.subscribe(new Subscriber<Card>() {

							@Override
							public void onCompleted() {
								this.unsubscribe();
							}

							@Override
							public void onError(Throwable arg0) {
							}

							@Override
							public void onNext(Card card) {
								if (card != null)
									pricer.getPrice(card).subscribe(
											new PriceSubscriber());
							}

						});

			} else if (cmd.length() >= commands[1].length()
					&& cmd.substring(0, commands[1].length()).equalsIgnoreCase(
							commands[1])) {

				String cardName = cmd.substring(commands[1].length() + 1,
						cmd.length());
				CardFactory.getCard(cardName).observeOn(Schedulers.newThread())
						.subscribe(new Subscriber<Card>() {

							@Override
							public void onCompleted() {
								this.unsubscribe();
							}

							@Override
							public void onError(Throwable arg0) {
							}

							@Override
							public void onNext(Card card) {
								collection.removeCard(card);
							}

						});

				// remove card

			} else if (cmd.length() >= commands[2].length()
					&& cmd.substring(0, commands[2].length()).equalsIgnoreCase(
							commands[2])) {

				collection.printCollection();

			} else if (cmd.length() >= commands[3].length()
					&& cmd.substring(0, commands[3].length()).equalsIgnoreCase(
							commands[3])) {

				// find white

			} else if (cmd.length() >= commands[4].length()
					&& cmd.substring(0, commands[4].length()).equalsIgnoreCase(
							commands[4])) {

				// find blue

			} else if (cmd.length() >= commands[5].length()
					&& cmd.substring(0, commands[5].length()).equalsIgnoreCase(
							commands[5])) {

				// find red

			} else if (cmd.length() >= commands[6].length()
					&& cmd.substring(0, commands[6].length()).equalsIgnoreCase(
							commands[6])) {

				// find black

			} else if (cmd.length() >= commands[7].length()
					&& cmd.substring(0, commands[7].length()).equalsIgnoreCase(
							commands[7])) {

				// find green

			} else if (cmd.length() >= commands[8].length()
					&& cmd.substring(0, commands[8].length()).equalsIgnoreCase(
							commands[8])) {

				System.exit(0);

			} else if (cmd.length() >= commands[9].length()
					&& cmd.substring(0, commands[9].length()).equalsIgnoreCase(
							commands[9])) {

				command();
			} else if (cmd.length() >= commands[10].length()
					&& cmd.substring(0, commands[10].length())
							.equalsIgnoreCase(commands[10])) {

				collection.priceCollection();
			} else if (cmd.length() >= commands[11].length()
					&& cmd.substring(0, commands[11].length())
							.equalsIgnoreCase(commands[11])) {
				
				System.out.println("hello");
				pricer.printSets();
			}
		}
	}

	public void command() {
		System.out.println("add {card}");
		System.out.println("remove {card}");
		System.out.println("print");
		System.out.println("find white");
		System.out.println("find blue");
		System.out.println("find red");
		System.out.println("find black");
		System.out.println("find green");
		System.out.println("exit");
	}

	private class PriceSubscriber extends Subscriber<Card> {

		@Override
		public void onCompleted() {
			// TODO Auto-generated method stub

		}

		@Override
		public void onError(Throwable arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onNext(Card card) {
			collection.addCard(card);
		}

	}
}
