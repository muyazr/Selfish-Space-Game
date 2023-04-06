package selfish;
import java.io.Serializable;
import java.util.Collection;
import java.util.Random;

import selfish.deck.Deck;
import selfish.deck.Card;
import selfish.deck.Oxygen;
import selfish.deck.GameDeck;
import selfish.deck.SpaceDeck;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;


/*
@author Muyaz Rahman
@version 19.0.2
 */
public class GameEngine implements Serializable {
    /*
     * Checks if turn started
     */
    private boolean turnStarted;
    /*
     * Collection of current Astronauts who are alive
     */
    private Collection<Astronaut> activePlayers; 
    /*
     * Collection of current Astronauts who are dead
     */
    private List<Astronaut> corpses; 
    /*
     * Current astronaut taking their turn
     */
    private Astronaut currentPlayer;
    /*
     * Boolean if game has started
     */
    private boolean hasStarted;
    /*
     * // A random number generator
     */
    private Random random;
    /*
     * The deck of cards for the game
     */
    private GameDeck gameDeck; 
    /*
     * // The pile of discarded cards for the game
     */
    private GameDeck gameDiscard;
    /*
     * The deck of cards for the space
     */
    private SpaceDeck spaceDeck;
    /*
     * The pile of discarded cards for the space
     */
    private SpaceDeck spaceDiscard; // The pile of discarded cards for the space
    /*
     * The serialVersionUID
     */
    private static final long serialVersionUID = 123456789L; 

    
    /**
     * Constructs a new private `GameEngine` instance.
     * Only used for testing
     */
    private GameEngine() {

    }

    /**
     * Constructs a new `GameEngine` instance with the specified seed and deck filenames.
     * @param seed      the seed to use for the random number generator
     * @param gameDeck  the filename of the game deck
     * @param spaceDeck the filename of the space deck
     */
    public GameEngine(long seed, String gameDeck, String spaceDeck) throws GameException {
        this.random = new Random(seed); // Creates a random object
        this.gameDeck = new GameDeck(gameDeck); // Creates a gamedeck
        this.gameDiscard = new GameDeck(); // Creates a gamedeck for a discard pile
        this.spaceDeck = new SpaceDeck(spaceDeck); // Creates a spacedeck
        this.spaceDiscard = new SpaceDeck(); // Creates a spacedeck for a discard pile
        this.activePlayers = new ArrayList<>(); //Array for all active players
        this.corpses = new ArrayList<>(); //Array for all dead players
        this.turnStarted = false;

        this.gameDeck.shuffle(random);
        this.spaceDeck.shuffle(random);
    }


    /**
     * Main method for the GameEngine class.
     * Only used for testing
     */
    public static void main(String[] args) throws GameException {
        GameEngine engine = new GameEngine(16412, "../../io/ActionCards.txt", "../../io/SpaceCards.txt");
        engine.addPlayer("s"); engine.addPlayer("sd"); engine.addPlayer("sa");
        engine.startGame();
        for (int i=0; i<12; ++i) {
    		engine.startTurn();
			while ((i==7 || i==8) && engine.getCurrentPlayer().breathe() > 0);
			while (i%3 == 0 && engine.getCurrentPlayer().sad().size() < 5) engine.getCurrentPlayer().sad().add(new Oxygen(2));
			if (i<5 || i>8) engine.travel( engine.getCurrentPlayer());
		    engine.endTurn();
		}

		engine.startTurn();
        System.out.println(engine.currentPlayer.getHandStr());
        System.out.println(engine.currentPlayer.getTrack());
        while (engine.currentPlayer.breathe() > 2); engine.travel(engine.currentPlayer);
		

        
    }   

/**
 * Checks if turn already started
 * Starts the turn, moving the first player in the list to the end and making them the currentPlayer.
 * @throws IllegalStateException
 */
    public void startTurn() {
        if (this.turnStarted) throw new IllegalStateException("Cannot start turn");
        if (gameOver()) throw new IllegalStateException("Game is over cannot start new turn");
        if (!hasStarted) throw new IllegalStateException("Game has not started");

        Astronaut[] player = this.activePlayers.toArray(new Astronaut[0]);
        Astronaut nextPlayer = player[0];
        this.activePlayers.remove(nextPlayer);
        currentPlayer = nextPlayer;
        this.turnStarted = true;
    }

