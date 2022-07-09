package oh_heaven.game;

import ch.aplu.jcardgame.Card;
import ch.aplu.jcardgame.Hand;

import java.util.Random;

public class Randomizer {
    private static Randomizer randomrizer = null;
    private final int seed;
    private final Random random;


    public Randomizer() {
        seed = readSeed();
        random = new Random(seed);
    }

    private int readSeed(){
        return PropertiesLoader.loadSeed();
    }

    public Random getRandom() {
        return random;
    }

    public <T extends Enum<?>> T randomEnum(Class<T> clazz){
        int x = random.nextInt(clazz.getEnumConstants().length);
        return clazz.getEnumConstants()[x];
    }

    public Card randomCard(Hand hand){
        int x = random.nextInt(hand.getNumberOfCards());
        return hand.get(x);
    }

    public static Randomizer getInstance(){
        if(randomrizer == null){
            randomrizer = new Randomizer();
        }
        return randomrizer;
    }
}
