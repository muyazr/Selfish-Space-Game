package selfish;
import selfish.deck.Card;
import selfish.deck.Oxygen;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Arrays;
import java.util.Random;


/*
@author Muyaz Rahman
@version 19.0.2
 */
public class Astronaut implements Serializable {
    /**
     * Represents a player in the game, with a reference to the game engine, a name, lists of actions and oxygens, a track of cards,
     * a flag indicating whether the player is alive, and a unique serial version UID.
     */

    /**
     * The GameEngine the Astronaut belongs to
     */
    private GameEngine game;
    /**
     * Astronaut name
     */
    private String name;
    /**
     * Action cards
     */
    private List<Card> actions;
    /**
     * Oxygen cards
     */
    private List<Oxygen> oxygens;
    /**
     * Current track of the Astronaut
     */
    private Collection<Card> track;
    /**
     * Seriliastion number
     */
    private static final long serialVersionUID = 123456719L;
    /**
     * Boolean if the astronaut is alive
     */
    private boolean isAlive = false;
    
    /**
     * Creates a new Astronaut object with the given name and GameEngine.
     * The Astronaut is initially alive and has empty track, actions, and oxygens lists.
     * @param name the name of the astronaut
     * @param game the GameEngine object that the astronaut is associated with
     */
    public Astronaut(String name, GameEngine game) {
        this.name = name;
        this.game = game;
        this.isAlive = true;
        this.track = new ArrayList<>();
        this.actions = new ArrayList<>();
        this.oxygens = new ArrayList<>();
    }

    /**
     * Returns a string representation of the Astronaut object.
     *
     * @return a string representation of the Astronaut object
     */
    public String toString() {
        if (!this.oxygens.isEmpty()) return this.name;
        else return this.name + " (is dead)";
    }
  
    /**
     *Adds a card to the player's hand.
     *If the card is an instance of Oxygen, adds it to the oxygens list.
     *Otherwise, adds it to the actions list.
     *@param card the card to be added to the hand
     */
    public void addToHand(Card card) {
        if (card instanceof Oxygen) {
            Oxygen oxygenCard = (Oxygen) card;
            this.oxygens.add(oxygenCard);
            this.isAlive = true;
        } else this.actions.add(card);
    }

    /**
     *Adds a new card to the track.
     *@param card the card to be added to the track
     */
    public void addToTrack(Card card) {
        this.track.add(card);
    }

     /**
     * Removes a card from the deck if it exists.
     * @param card the name of the card to remove
     * @return true if the card was found and removed, false otherwise
     */
    public boolean removeCard(String card) {
        for (Card oxygen : this.oxygens) {
            if (oxygen.toString().toLowerCase().trim().equals(card.toLowerCase().trim())) {
                this.oxygens.remove(oxygen);
                return true;
            }
        }

        for (Card action : this.actions) {
            if (action.toString().toLowerCase().trim().equals(card.toLowerCase().trim())) {
                this.actions.remove(action);
                return true;
            }
        }

        return false;
    }

     /**
     * Returns a sorted list of cards in the deck, including both action cards and oxygen cards.
     * @return a sorted list of cards in the deck
     */
    public List<Card> getHand() {
        List<Card> deck = new ArrayList<>(this.actions);

        for (Oxygen oxygen : this.oxygens) deck.add(oxygen);

        Collections.sort(deck);
        return deck;
    }

    /**
     * Gets the astronauts actions
     * @return a sorted list of the current game actions
     */
    public List<Card> getActions() {
        Collections.sort(this.actions);
        return this.actions;
    }

    /**
     * Gets the astronauts distance from ship
     * @return the distance from the ship to the current location on the game track
     */
    public int distanceFromShip() {
        return 6 - this.track.size();
    }

    /**
     * Gets the astronauts track
     * @return a collection of cards currently on the game track
     */
    public Collection<Card> getTrack() {
        return this.track;
    }

    public List<Oxygen> sad(){return this.oxygens;}
    /**
     * @param card the card to count
     * @return the number of cards in the player's hand or actions list that match the specified card
     */
    public int hasCard(String card) {
        String[] oxygenList = getHandStr().split(";")[0].split(", ");
        String[] cardsArray = getActionsStr(false, false).split(", ");
        String[] cards = new String[oxygenList.length + cardsArray.length];
        System.arraycopy(cardsArray, 0, cards, 0, cardsArray.length);
        System.arraycopy(oxygenList, 0, cards, cardsArray.length, oxygenList.length);
       
        int count = 0;

        for (String currentCard : cards) {
            String[] parts = currentCard.split("x ");

            if (parts.length > 1) {
                if (parts[1].trim().toLowerCase().equals(card.toLowerCase().trim())) return count += Integer.parseInt(parts[0]);
            } else if (currentCard.trim().toLowerCase().equals(card.toLowerCase().trim())) return count += 1;
        } 

        return count;
    }
    
