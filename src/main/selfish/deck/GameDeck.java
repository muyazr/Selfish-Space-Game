package selfish.deck;

import selfish.GameException;

/*
@author Muyaz Rahman
@version 19.0.2
 */
public class GameDeck extends Deck{
    /**
     * Represents a hack suit item.
     */
    public static final String HACK_SUIT = "Hack suit";
    
    /**
     * Represents a hole in a space suit event.
     */
    public static final String HOLE_IN_SUIT = "Hole in suit";
    
    /**
     * Represents a laser blast event.
     */
    public static final String LASER_BLAST = "Laser blast";
    
    /**
     * Represents an oxygen item.
     */
    public static final String OXYGEN = "Oxygen";
    
    /**
     * Represents an oxygen item with a level of 1.
     */
    public static final String OXYGEN_1 = "Oxygen(1)";
    
    /**
     * Represents an oxygen item with a level of 2.
     */
    public static final String OXYGEN_2 = "Oxygen(2)";
    
    /**
     * Represents an oxygen siphon item.
     */
    public static final String OXYGEN_SIPHON = "Oxygen siphon";
    
    /**
     * Represents a rocket booster item.
     */
    public static final String ROCKET_BOOSTER = "Rocket booster";
    
    /**
     * Represents a shield item.
     */
    public static final String SHIELD = "Shield";
    
    /**
     * Represents a tether item.
     */
    public static final String TETHER = "Tether";
    
    /**
     * Represents a tractor beam item.
     */
    public static final String TRACTOR_BEAM = "Tractor beam";
    
    /**
     * A unique serial version ID used for serialization.
     */
    private static final long serialVersionUID = 123356789L;

    /**
     * Constructs a new GameDeck object.
     */
    public GameDeck() {
        super();
    }

    /**
     * Constructs a new GameDeck object with a specified string of cards.
     * @param str the string of cards to load into the deck
     */
    public GameDeck(String str) throws GameException {
        super();
        add(loadCards(str));
        addInitialOxygenCards();
    }

    /**
     * Draws an oxygen card with the specified value from the deck.
     * @param value the value of the oxygen card to draw
     * @return the drawn oxygen card, or null if not found
     */
    public Oxygen drawOxygen(int value) {
        for (Card card : getDeck()){
            if (card.toString().equals(String.format("Oxygen(%d)", value))) {
                remove(card);
                Oxygen oxygenCard = (Oxygen) card;
                return oxygenCard;
            }
        } throw new IllegalStateException("No oxygens to draw");
    }

    /**
     * Splits a double oxygen card into two single oxygen cards.
     * @param dbl the double oxygen card to split
     * @return an array of the two single oxygen cards, or null if unable to split
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if (dbl.getValue() == 1) throw new IllegalArgumentException("Cannot split a value one oxygen");

        int noOxygens = 0;
        Oxygen[] oxygens = new Oxygen[2];
        Card singleOxygen[] = new Oxygen[2];

        for (Card card : getDeck()){
            if (card.toString().equals(OXYGEN_1)) {
                singleOxygen[noOxygens] = card;
                noOxygens++;
                if (noOxygens >= 2) {
                    noOxygens = 0;
                    add(dbl);
                    for (Card oxygenCard : singleOxygen) {
                        remove(oxygenCard);
                        Oxygen oxygenReturn = (Oxygen) oxygenCard;
                        oxygens[noOxygens] = oxygenReturn;
                        noOxygens++;
                    }
                    return oxygens;
                }
            }
        } 
        
        throw new IllegalStateException("No oxygens to swap");

    }

    /**
     * The addInitialOxygenCards method adds 10 Oxygen cards with a value of 2 and 38 Oxygen cards with value 1
     */
    public void addInitialOxygenCards() {
        for (int i = 0; i < 10; i++) {
            Card oxygen = new Oxygen(2);
            add(oxygen);
        }

        for (int i = 0; i < 38; i++) {
            Card oxygen = new Oxygen(1);
            add(oxygen);
        }
    }
}