     /**
     * Ends the turn, resetting the current player to null and returning the number of remaining players.
     * 
     * @return the number of remaining players
     */
    public int endTurn() {
        this.turnStarted = false;
        if (currentPlayer.isAlive()) this.activePlayers.add(currentPlayer);
        else if (!this.corpses.contains(currentPlayer)) this.corpses.add(currentPlayer);
        currentPlayer = null;
        return this.activePlayers.size();
    }
    /**
     * Adds a new astronaut to the active players collection.
     * @param player the name of the astronaut to add
     * @return the number of players in the active players collection after the new astronaut has been added
     */
    public int addPlayer(String player) {
        if (hasStarted) throw new IllegalStateException("Cannot add players after game has started");
        if (this.activePlayers.size() == 5) throw new IllegalStateException("Cannot add more than five players");

        Astronaut astronaut = new Astronaut(player.trim(), this);
        this.activePlayers.add(astronaut);
        return this.activePlayers.size();
    }

    /**
     * Starts the space game, setting the 'hasStarted' flag to true, selecting the first player, and dealing cards to all players.
     */
    public void startGame() {
        if (this.activePlayers.size() == 1) throw new IllegalStateException("Cannot start game with one player");
        if (this.activePlayers.size() > 5) throw new IllegalStateException("Cannot start game wth more than five players");
        if (this.hasStarted) throw new IllegalStateException("Cannot start game again");

        this.hasStarted = true;
        dealCards();
    }

    /**
     * Returns a list of all players, including both active players and corpses.
     * @return a List of Astronaut objects representing all players
     */ 
    public List<Astronaut> getAllPlayers() {
        List<Astronaut> allPlayers = new ArrayList<>(this.activePlayers);
        allPlayers.addAll(this.corpses);
        if (this.currentPlayer != null && !this.corpses.contains(this.currentPlayer)) allPlayers.add(currentPlayer);
        
        return allPlayers;
    }

    public List<Astronaut> getCorpses() {
        return this.corpses;
    }

    /**
     * Merges two decks by adding all the cards from 'deck2' to 'deck1', and then clearing 'deck2'.
     * @param deck1 the destination deck
     * @param deck2 the source deck
     */
    public void mergeDecks(Deck deck1, Deck deck2) {
        for (Card card : deck2.getDeck()) deck1.add(card);
        deck2.getDeck().clear();
    }

    /**
     * Returns the total number of players, including both active players and corpses.
     * @return an integer representing the total number of players
     */ 
    public int getFullPlayerCount() {
        List<Astronaut> allPlayers = new ArrayList<>(this.activePlayers);
        allPlayers.addAll(this.corpses);
        if (this.currentPlayer != null && !this.corpses.contains(this.currentPlayer)) allPlayers.add(this.currentPlayer);
        
        return allPlayers.size();
    }

    /**
     * Returns the current player.
     * @return the current player
     */
    public Astronaut getCurrentPlayer() {
        return this.currentPlayer;
    }

    /**
     * Removes a player from the active players list and adds them to the corpses list.
     * @param corpse the player to be removed and added to the corpses list
     */
    public void killPlayer(Astronaut corpse) {
        activePlayers.remove(corpse);
        this.corpses.add(corpse);
    }

    public boolean checkIfAlive(Astronaut astronaut) {
        if (!astronaut.isAlive()) {
            this.activePlayers.remove(astronaut);
            return false;
        } return true;
    }

    /**
     * Causes an astronaut to travel and draw a card from the space deck. If the astronaut has no oxygen(2) card,
     * they cannot travel and this method returns null.
     * @param traveller the astronaut that is travelling
     * @return the card drawn from the space deck
     */
    public Card travel(Astronaut traveller) {
        if (!checkIfAlive(traveller)) throw new IllegalStateException("Cannot travel with no oxygen");
        if (traveller.hasCard(GameDeck.OXYGEN_2) != 0) traveller.removeCard(GameDeck.OXYGEN_2);
        else if (traveller.oxygenRemaining() >= 2) for (int i = 0; i < 2; i++) traveller.removeCard(GameDeck.OXYGEN_1);
        else throw new IllegalStateException("Cannot travel with one oxygen");

        Card spaceCard = spaceDeck.draw();
        
        if (!spaceCard.toString().equals(SpaceDeck.GRAVITATIONAL_ANOMALY)) traveller.addToTrack(spaceCard);
        checkIfAlive(traveller);
        gameOver();

        return spaceCard;
    }
    
