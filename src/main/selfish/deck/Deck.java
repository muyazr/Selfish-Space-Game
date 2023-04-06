package selfish.deck;
import java.util.Collection;
import java.util.Collections;
import java.io.Serializable;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import selfish.GameException;

/*
@author Muyaz Rahman
@version 19.0.2
 */

abstract public class Deck implements Serializable {
    /**
     * The serial version UID for this class.
     * The collection of cards in this deck.
     */
    private static final long serialVersionUID = 123456781L;
    
    private Collection<Card> cards;

    /**
     * Constructor for the Deck class.
     * Initialises this.cards to an empty ArrayList
     */
    protected Deck() {
        this.cards = new ArrayList<Card>(); 
    }

    /**
     * The main method is not used in the Deck class.
     * @param args The command-line arguments passed to the main method.
     */
    public static void main(String[] args)  {
        
    }

    /**
     * Returns the collection of cards in this deck.
     * 
     * @return the collection of cards
     */
    public Collection<Card> getDeck() {
        return this.cards;
    }

    /**
     * Checks if this deck contains a specified card.
     * 
     * @param card the card to check for
     * @return true if the deck contains the card, false otherwise
     */
    public boolean contains(Card card) {
        if (this.cards.contains(card)) return true;
        else return false;
    }

    /**
     * Add a card to the deck.
     * @param card The card to be added to the deck.
     * @return The number of cards in the deck after the addition.
     */
    public int add(Card card) {
        this.cards.add(card);
        return this.cards.size();
    }

    /**
     * Add a list of cards to the deck.
     * 
     * @param cards The list of cards to be added to the deck.
     * @return The number of cards in the deck after the addition.
     */
    protected int add(List<Card> cards) {
        for (Card card : cards) {
            this.cards.add(card);
        }

        return this.cards.size();
    }

    public int hasCard(String targetCard) {
        int noCards = 0;
        for (Card card : this.cards) if (card.toString().toLowerCase().equals(targetCard.toLowerCase().trim())) noCards += 1;
        return noCards;
    }

    /**
     * Remove a card from the deck.
     * 
     * @param card The card to be removed from the deck.
     */
    public void remove(Card card) {
        if (this.cards.contains(card)) {
            this.cards.remove(card);
        } 
    }

    /**
     * Draw a card from the deck.
     * 
     * @return The last card in the deck.
     */
    public Card draw() {
        if (this.cards.size() == 0) throw new IllegalStateException("Cannot draw from empty deck");
        ArrayList<Card> cardList = (ArrayList<Card>) this.cards;
        Card lastCard = cardList.remove(cardList.size() - 1);
        return lastCard;
    }

    /**
     * Shuffle the deck.
     * @param random The random number generator used for shuffling.
     */
    public void shuffle(Random random) {

        List<Card> shuffledCards = (List<Card>) this.cards; // create a new shuffled list
        Collections.shuffle(shuffledCards, random); // shuffle the new list
        this.cards = (ArrayList<Card>) shuffledCards; // assign the shuffled list back to this.cards
    }

    /**
     * Get the number of cards in the deck.
     * @return The number of cards in the deck.
     */
    public int size() {
        return this.cards.size();
    }

    /**
     * Load a list of cards from a file.
     * @param path The path of the file containing the list of cards.
     * @return The list of cards loaded from the file.
     */
    protected static List<Card> loadCards(String path) throws GameException {
        List<Card> cardList = new ArrayList<Card>();
        try {
            File file = new File(path);
            Scanner scanner = new Scanner(file);

            // skip the first line
            if (scanner.hasNextLine()) {
                scanner.nextLine();
            }
            
            // read and print each subsequent line
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                Card[] cards = stringToCards(line);
                for (Card card : cards) {
                    cardList.add(card);
                }
            }
            scanner.close();
            
        } catch (Exception e) {
            throw new GameException("File not found: ", e);
            }

        return cardList;
    }

    /**
     * Converts a string representing one or more cards into an array of Card objects.
     * @param str the string to be parsed, formatted as "name;description;quantity"
     * @return an array of Card objects, where each card has the specified name and description
     * and there are the specified number of cards in the array
     */
    protected static Card[] stringToCards(String str) {
        String[] parts = str.split(";");
        String name = parts[0].trim();
        String description = parts[1].trim();
        int quantity = Integer.parseInt(parts[2].trim());
        Card[] cards = new Card[quantity];
        for (int i = 0; i < quantity; i++) {
            cards[i] = new Card(name, description);
        }
        return cards;
    }
}