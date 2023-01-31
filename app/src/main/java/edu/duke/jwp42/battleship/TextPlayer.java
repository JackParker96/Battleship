package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
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
  private final String name;
  private final ArrayList<String> shipsToPlace;
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
    this.shipsToPlace = new ArrayList<String>();
    setupShipCreationList();
    this.shipCreationFns = new HashMap<String, Function<Placement, Ship<Character>>>();
    setupShipCreationMap();
  }

  protected void setupShipCreationMap() {
    shipCreationFns.put("Submarine", (p) -> shipFactory.makeSubmarine(p));
    shipCreationFns.put("Destroyer", (p) -> shipFactory.makeDestroyer(p));
    shipCreationFns.put("Battleship", (p) -> shipFactory.makeBattleship(p));
    shipCreationFns.put("Carrier", (p) -> shipFactory.makeCarrier(p));
  }

  protected void setupShipCreationList() {
    shipsToPlace.addAll(Collections.nCopies(2, "Submarine"));
    shipsToPlace.addAll(Collections.nCopies(3, "Destroyer"));
    shipsToPlace.addAll(Collections.nCopies(3, "Battleship"));
    shipsToPlace.addAll(Collections.nCopies(2, "Carrier"));
  }

  // Prompts the TextPlayer for a Placement and returns the given placement
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = input.readLine();
    return new Placement(s);
  }

  // Calls the readPlacement() method above, creates a destroyer based on that
  // placement, tries to add that destroyer to the TextPlayer's board, then
  // displays the TextPlayer's board with the newly placed destroyer
  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException {
    Placement p = readPlacement("Player " + name + " where do you want to place a " + shipName + "?");
    Ship<Character> s = createFn.apply(p);
    theBoard.tryAddShip(s);
    out.print(view.displayMyOwnBoard());
  }

  /**
   * (1) Display the TextPlayer's empty board
   * (2) Print a prompt explaining how to enter a placement and which ships the
   * player can place
   * (3) Call the doOnePlacement() method from above
   */
  public void doPlacementPhase() throws IOException {
    view.displayMyOwnBoard();
    String prompt = "--------------------------------------------------------------------------------\n" +
        "Player A: you are going to place the following ships (which are all " +
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