    /**
     * Determines whether the astronaut has won the game by checking if their distance from the ship is 0 and they are alive.
     * @return true if the astronaut has won, false otherwise
     */
    public boolean hasWon() {
        if (distanceFromShip() == 0 && isAlive()) return true;
        else return false;
    }

    /**
     * Consumes one unit of oxygen from the astronaut's supply, removing the oxygen card from their hand and adding it to the game's discard pile.
     * If the astronaut has an oxygen card with a value of 2, splits it into two oxygen cards with a value of 1 and adds one back to the astronaut's hand.
     * @return the number of oxygen cards the astronaut has left after the operation
     */
    public int breathe() {
        Oxygen oxygen2 = null;

        for (Oxygen oxygen : this.oxygens) {
            if (oxygen.getValue() == 1) {
                this.oxygens.remove(oxygen);
                game.getGameDiscard().add(oxygen);
                oxygenRemaining();
                return this.oxygens.size();
            } else oxygen2 = oxygen;
        }

        if (oxygen2 != null) {
            Oxygen[] oxygens = game.splitOxygen(oxygen2);

            if (oxygens == null) {oxygenRemaining();return 0;}

            game.getGameDeck().add(oxygen2);
            this.oxygens.remove(oxygen2);
            this.oxygens.add(oxygens[0]);
        }
        oxygenRemaining();
        return this.oxygens.size();
    }

    /**
     * Determines whether the astronaut is alive by checking if they have any oxygen cards and whether they have been killed.
     * @return true if the astronaut is alive, false otherwise
     */
    public boolean isAlive() {
        if (this.oxygens.size() != 0 && this.isAlive) return true;

        if (this.isAlive) {
            this.isAlive = false;

            for (Card card : this.getHand()) game.getGameDiscard().add(card);
            this.actions.clear();
            this.oxygens.clear();
        }
        return false;
    }

    /**
     * Calculates the amount of oxygen remaining in the deck.
     * @return the amount of oxygen remaining
     */
    public int oxygenRemaining() {
        int oxygenRemaining = 0;
        for (Oxygen oxygen : this.oxygens) oxygenRemaining += oxygen.getValue();

        if (oxygenRemaining == 0) {
            if (!game.getCorpses().contains(this)) game.getCorpses().add(this);
            for (Card card : this.actions) {
                game.getGameDiscard().add(card);
            } this.actions.clear();
        }
        return oxygenRemaining;
    }

    /**
     * Swaps the track of this deck with another player's track.
     * @param swapee the player whose track to swap with this deck's track
     */
    public void swapTrack(Astronaut swapee) {
        Collection<Card> swapeeTrack = swapee.track;
        swapee.track = this.track;
        this.track = swapeeTrack;
    }

    /**
     * Checks if this deck has a "Solar flare" card at the top of its track, indicating that the player has melted eyeballs.
     * @return true if the top card of the track is a "Solar flare", false otherwise
     */
    public boolean hasMeltedEyeballs() {
        if (this.track.isEmpty()) return false;

        if (peekAtTrack().toString().trim().equals("Solar flare")) return true;
        else return false;
    }

    /*
     * Returns the track card behind the Astronaut
     * @return the top card of the track, or null if the track is empty
     */
    public Card peekAtTrack() {
        if (!this.track.isEmpty()) {
            Card[] trackArray = this.track.toArray(new Card[0]);
            return trackArray[trackArray.length - 1]; 
        } return null;
    }

    /**
     * Selects a random card from the player's hand to steal and removes it from the hand.
     * If the stolen card is an oxygen, it is removed from the oxygen collection.
     * @return the stolen card
     */
    public Card steal() {
        Random random = new Random();
        Card randomCard = getHand().get(random.nextInt(getHand().size()));

        if (randomCard instanceof Oxygen) {this.oxygens.remove(randomCard); oxygenRemaining();}
        else this.actions.remove(randomCard);
        
        return randomCard;
    }

    /**
     * Hack is used by an enemy AstronautFHAS
     * Hacks the given card, removing it from the appropriate collection.
     * @param card the card to hack
     */
    public void hack(Card card) {
        if (card == null) throw new IllegalArgumentException("Cannot hack null card");

        for (Card oxygen : this.oxygens) {
            if (card.toString().equals(oxygen.toString())) {
                this.oxygens.remove(oxygen);
                oxygenRemaining();
                return;
            }
        }

        for (Card action : this.actions) {
            if (card.toString().equals(action.toString())) {
                this.actions.remove(action);
                return;
            }
        }
    }

