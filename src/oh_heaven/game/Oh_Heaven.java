package oh_heaven.game;

// Oh_Heaven.java

import ch.aplu.jcardgame.*;
import ch.aplu.jgamegrid.*;
import ch.aplu.jcardgame.Card;
import java.awt.Color;
import java.awt.Font;
import java.util.*;
import java.util.stream.Collectors;
import java.util.Properties;

@SuppressWarnings("serial")
public class Oh_Heaven extends CardGame {

  final String trumpImage[] = {"bigspade.gif","bigheart.gif","bigdiamond.gif","bigclub.gif"};

  private Randomizer random;

  public static boolean rankGreater(Card card1, Card card2) {
	  return card1.getRankId() < card2.getRankId(); // Warning: Reverse rank order of cards (see comment on enum)
  }
	 
  private final String version = "1.0";
  public final int nbPlayers = 4;
  public final int nbStartCards;
  public final int nbRounds;
  public final int madeBidBonus = 10;
  private final int handWidth = 400;
  public static final int trickWidth = 40;
  //private final Deck deck = new Deck(Suit.values(), Rank.values(), "cover");
  private AllPlayers aPlayers;
  private Player[] players;
  private final Location[] handLocations = {
			  new Location(350, 625),
			  new Location(75, 350),
			  new Location(350, 75),
			  new Location(625, 350)
	  };
  private final Location[] scoreLocations = {
			  new Location(575, 675),
			  new Location(25, 575),
			  new Location(575, 25),
			  // new Location(650, 575)
			  new Location(575, 575)
	  };
  private Actor[] scoreActors = {null, null, null, null };
  public static final Location trickLocation = new Location(350, 350);
  private final Location textLocation = new Location(350, 450);
  public final int thinkingTime = 2000;

  public Location hideLocation = new Location(-500, - 500);
  private Location trumpsActorLocation = new Location(50, 50);
  public boolean enforceRules;

  public void setStatus(String string) { setStatusText(string); }
	public CardFactory card_factory;
  public Suit lead;
  public Hand trick;

Font bigFont = new Font("Serif", Font.BOLD, 36);

private void initScore() {
	 for (int i = 0; i < nbPlayers; i++) {
		 // scores[i] = 0;
		 String text = "[" + String.valueOf(players[i].score) + "]" + String.valueOf(players[i].trick) + "/" + String.valueOf(players[i].bid);
		 scoreActors[i] = new TextActor(text, Color.WHITE, bgColor, bigFont);
		 addActor(scoreActors[i], scoreLocations[i]);
	 }
  }

public void updateScore(int player) {
	removeActor(scoreActors[player]);
	String text = "[" + String.valueOf(players[player].score) + "]" + String.valueOf(players[player].trick) + "/" + String.valueOf(players[player].bid);
	scoreActors[player] = new TextActor(text, Color.WHITE, bgColor, bigFont);
	addActor(scoreActors[player], scoreLocations[player]);
}

private void initScores() {
	 for (int i = 0; i < nbPlayers; i++) {
	 	players[i].score = 0;
	 }
}

private void updateScores() {
	 for (int i = 0; i < nbPlayers; i++) {
		 players[i].score += players[i].trick;
		 if (players[i].trick == players[i].bid) players[i].score += madeBidBonus;
	 }
}

private void initTricks() {
	 for (int i = 0; i < nbPlayers; i++) {
	 	players[i].trick = 0;
	 }
}


private void initBids(Suit trumps, int nextPlayer) {
	int total = 0;
	for (int i = nextPlayer; i < nextPlayer + nbPlayers; i++) {
		 int iP = i % nbPlayers;

		players[iP].bid = nbStartCards / 4 + random.getRandom().nextInt(2);
		 total += players[iP].bid;
	 }
	 if (total == nbStartCards) {  // Force last bid so not every bid possible
		 int iP = (nextPlayer + nbPlayers) % nbPlayers;
		 if (players[iP].bid == 0) {
			 players[iP].bid = 1;
		 } else {
			 players[iP].bid += random.getRandom().nextBoolean() ? -1 : 1;
		 }
	 }
 }

private void initRound() {

		for (int i = 0; i < nbPlayers; i++) {
				players[i].hand = new Hand(card_factory.getDeck());
		}
		card_factory.dealingOut(players, nbPlayers, nbStartCards);
		 for (int i = 0; i < nbPlayers; i++) {
			 players[i].hand.sort(Hand.SortType.SUITPRIORITY, true);
		 }
	    RowLayout[] layouts = new RowLayout[nbPlayers];
	    for (int i = 0; i < nbPlayers; i++) {
	      layouts[i] = new RowLayout(handLocations[i], handWidth);
	      layouts[i].setRotationAngle(90 * i);
	      // layouts[i].setStepDelay(10);
			players[i].hand.setView(this, layouts[i]);
			players[i].hand.setTargetArea(new TargetArea(trickLocation));
			players[i].hand.draw();
	    }
 }

private void playRound() {
	// Select and display trump suit
	final Suit trumps = random.randomEnum(Suit.class);
	final Actor trumpsActor = new Actor("sprites/" + trumpImage[trumps.ordinal()]);
	addActor(trumpsActor, trumpsActorLocation);
	int nextPlayer = random.getRandom().nextInt(nbPlayers); // randomly select player to lead for this round
	initBids(trumps, nextPlayer);
	for (int i = 0; i < nbPlayers; i++) updateScore(i);
	aPlayers.startRound(this,nbStartCards,nextPlayer,trumps);
	// initScore();
	removeActor(trumpsActor);
}