    /**
     * Loads the game state from the specified file path.
     * @param path the file path to load the game state from
     * @return the loaded `GameEngine` instance
     */
    public static GameEngine loadState(String path) throws GameException {
        try (ObjectInputStream inputs = new ObjectInputStream(new FileInputStream(path))) {
            return (GameEngine) inputs.readObject();
        } catch (Exception e) {
            throw new GameException("Error saving game state to file: " + path, e);
        }
    }

    /**
     * Saves the game state to the specified file path.
    * @param path the file path to save the game state to
    */
    public void saveState(String path) {
        try (ObjectOutputStream outputs = new ObjectOutputStream(new FileOutputStream(path))) {
            outputs.writeObject(this);
        } catch (IOException e) {
            new GameException("Error saving game state to file: " + path, e);
        }
    }

    /**
     * Splits an Oxygen card from either the game deck or the game discard pile.
     * @param dbl the Oxygen card to be split
     * @return an array of Oxygen cards split from the game deck or discard pile, or null if no Oxygen cards can be split
     */
    public Oxygen[] splitOxygen(Oxygen dbl) {
        if (gameDeck.hasCard(GameDeck.OXYGEN_1) >= 2) {
            Oxygen gameDeckSplit[] = gameDeck.splitOxygen(dbl);
            if (gameDeckSplit != null) return gameDeckSplit;
        } else if (gameDiscard.hasCard(GameDeck.OXYGEN_1) >= 2) {
            Oxygen gameDiscardSplit[] = gameDiscard.splitOxygen(dbl);
            if (gameDiscardSplit != null) return gameDiscardSplit;
        }

        Oxygen[] oxygens = new Oxygen[2];
        for (Card oxyCard : gameDeck.getDeck()) if (oxyCard.toString().equals(GameDeck.OXYGEN_1)) oxygens[0] = (Oxygen) oxyCard;
        for (Card oxyCard : gameDiscard.getDeck()) if (oxyCard.toString().equals(GameDeck.OXYGEN_1)) oxygens[1] = (Oxygen) oxyCard;

        if (oxygens[0] != null && oxygens[1] != null) {
            gameDeck.remove(oxygens[0]);
            gameDiscard.remove(oxygens[1]);
            gameDeck.add(dbl);
            return oxygens;
        } else throw new IllegalStateException("Not enough oxygens to split");
    }

    /**
     * Returns the winner of the game if there is one.
     * @return the Astronaut who has won the game, or null if no one has won yet
     */
    public Astronaut getWinner() {
        for (Astronaut astronaut : this.activePlayers) if (astronaut.hasWon()) return astronaut;
        return null;
    }

    /**
     * Determines if the game is over.
     * @return true if the game is over, false otherwise
     */
    public boolean gameOver() {
        if (activePlayers.isEmpty()) return true;
        else if (getWinner() != null) return true;
        
        return false;
    }

    /**
     * Returns the game deck.
     * @return the game deck
     */
    public GameDeck getGameDeck() {
        return this.gameDeck;
    } 

    /**
     * Returns the game discard pile.
     * @return the game discard pile
     */
    public GameDeck getGameDiscard() {
        return this.gameDiscard;
    }

    /**
     * Returns the space deck.
     * @return the space deck
     */
    public SpaceDeck getSpaceDeck() {
        return this.spaceDeck;
    }

    /**
     * Returns the space discard pile.
     * @return the space discard pile
     */
    public SpaceDeck getSpaceDiscard() {
        return this.spaceDiscard;
    }

    /**
     * dealCards class used by startGame
     * Deals cards to all active players. Each player receives four Oxygen(1) cards and four additional cards. Each player receives one Oxygen(2) card.
     */
    public void dealCards() {
        for (int i = 0; i < 4; i++) {
            for (Astronaut astronaut : activePlayers) {
                astronaut.addToHand(gameDeck.drawOxygen(1));
            }
        }

        for (Astronaut astronaut : activePlayers) {
            astronaut.addToHand(gameDeck.drawOxygen(2));
        }

        for (int i = 0; i < 4; i++) {
            for (Astronaut astronaut : activePlayers) {
                astronaut.addToHand(gameDeck.draw());
            }
        }
    }

    
}
