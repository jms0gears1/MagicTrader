import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Set;

import rx.Subscriber;
import rx.schedulers.Schedulers;

public class Main {

	private static Scanner input = null;
	private ArrayList<String> expansions;
	private HashMap<String, String> cards;
	private CardPricer pricer;
	Collection collection;

	public static void main(String[] args) {
		Main main = new Main();
	}

	public void getExpansions() {
		pricer.getAllExpansionNames().observeOn(Schedulers.newThread()).subscribe(
				new Subscriber<ArrayList<String>>() {

					@Override
					public void onCompleted() {
						if (expansions != null) {
							System.out.println("finished loading expansions");
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

	public void matchCards(ArrayList<String> expansions) {
		pricer.matchCardsWithSets(expansions).observeOn(Schedulers.newThread()).subscribe(
				new Subscriber<HashMap<String, String>>() {

					@Override
					public void onCompleted() {
						if(cards==null)
							System.out.println("what/");
						System.out.println("loading data complete");
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

	public Main() {

		input = new Scanner(System.in);
		collection = new Collection();
		pricer = new CardPricer();
		this.getExpansions();
		while (true) {

			System.out
					.println("Enter Command (say help for list of commands): ");
			String cmd = input.nextLine();

			if (cmd.equalsIgnoreCase("help"))
				command();
			else if (cmd.equalsIgnoreCase("exit"))
				System.exit(0);
			else if (cmd.equalsIgnoreCase("print")){
				if(cards==null)
					System.out.println("data not yet loaded");
				else{
					Set<String> keys = cards.keySet();
					
					for(String s: keys)
						System.out.println(cards.get(s));
				}
			}
			else if (cmd.substring(0, "add".length()).equalsIgnoreCase("add"))
				CardFactory
						.getCard(cmd.substring("add".length(), cmd.length()))
						.subscribe(new Subscriber<Card>() {

							@Override
							public void onCompleted() {
							}

							@Override
							public void onError(Throwable t) {
								System.out.println(t.toString());
							}

							@Override
							public void onNext(Card card) {
								if (card.getError() == null)
									collection.addCard(card);
								else
									System.out.println(card.getError());
							}

						});
			else if (cmd.substring(0, "remove".length()).equalsIgnoreCase(
					"remove"))
				CardFactory.getCard(
						cmd.substring("remove".length(), cmd.length()))
						.subscribe(new Subscriber<Card>() {

							@Override
							public void onCompleted() {
							}

							@Override
							public void onError(Throwable t) {
								System.out.println(t.toString());
							}

							@Override
							public void onNext(Card card) {
								if (card.getError() == null)
									collection.removeCard(card);
								else
									System.out.println(card.getError());
							}

						});
			else if (cmd.length() >= "find blue".length()
					&& cmd.substring(0, "find blue".length()).equalsIgnoreCase(
							"find blue"))
				for (Card c : collection.collection) {
					if (c.getMana_cost().contains("U"))
						System.out.println(c.getName());
				}
			else if (cmd.length() >= "find green".length()
					&& cmd.substring(0, "find green".length())
							.equalsIgnoreCase("find green"))
				for (Card c : collection.collection) {
					if (c.getMana_cost().contains("G"))
						System.out.println(c.getName());
				}
			else if (cmd.length() >= "find red".length()
					&& cmd.substring(0, "find red".length()).equalsIgnoreCase(
							"find red"))
				for (Card c : collection.collection) {
					if (c.getMana_cost().contains("R"))
						System.out.println(c.getName());
				}
			else if (cmd.length() >= "find black".length()
					&& cmd.substring(0, "find black".length())
							.equalsIgnoreCase("find black"))
				for (Card c : collection.collection) {
					if (c.getMana_cost().contains("B"))
						System.out.println(c.getName());
				}
			else if (cmd.length() >= "find white".length()
					&& cmd.substring(0, "find white".length())
							.equalsIgnoreCase("find white"))
				for (Card c : collection.collection) {
					if (c.getMana_cost().contains("W"))
						System.out.println(c.getName());
				}
		}
	}

	public void command() {
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
}
