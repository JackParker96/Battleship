package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public abstract class Player {

  protected final AbstractShipFactory<Character> shipFactory;
  protected final Board<Character> theBoard;
  protected final BoardTextView view;
  protected final BufferedReader input;
  protected final PrintStream out;
  // The name of a player (e.g. "A" or "B")
  protected final String name;
  // List of all ships that a player needs to place (2 subs, 3 destroyers,
  // 3 bships, 2 carriers)
  protected final ArrayList<String> shipsToPlace;
  // Map from ship names (Submarine, Battleship, etc.) to functions in
  // V1ShipFactory used to construct these ships
  protected final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;
  protected int numSonarScansRemaining;
  protected int numMoveShipsRemaining;

  public Player(AbstractShipFactory<Character> f, Board<Character> b, BufferedReader input, PrintStream out, String name) {
    this.shipFactory = f;
    this.theBoard = b;
    this.view = new BoardTextView(theBoard);
    this.input = input;
    this.out = out;
    this.name = name;
    this.numSonarScansRemaining = 3;
    this.numMoveShipsRemaining = 3;
    this.shipsToPlace = new ArrayList<String>();
    setupShipCreationList();
    this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
    setupShipCreationMap();
  }

  // Uses the AbstractShipFactory to get functions used to create the four different types of ships
  public void setupShipCreationMap() {
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
  }

  // Populates a list with all ten ships to be added by the player
  public void setupShipCreationList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  // Provides all the functionality for playing a single turn of Battleship
  public abstract void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException;

  /**
   * Method to check if the player has lost the game
   *
   * @returns the name of the player if they have lost, returns null otherwise
   */
  public abstract String hasLost();

  // Provides all the functionality for doing the placement phase of Battleship
  public abstract void doPlacementPhase() throws IOException;

}
