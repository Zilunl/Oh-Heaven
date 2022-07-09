package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jcardgame.RowLayout;
import ch.aplu.jgamegrid.GameGrid;

public class AllPlayers extends Player{
    public Player[] players;
    public Hand trick;
    private int winner;
    private Card winningCard;
    private Suit lead;
    private int nbStartCards;
    private int nextPlayer;
    private Suit trumps;
    // Oh_Heaven game;

    int i;

    public AllPlayers(int num) {
        i =0;
        players = new Player[num];
    }

    public void addPlayer(Player curr){
        players[i] = curr;
        i++;
    }

    //use this function to start round
    public void startRound(Oh_Heaven game,int nbStartCards, int nextPlayer, Suit trumps){
        this.nbStartCards = nbStartCards;
        this.nextPlayer = nextPlayer;
        this.trumps = trumps;
        playHand(game);
    }


    @Override
    public Card playHand(Oh_Heaven game) {
        int nbPlayers = players.length;

        for (int i = 0; i < nbStartCards; i++) {
            trick = new Hand(CardFactory.getInstance().getDeck());
            game.trick = trick;
            Card selected = null;
            // if (false) {
            selected = players[nextPlayer].playHand(game);
            System.out.println("Selected is: "+selected.toString());
            // Lead with selected card

            trick.setView(game, new RowLayout(Oh_Heaven.trickLocation, (trick.getNumberOfCards()+2)*Oh_Heaven.trickWidth));
            trick.draw();
            selected.setVerso(false);
            // No restrictions on the card being lead
            lead = (Suit) selected.getSuit();
            game.lead = (Suit)selected.getSuit();
            selected.transfer(trick, true); // transfer to trick (includes graphic effect)
            game.trick = trick;
            winner = nextPlayer;
            winningCard = selected;
            // End Lead
            for (int j = 1; j < nbPlayers; j++) {
                if (++nextPlayer >= nbPlayers) nextPlayer = 0;  // From last back to first
                selected = null;
                selected = players[nextPlayer].playHand(game);
                System.out.println("Selected is: "+selected.toString());
                // if (false) {

                // Follow with selected card
                trick.setView(game, new RowLayout(Oh_Heaven.trickLocation, (trick.getNumberOfCards()+2)*Oh_Heaven.trickWidth));
                trick.draw();
                selected.setVerso(false);  // In case it is upside down
                // Check: Following card must follow suit if possible
                if (selected.getSuit() != lead && players[nextPlayer].hand.getNumberOfCardsWithSuit(lead) > 0) {
                    // Rule violation
                    String violation = "Follow rule broken by player " + nextPlayer + " attempting to play " + selected;
                    System.out.println(violation);
                    if (game.enforceRules)
                        try {
                            throw(new BrokeRuleException(violation));
                        } catch (BrokeRuleException e) {
                            e.printStackTrace();
                            System.out.println("A cheating player spoiled the game!");
                            System.exit(0);
                        }
                }
                // End Check
                selected.transfer(trick, true); // transfer to trick (includes graphic effect)
                game.trick = trick;
                System.out.println("winning: " + winningCard);
                System.out.println(" played: " + selected);
                // System.out.println("winning: suit = " + winningCard.getSuit() + ", rank = " + (13 - winningCard.getRankId()));
                // System.out.println(" played: suit = " +    selected.getSuit() + ", rank = " + (13 -    selected.getRankId()));
                if ( // beat current winner with higher card
                        (selected.getSuit() == winningCard.getSuit() && game.rankGreater(selected, winningCard)) ||
                                // trumped when non-trump was winning
                                (selected.getSuit() == trumps && winningCard.getSuit() != trumps)) {
                    System.out.println("NEW WINNER");
                    winner = nextPlayer;
                    winningCard = selected;
                }
                // End Follow
            }
            Oh_Heaven.delay(600);
            trick.setView(game, new RowLayout(game.hideLocation, 0));
            trick.draw();
            nextPlayer = winner;
            game.setStatusText("Player " + nextPlayer + " wins trick.");
            players[nextPlayer].trick++;
            game.updateScore(nextPlayer);
            game.lead = null;
        }
        return null;
    }
}
