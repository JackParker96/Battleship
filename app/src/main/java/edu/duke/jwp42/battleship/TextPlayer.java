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
  protected final Board<Character> theBoard;
  protected final BoardTextView view;
  private final BufferedReader input;
  protected final PrintStream out;
  // The name of a player (e.g. "A" or "B")
  protected final String name;
  // List of all ships that a player needs to place (2 subs, 3 destroyers,
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

  public void sonarScanMyBoard(Coordinate center) throws IOException {
    HashMap<String, Integer> scanResult = theBoard.doSonarScan(center);
    int numSubs = scanResult.get("submarine");
    int numDestroyers = scanResult.get("destroyer");
    int numBattleships = scanResult.get("battleship");
    int numCarriers = scanResult.get("carrier");
    out.println(
        "Result of sonar scan centered at coordinate (" + center.getRow() + ", " + center.getColumn() + "):\n\n" +
            "Submarines occupy " + numSubs + " square(s)\n" +
            "Destroyers occupy " + numDestroyers + " square(s)\n" +
            "Battleships occupy " + numBattleships + " square(s)\n" +
            "Carriers occupy " + numCarriers + " square(s)\n");
  }

  /**
   * Takes care of all the functionality for firing at the enemy board
   *
   * @param enemyBoard is the enemy's board
   * @param enemyView  is the BoardTextView version of enemyBoard
   */
  public void fireAtEnemy(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException {
    Coordinate c = null;
    while (true) {
      out.println("Please enter a coordinate on your enemy's board you'd like to fire at.");
      String s = input.readLine();
      try {
        c = new Coordinate(s);
        if (c.getRow() >= enemyBoard.getHeight() || c.getColumn() >= enemyBoard.getWidth()) {
          out.println("Please try again -> That coordinate is not on your enemy's board.");
          continue;
        }
        break;
      } catch (IllegalArgumentException e) {
        out.println("Please try again -> " + e.getMessage());
        continue;
      }
    }
    enemyBoard.fireAt(c);
    Character ch = enemyBoard.whatIsAtForEnemy(c);
    if (ch == 's') {
      out.println("You hit a submarine!");
    } else if (ch == 'd') {
      out.println("You hit a destroyer!");
    } else if (ch == 'b') {
      out.println("You hit a battleship!");
    } else if (ch == 'c') {
      out.println("You hit a carrier!");
    } else {
      out.println("You missed!");
    }
  }

  public void sonarScanEnemyBoard(Board<Character> enemyBoard) throws IOException {
    out.println("Please enter a coordinate on your enemy board that you'd like to be the center of your sonar scan");
    while (true) {
      String centerString = input.readLine();
      try {
        Coordinate centerCoordinate = new Coordinate(centerString);
        HashMap<String, Integer> scanResult = enemyBoard.doSonarScan(null);
        int numSubs = scanResult.get("submarine");
        int numDestroyers = scanResult.get("destroyer");
        int numBattleships = scanResult.get("battleship");
        int numCarriers = scanResult.get("carrier");
        out.println(
            "Result of sonar scan centered at coordinate (" + centerCoordinate.getRow() + ", " + centerCoordinate.getColumn() + "):\n\n" +
                "Submarines occupy " + numSubs + " square(s)\n" +
                "Destroyers occupy " + numDestroyers + " square(s)\n" +
                "Battleships occupy " + numBattleships + " square(s)\n" +
                "Carriers occupy " + numCarriers + " square(s)\n");
        break;
      }
      catch (IllegalArgumentException e) {
        out.println("Please try again -> " + e.getMessage());
      }
    }
  }

  /**
   * Goes through one turn for a single player
   *
   * @param enemyBoard is the enemy's board
   * @param enemyView  is the BoardTextView version of enemyBoard
   * @param enemyName  is the name of the other player
   */
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
    out.println("Current state of the game:\n");
    String enemyHeader = enemyName + "'s ocean";
    out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", enemyHeader));
    out.println("Possible actions for Player " + name + ":\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player " + name + " what would you like to do?");
    while (true) {
      String action = input.readLine().toUpperCase();
      if (action.equals("F")) {
        fireAtEnemy(enemyBoard, enemyView);
        break;
      }
      if (action.equals("S")) {
        sonarScanEnemyBoard(enemyBoard);
        break;
      }
      if (action.equals("M")) {
        // TODO - We'll come back and fill this in later
        break;
      }
      out.println("Please try again -> You must type either f, m, or s to specify which action you'd like to take");
    }
  }

  // Checks if the player has lost the game (all ships sunk)
  public String hasLost() {
    if (theBoard.allShipsSunk()) {
      return name;
    }
    return null;
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
      if (shipName == "Submarine" || shipName == "Destroyer") {
        if (p.getOrientation() != 'V' && p.getOrientation() != 'H') {
          out.println("Please try again -> That placement is invalid: Invalid orientation for " + shipName);
          continue;
        }
      }
      try {
        Ship<Character> s = createFn.apply(p);
        String addShipErrorMessage = theBoard.tryAddShip(s);
        if (addShipErrorMessage == null) {
          out.print(view.displayMyOwnBoard());
          break;
        } else {
          out.println("Please try again -> " + addShipErrorMessage);
        }
      } catch (IllegalArgumentException e) {
        out.println("Please try again -> " + e.getMessage());
        continue;
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
    String prompt = "Player " + name
        + ": You have ten ships that you are going to place on your board one by one.\n\n" +
        "First, you will place two submarines, which are 1x2 rectangles.\n" +
        "Next, you will place three destroyers, which are 1x3 rectangles.\n\n" +
        "To place a submarine or a destroyer, you will type the upper left coordinate for the ship," +
        "followed by H (for a horizontal orientation) or V (vertical).\n" +
        "For example, M4H would place a ship horizontally starting at M4 and going to the right.\n\n" +
        "Next, you will place three battleships, which are shaped like a T.\n" +
        "Battleships can have four different orientations: Up, Down, Left, or Right:\n\n" +
        "  b      bbb       b        b\n" +
        " bbb      b       bb        bb\n " +
        "                  b        b\n\n" +
        " UP      DOWN     LEFT      RIGHT\n\n" +
        "To specify where you want to place your battleships, first type the coordinate of the upper left corner of the smallest rectangle containing your ship\n"
        +
        "Then type either U, D, L, or R to specify the orientation\n\n" +
        "Finally, you will place two carriers that are shaped like a Z and can have the same four orienations as a battleship\n\n"
        +
        "  c       c        ccc     cccc\n" +
        "  c       cc     cccc     ccc\n" +
        "  cc      cc\n" +
        "  cc       c\n" +
        "   c       c\n\n" +
        "  UP     DOWN     LEFT     RIGHT\n\n" +
        "You should specify how you want to place a carrier in the same way as for battleships\n\n";
    out.println(prompt);
    for (String shipName : shipsToPlace) {
      Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipName);
      doOnePlacement(shipName, createFn);
    }
  }

}
