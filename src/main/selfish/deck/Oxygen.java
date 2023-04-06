package selfish.deck;
/*
@author Muyaz Rahman
@version 19.0.2
 */
public class Oxygen extends Card {
    private int value; // Value of oxygen card
    private static final long serialVersionUID = 223456789L; // Serialisation number

    /**
     * Constructs a new Oxygen object with the specified value.
     *
     * @param value the value of the Oxygen card
     */
    public Oxygen(int value) {
        super(String.format("Oxygen(%d)", value), "You need this to breathe.");
        this.value = value;
    }

    /**
     * Returns the value of the Oxygen card.
     *
     * @return the value of the Oxygen card
     */
    public int getValue() {
        return this.value;
    }

    /**
     * Returns a string representation of the Oxygen card.
    * @return a string representation of the Oxygen card
    */
    public String toString() {
        return String.format("Oxygen(%d)", this.value);
    }

    /**
     * Compares this Oxygen card with the specified Oxygen card for order.
     * Returns a negative integer, zero, or a positive integer as this card
     * is less than, equal to, or greater than the specified card.
     * @param other the Oxygen card to be compared
     * @return a negative integer, zero, or a positive integer as this card
     * is less than, equal to, or greater than the specified card
     */
    
    public int compareTo(Oxygen other) {
        return this.value - other.value; // sort in descending order
    }
}
