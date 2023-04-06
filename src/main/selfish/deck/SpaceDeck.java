package selfish.deck;
import selfish.GameException;

/*
@author Muyaz Rahman
@version 19.0.2
 */
public class SpaceDeck extends Deck {
    /**
     * Represents an asteroid field environment.
     */
    public static final String ASTEROID_FIELD = "Asteroid field";
    
    /**
     * Represents a blank space environment.
     */
    public static final String BLANK_SPACE = "Blank space";
    
    /**
     * Represents a cosmic radiation environment.
     */
    public static final String COSMIC_RADIATION = "Cosmic radiation";
    
    /**
     * Represents a gravitational anomaly environment.
     */
    public static final String GRAVITATIONAL_ANOMALY = "Gravitational anomaly";
    
    /**
     * Represents a hyperspace environment.
     */
    public static final String HYPERSPACE = "Hyperspace";
    
    /**
     * Represents a meteoroid object.
     */
    public static final String METEOROID = "Meteoroid";
    
    /**
     * Represents a mysterious nebula environment.
     */
    public static final String MYSTERIOUS_NEBULA = "Mysterious nebula";
    
    /**
     * Represents a solar flare environment.
     */
    public static final String SOLAR_FLARE = "Solar flare";
    
    /**
     * Represents a useful junk object.
     */
    public static final String USEFUL_JUNK = "Useful junk";
    
    /**
     * Represents a wormhole environment.
     */
    public static final String WORMHOLE = "Wormhole";
    
    /**
     * A unique serial version ID used for serialization.
     */
    private static final long serialVersionUID = 123456784L;

    /**
     * Creates a new, empty space deck.
     */
    public SpaceDeck() {
        super();
    }
    
    /**
     * Creates a new space deck and loads it with cards from a specified string.
     * 
     * @param str the string containing card data
     */
    public SpaceDeck(String str) throws GameException{
        super();
        add(loadCards(str));
    }
}