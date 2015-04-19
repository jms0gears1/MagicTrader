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
			String cmd = input.nextLine();
			
			if(cmd.equalsIgnoreCase("help"))
				command();
			else if(cmd.equalsIgnoreCase("exit"))
				System.exit(0);
			else if(cmd.equalsIgnoreCase("print"))
				collection.printCollection();
			else if(cmd.substring(0, "add".length()).equalsIgnoreCase("add"))
				CardFactory.getCard(cmd.substring("add".length(), cmd.length())).subscribe(new Subscriber<Card>(){

					@Override
					public void onCompleted() {}

					@Override
					public void onError(Throwable t) {
						System.out.println(t.toString());
					}

					@Override
					public void onNext(Card card) {
						if(card.getError()==null)
							collection.addCard(card);
					}
					
				});
			else if(cmd.substring(0, "remove".length()).equalsIgnoreCase("remove"))
				CardFactory.getCard(cmd.substring("remove".length(), cmd.length())).subscribe(new Subscriber<Card>(){

					@Override
					public void onCompleted() {}

					@Override
					public void onError(Throwable t) {
						System.out.println(t.toString());
					}

					@Override
					public void onNext(Card card) {
						if(card.getError()==null)
							collection.removeCard(card);
					}
					
				});
			else if(cmd.length()>= "find blue".length() && cmd.substring(0, "find blue".length()).equalsIgnoreCase("find blue"))
				for(Card c: collection.collection){
					if(c.getMana_cost().contains("U"))
						System.out.println(c.getName());
				}
			else if(cmd.length()>= "find green".length() && cmd.substring(0, "find green".length()).equalsIgnoreCase("find green"))
				for(Card c: collection.collection){
					if(c.getMana_cost().contains("G"))
						System.out.println(c.getName());
				}
			else if(cmd.length()>= "find red".length() && cmd.substring(0, "find red".length()).equalsIgnoreCase("find red"))
				for(Card c: collection.collection){
					if(c.getMana_cost().contains("R"))
						System.out.println(c.getName());
				}
			else if(cmd.length()>= "find black".length() && cmd.substring(0, "find black".length()).equalsIgnoreCase("find black"))
				for(Card c: collection.collection){
					if(c.getMana_cost().contains("B"))
						System.out.println(c.getName());
				}
			else if(cmd.length()>= "find white".length() && cmd.substring(0, "find white".length()).equalsIgnoreCase("find white"))
				for(Card c: collection.collection){
					if(c.getMana_cost().contains("W"))
						System.out.println(c.getName());
				}
		}
	}
	
	public void command(){
		System.out.println("add {card}");
		System.out.println("remove {card}");
		System.out.println("print collection");
		System.out.println("find white");
		System.out.println("find blue");
		System.out.println("find red");
		System.out.println("find black");
		System.out.println("find green");
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
