package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Deck;
import ch.aplu.jcardgame.Hand;

public class CardFactory {

    private static CardFactory CardFactory_instance = null;
    private Deck deck;

    public CardFactory() {
        deck = new Deck(Suit.values(), Rank.values(), "cover");
    }

    public Deck getDeck() {
        return deck;
    }

    public void dealingOut(Player[] players, int nbPlayers, int nbCardsPerPlayer) {
        Hand pack = deck.toHand(false);
        for (int i = 0; i < nbCardsPerPlayer; i++) {
            for (int j=0; j < nbPlayers; j++) {
                if (pack.isEmpty()) return;
                Card dealt = Randomizer.getInstance().randomCard(pack);
                dealt.removeFromHand(false);
                players[j].hand.insert(dealt, false);
            }
        }
    }

    public static CardFactory getInstance(){
        if(CardFactory_instance == null){
            CardFactory_instance = new CardFactory();
        }
        return CardFactory_instance;
    }
}
