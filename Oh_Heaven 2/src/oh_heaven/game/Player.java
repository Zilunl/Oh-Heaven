package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public abstract class Player {

    public Hand hand;
    public int score;
    public int trick;
    public int bid;
    protected Card selected;

    public abstract Card playHand(Oh_Heaven game);
}
