package selfish.deck;
import java.io.Serializable;
/*
@author Muyaz Rahman
@version 19.0.2
 */

public class Card implements Serializable, Comparable<Card> {
    /**
     * @param name the name of the card
     * @param description the description of the card
     */
    private String name;
    private String description;
    private static final long serialVersionUID = 123456789L;

    /**
     * Constructs a new Card object with the specified name and description.
     * @param name the name of the card
     * @param description the description of the card
     */
    public Card(String name, String description) {
        this.name = name;
        this.description = description;
    }

    /**
     * Returns the description of the card.
     *
     * @return the description of the card
     */
    public String getDescription() {
        return this.description;
    }

    /**
     * Returns a string representation of the Card object.
     *
     * @return a string representation of the Card object
     */
    public String toString() {
        return this.name;
    }

    /**
     * The main method does nothing in this implementation.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        
    }

    /**
     * Compares this Card object with the specified Card object for order based on the alphabetical order of their names.
     * @param other the Card object to be compared
     * @return a negative integer, zero, or a positive integer as this Card object is less than, equal to, or greater than the specified Card object
     */
    @Override
    public int compareTo(Card other) {
        return this.name.compareTo(other.name);
    }
}
