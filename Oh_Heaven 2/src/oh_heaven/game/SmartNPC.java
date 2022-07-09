package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.ArrayList;

public class SmartNPC extends LegalNPC{

    //Card smallest;
    Card played;
    Card has;


    protected Card findSmallestLegal(Oh_Heaven game){
        ArrayList<Card> cards = getLegalCards(game);
        if(cards.size()>0) {
            Card smallest = cards.get(0);
            for (int i = 1; i < cards.size(); i++) {
                if (Oh_Heaven.rankGreater(smallest, cards.get(i))) {
                    smallest = cards.get(i);
                }
            }
            return smallest;
        }
        return playLegal(game);
    }

    protected Card findLargestLegal(Oh_Heaven game){
        ArrayList<Card> cards = getLegalCards(game);
        if(cards.size()>0){
            Card largest = cards.get(0);
            for (int i=1;i<cards.size();i++){
                if(Oh_Heaven.rankGreater(cards.get(i),largest)){
                    largest = cards.get(i);
                }
            }
            return largest;
        }
        return playLegal(game);
    }

    protected ArrayList<Card> getLegalTrick(Oh_Heaven game, Hand trick){
        return trick.getCardsWithSuit(game.lead);
    }

    protected Card findLargestTrick(Oh_Heaven game, Hand trick){
        ArrayList<Card> legalTricks = getLegalTrick(game,trick);
        Card largest = legalTricks.get(0);
        if(legalTricks.size()>1){
            for (int i=1;i<legalTricks.size();i++){
                if(Oh_Heaven.rankGreater(legalTricks.get(i),largest)){
                    largest = legalTricks.get(i);
                }
            }
        }
        return largest;
    }

    protected Card playSmart(Oh_Heaven game){
        if(this.trick == this.bid){
            //try not to win
            return findSmallestLegal(game);
        }else{
            //try to win
            if(game.trick.getNumberOfCards()>0){
                played = findLargestTrick(game,game.trick);
                has = findLargestLegal(game);
                if(Oh_Heaven.rankGreater(played,has)){
                    return findSmallestLegal(game);
                }else{
                    return has;
                }
            }
            return playLegal(game);
        }
    }

    public void think(Oh_Heaven game){
        this.selected = playSmart(game);
    }

}