    /**
     * Hacks the given card, removing it from the appropriate collection.
     * @param card the card to hack
     */
    public Card hack(String card) {
        if (card == null) throw new IllegalArgumentException("Cannot hack null card");

        for (Card oxygen : this.oxygens) {
            if (card.toLowerCase().trim().equals(oxygen.toString().toLowerCase().trim())) {
                this.oxygens.remove(oxygen);
                oxygenRemaining();
                return oxygen;
            }
        }

        for (Card action : this.actions) {
            if (card.toString().toLowerCase().trim().equals(action.toString().toLowerCase().trim())) {
                this.actions.remove(action);
                return action;
            }
        } throw new IllegalArgumentException("Cannot hack a non existent card");
    }

    /**
     * Attempts to siphon oxygen from the player's oxygen collection. 
     * @return the siphoned oxygen, or null if no oxygen are available to be siphoned
     */
    public Oxygen siphon() {
        Oxygen oxygen2 = null;

        for (Oxygen oxygen : this.oxygens) {
            if (oxygen.getValue() == 1) {
                this.oxygens.remove(oxygen);
                oxygenRemaining();
                return oxygen;
            } else oxygen2 = oxygen;
        }

        if (oxygen2 != null) {
            Oxygen[] oxygens = game.getGameDeck().splitOxygen(oxygen2);

            if (oxygens == null) return null;

            this.oxygens.remove(oxygen2);
            this.oxygens.add(oxygens[0]);
            return oxygens[1];
        }

        return null;
    } 

    /**
     * Removes the top most track card
     * @return the last card on the track, or null if the track is empty
     */
    public Card laserBlast() {
        if (!this.track.isEmpty()) {
            List<Card> ca = (List<Card>) this.track;
            Card cas = ca.get(ca.size() - 1);
            ca.remove(ca.size() - 1);
            this.track = (Collection<Card>) ca;
            return cas;
        } throw new IllegalArgumentException("Cannot laser blast an Astronaut that has not moved");
    }

    /**
     * Gets the astronauts action cards and returns them in a string format
     * 
     * @return a string representation of all action cards
     */
    public String getActionsStr(boolean enumerated, boolean excludeShields) {
        if (this.actions.isEmpty()) return "";

        Collections.sort(this.actions);
        int noCards = 0;
        String currentCard = "";
        String actionString = "";
        char enumerateLetter = 'A';
        
        for (Card card : this.actions) {

            if (currentCard.equals("")) {
                currentCard = card.toString();
                noCards++;
                continue;
            }

            if (card.toString().equals(currentCard)) {
                noCards++;
                continue;
            } else {
                if (currentCard.equals("Shield") && excludeShields);
                else if (enumerated) {
                    actionString += String.format("[%s] %s, ", enumerateLetter, currentCard);
                    enumerateLetter++;
                }
                else if (noCards > 1) actionString += String.format("%dx %s, ", noCards, currentCard);
                else actionString += String.format("%s, ", currentCard);
                currentCard = card.toString();
                noCards = 1;
            }
            
        } 
        
        if (currentCard.equals("Shield") && excludeShields) return actionString.substring(0, actionString.length() - 2);
        else if (enumerated) actionString += String.format("[%s] %s", enumerateLetter, currentCard);
        else if (noCards > 1) actionString += String.format("%dx %s", noCards, currentCard);
        else actionString += String.format("%s", currentCard);

        return actionString;
    }

    /**
     * Gets the astronauts hand and returns a string format
     * @return A string representation of all cards in the players deck
     */
    public String getHandStr() {
        if (this.actions.size() == 0 && this.oxygens.size() == 0) return "Dead astronauts hold nothing in their hands";
        else if (this.oxygens.size() == 0) return getActionsStr(false, false);

        Collections.sort(this.oxygens);
        String currentCard = "";
        String oxygenString = "";
        int noOxygens = 0;
        for (int i = this.oxygens.size() - 1; i >= 0; i--) {
            if (i == this.oxygens.size() - 1) {
                noOxygens++;
                currentCard = this.oxygens.get(i).toString();
                continue;
            }
            
            if (this.oxygens.get(i).toString().equals(currentCard)) {
                noOxygens++;
                continue; 
            }
            else {
                if (noOxygens > 1) oxygenString += String.format("%dx %s, ", noOxygens, currentCard);
                else oxygenString += String.format("%s, ", currentCard);
                currentCard = this.oxygens.get(i).toString();
                noOxygens = 1;
            }   
        }

        if (noOxygens > 1) oxygenString += String.format("%dx %s; ", noOxygens, currentCard);
        else oxygenString += String.format("%s; ", currentCard);

        if (!this.actions.isEmpty()) return oxygenString + getActionsStr(false, false);
        else return oxygenString.substring(0, oxygenString.length() - 2);
    }
}
