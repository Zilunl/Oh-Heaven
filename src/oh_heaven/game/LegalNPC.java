package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class LegalNPC extends RandomNPC{

    protected ArrayList<Card> getLegalCards(Oh_Heaven game){
        return hand.getCardsWithSuit(game.lead);
    }

    public Card playLegal(Oh_Heaven game){
        ArrayList<Card> result;
        if (game.lead != null) {
            Oh_Heaven.delay(game.thinkingTime);
            result = getLegalCards(game);
            if (result.size()>0) {
                return result.get(0);
            }
        }
        return getRandomCard(hand);
    }


    @Override
    public void think(Oh_Heaven game) {
        selected = playLegal(game);
    }
}
