package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.EOFException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

/**
 * A class for a player in a text-based game of battleship
 */
public class TextPlayer {
  private final AbstractShipFactory<Character> shipFactory;
  private final Board<Character> theBoard;
  private final BoardTextView view;
  private final BufferedReader input;
  private final PrintStream out;
  // The name of a player (e.g. "A" or "B")
  private final String name;
  // List of all ships that a player needs to place (in V1, 2 subs, 3 destroyers,
  // 3 bships, 2 carriers)
  protected final ArrayList<String> shipsToPlace;
  // Map from ship names (Submarine, Battleship, etc.) to functions in
  // V1ShipFactory used to construct these ships
  private final HashMap<String, Function<Placement, Ship<Character>>> shipCreationFns;

  // Constructs a TextPlayer
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader input, PrintStream out,
      AbstractShipFactory<Character> factory) {
    this.shipFactory = factory;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.input = input;
    this.out = out;
    this.name = name;
    // Initialize shipsToPlace to an empty ArrayList, then call
    // setupShipCreationList() to fill it up with ships
    this.shipsToPlace = new ArrayList<String>();
    setupShipCreationList();
    // Initialize shipCreationFns to an empty HashMap, then call
    // setupShipCreationMap to fill it up with mappings from ship names to the
    // functions used to create that ship
    this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
    setupShipCreationMap();
  }

  // Adds four key/value pairs to shipCreationFns, one for each ship type in V1
  protected void setupShipCreationMap() {
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
  }

  // Adds 10 ships to shipsToPlace (the 10 ships used in V1)
  protected void setupShipCreationList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  // Checks if the player has lost the game (all ships sunk)
  public boolean hasLost() {
    return theBoard.allShipsSunk();
  }

  /**
   * Prompt the player for a placement string (e.g. "A1h") and return a Placement
   * object corresponding to the player's input.
   *
   * If the player's input is invalid (incorrect format, collision, not in
   * bounds), an appropriate message is displayed to the player, and they are
   * prompted for input again. This continues until valid input is received.
   *
   * @param prompt - To be displayed to the player to get them to enter a
   *               placement
   * @throws EOFException if input.readLine() returns null (used mainly for
   *                      testing purposes)
   * @return a Placement constructed from the player's input
   */
  public Placement readPlacement(String prompt) throws IOException {
    Placement p;
    while (true) {
      try {
        out.println(prompt);
        String s = input.readLine();
        if (s == null) {
          throw new EOFException();
        }
        p = new Placement(s);
        break;
      } catch (IllegalArgumentException e) {
        out.println("Please try again -> " + e.getMessage());
      }
    }
    return p;
  }

  /**
   * Places a single ship on the board
   *
   * @param shipName is the name of the type of ship to place (e.g. "Submarine")
   * @param createFn is the function used to create that particular ship
   * @return void, but print out the current state of the board after adding the
   *         ship
   */
  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
    while (true) {
      Placement p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?");
      Ship<Character> s = createFn.apply(p);
      String addShipErrorMessage = theBoard.tryAddShip(s);
      if (addShipErrorMessage == null) {
        out.print(view.displayMyOwnBoard());
        break;
      } else {
        out.println("Please try again -> " + addShipErrorMessage);
      }
    }
  }

  /**
   * (1) Display the TextPlayer's empty board
   * (2) Print a prompt explaining how to enter a placement and which ships the
   * player can place
   * (3) Call the doOnePlacement() method from above
   */
  public void doPlacementPhase() throws IOException {
    String emptyBoard = view.displayMyOwnBoard();
    out.println(emptyBoard);
    String prompt = "--------------------------------------------------------------------------------\n" +
        "Player " + name + ": you are going to place the following ships (which are all " +
        "rectangular). For each ship, type the coordinate of the upper left " +
        "side of the ship, followed by either H (for horizontal) or V (for " +
        "vertical).  For example M4H would place a ship horizontally starting " +
        "at M4 and going to the right.  You have" +
        "\n\n" +
        "2 \"Submarines\" ships that are 1x2\n" +
        "3 \"Destroyers\" that are 1x3\n" +
        "3 \"Battleships\" that are 1x4\n" +
        "2 \"Carriers\" that are 1x6\n" +
        "--------------------------------------------------------------------------------";
    out.println(prompt);
    for (String shipName : shipsToPlace) {
      Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipName);
      doOnePlacement(shipName, createFn);
    }
  }

}
