package oh_heaven.game;

import ch.aplu.jcardgame.Card;

public abstract class NPC extends Player {

    public abstract void think(Oh_Heaven game);

    @Override
    public Card playHand(Oh_Heaven game) {
        Oh_Heaven.delay(game.thinkingTime);
        think(game);
        return selected;
    }
}
