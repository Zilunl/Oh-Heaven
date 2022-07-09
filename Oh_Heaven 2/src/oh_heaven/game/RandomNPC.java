package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

public class RandomNPC extends NPC{

    protected Card getRandomCard(Hand hand){
        return Randomizer.getInstance().randomCard(hand);
    }

    @Override
    public void think(Oh_Heaven game) {
        selected = getRandomCard(hand);
    }
}
