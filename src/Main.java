import java.util.Scanner;

import rx.Observable;
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
					.flatMap(cardsWithNoPrice -> pricer.getPrice(cardsWithNoPrice))
					.subscribe(cardsWithPrice -> collection.addCard(cardsWithPrice));
				break;
			case 1:
					CardFactory.getCard(getAfterCommand(cmd, commands[1]))
					.observeOn(Schedulers.newThread())
					.subscribe(card -> collection.removeCard(card));
				break;
			case 2:
					collection.printCollection();
				break;
			case 3:
					//find white
					getColor(ManaSymbol.WHITE);	
				break;
			case 4:
					//find blue
					getColor(ManaSymbol.BLUE);	
				break;
			case 5:
					//find black
					getColor(ManaSymbol.BLACK);	
				break;
			case 6:
					//find red
					getColor(ManaSymbol.RED);	
				break;
			case 7:
					//find green
					getColor(ManaSymbol.GREEN);	
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
	
	public enum ManaSymbol{
		WHITE("W"), BLUE("U"), BLACK("B"), RED("R"), GREEN("G");

		private String val;
		private ManaSymbol(String val){
			this.val = val;
		}
		
		public String toString(){
			return this.val;
		}
	}
	
	public void getColor(ManaSymbol color){
		Observable.from(collection.getCollection())
		.filter(card -> {return card.getMana_cost().contains(color.toString());})
		.subscribe(whiteCard -> {System.out.println(whiteCard.getName());});
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
}
