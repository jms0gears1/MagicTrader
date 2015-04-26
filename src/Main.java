import java.util.Scanner;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class Main {

	private static Scanner input = null;
	private CardPricer pricer;
	private Collection collection;

	private String[] commands = { "add", "remove", "print", "find white",
			"find blue", "find black", "find red", "find green", "exit",
			"help", "price" };

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

			switch (selectCommand(cmd)) {

			case 0:
				CardFactory.getCard(getAfterCommand(cmd, commands[0]))
						.observeOn(Schedulers.newThread())
						.subscribe(new AddSubscriber());
				break;
			case 1:
				CardFactory.getCard(getAfterCommand(cmd, commands[1]))
						.observeOn(Schedulers.newThread())
						.subscribe(new RemoveSubscriber());
				break;
			case 2:
				collection.printCollection();
				break;
			case 3:
				break;
			case 4:
				break;
			case 5:
				break;
			case 6:
				break;
			case 7:
				break;
			case 8:
				System.exit(0);
				break;
			case 9:
				help();
				break;
			case 10:
				collection.priceCollection();
				break;
			default:
				break;
			}
		}
	}

	public int selectCommand(String cmd) {
		for (int i = 0; i < commands.length; i++)
			if (getCommand(cmd, commands[i]))
				return i;

		return -1;
	}

	public boolean getCommand(String input, String command) {
		return input.length() >= command.length()
				&& input.substring(0, command.length()).equalsIgnoreCase(
						command);
	}

	public String getAfterCommand(String input, String command) {
		return input.substring(command.length() + 1, input.length());
	}
	

	public void help() {
		for (String s : commands)
			System.out.println(s);
	}

	private class PriceSubscriber extends Subscriber<Card> {

		@Override
		public void onCompleted() {
			this.unsubscribe();
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

	private class AddSubscriber extends Subscriber<Card> {

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
				pricer.getPrice(card).subscribe(new PriceSubscriber());
		}

	}

	private class RemoveSubscriber extends Subscriber<Card> {

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
				collection.removeCard(card);
		}

	}
}