  public Oh_Heaven(Properties properties)
  {
	super(700, 700, 30);
    setTitle("Oh_Heaven (V" + version + ") Constructed for UofM SWEN30006 with JGameGrid (www.aplu.ch)");
    setStatusText("Initializing...");
    this.nbStartCards = PropertiesLoader.loadNbStartCards();
	this.nbRounds = PropertiesLoader.loadRounds();
	this.enforceRules = PropertiesLoader.loadEnforeRules();
	aPlayers = PropertiesLoader.loadAPlayer(nbPlayers);
    players = aPlayers.players;
    card_factory = CardFactory.getInstance();
    random = Randomizer.getInstance();
    initScores();
    initScore();
    for (int i=0; i <nbRounds; i++) {
      initTricks();
      initRound();
      playRound();
      updateScores();
    };
    for (int i=0; i <nbPlayers; i++) updateScore(i);
    int maxScore = 0;
    for (int i = 0; i <nbPlayers; i++) if (players[i].score > maxScore) maxScore = players[i].score;
    Set <Integer> winners = new HashSet<Integer>();
    for (int i = 0; i <nbPlayers; i++) if (players[i].score == maxScore) winners.add(i);
    String winText;
    if (winners.size() == 1) {
    	winText = "Game over. Winner is player: " +
    			winners.iterator().next();
    }
    else {
    	winText = "Game Over. Drawn winners are players: " +
    			String.join(", ", winners.stream().map(String::valueOf).collect(Collectors.toSet()));
    }
    addActor(new Actor("sprites/gameover.gif"), textLocation);
    setStatusText(winText);
    refresh();
  }

  public static void main(String[] args)
  {
	// System.out.println("Working Directory = " + System.getProperty("user.dir"));
	//final Properties properties;
	PropertiesLoader propertiesLoader;
	if (args == null || args.length == 0) {
		propertiesLoader = new PropertiesLoader(null);
	  //properties = PropertiesLoader.loadPropertiesFile(null);
	} else {
		propertiesLoader = new PropertiesLoader(args[0]);
	    //properties = PropertiesLoader.loadPropertiesFile(args[0]);
	}
	//this.nbStartCards = Integer.parseInt(properties.getProperty("nbStartCards"));

    new Oh_Heaven(PropertiesLoader.properties);
  }

}
