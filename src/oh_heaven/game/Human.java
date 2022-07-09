package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.CardAdapter;
import ch.aplu.jcardgame.CardListener;
import ch.aplu.jcardgame.Hand;
import ch.aplu.jgamegrid.GameGrid;

public class Human extends Player{

    CardListener cardListener = new CardAdapter()  // Human Player plays card
    {
        public void leftDoubleClicked(Card card) {
            selected = card;
            hand.setTouchEnabled(false);
        }
    };

    @Override
    public Card playHand(Oh_Heaven game) {
        selected = null;
        hand.addCardListener(cardListener);
        hand.setTouchEnabled(true);
        while (null == selected) Oh_Heaven.delay(100);
        return selected;
    }
}
