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
public class TextPlayer extends Player {

  public TextPlayer(String name, Board<Character> theBoard, BufferedReader input, PrintStream out,
      AbstractShipFactory<Character> factory) {
    super(factory, theBoard, input, out, name);
  }

  /**
   * Does a sonar scan of the player's own board - not used at all in this
   * program, but I thought it could be useful to have
   *
   * @param center is the coordinate (input by the player) of the center of hte
   *               sonar scan
   * @throws IOException
   */
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

  /**
   * Method for doing a sonar scan of an enemy board
   *
   * @param enemyBoard is the enemy's board
   * @throws IOException
   */
  public void sonarScanEnemyBoard(Board<Character> enemyBoard) throws IOException {
    out.println("Please enter a coordinate on your enemy board that you'd like to be the center of your sonar scan");
    while (true) {
      String centerString = input.readLine();
      try {
        Coordinate centerCoordinate = new Coordinate(centerString);
        HashMap<String, Integer> scanResult = enemyBoard.doSonarScan(centerCoordinate);
        int numSubs = scanResult.get("submarine");
        int numDestroyers = scanResult.get("destroyer");
        int numBattleships = scanResult.get("battleship");
        int numCarriers = scanResult.get("carrier");
        out.println(
            "Result of sonar scan centered at coordinate (" + centerCoordinate.getRow() + ", "
                + centerCoordinate.getColumn() + "):\n\n" +
                "Submarines occupy " + numSubs + " square(s)\n" +
                "Destroyers occupy " + numDestroyers + " square(s)\n" +
                "Battleships occupy " + numBattleships + " square(s)\n" +
                "Carriers occupy " + numCarriers + " square(s)\n");
        numSonarScansRemaining -= 1;
        break;
      } catch (IllegalArgumentException e) {
        out.println("Please try again -> " + e.getMessage());
      }
    }
  }

  // Takes care of all the functionality for allowing a player to move a ship to a
  // new location on their board
  public Boolean moveShipOnMyBoard() throws IOException {
    String tryAgainPrompt = "A problem occurred. Either you entered a coordinate that doesn't have a ship, or you entered an invalid placement. Please select your action again. You can choose move ship again, or you can choose a different action";
    out.println(
        "Please choose a ship you'd like to move, select any Coordinate occupied by that ship, and input the Coordinate.");
    // Prompt until user enters a valid coordinate
    Coordinate c = null;
    while (true) {
      String chosenCoord = input.readLine();
      try {
        c = new Coordinate(chosenCoord);
        break;
      } catch (IllegalArgumentException e) {
        out.println("Please try again -> " + e.getMessage());
      }
    }
    // If they entered a Coordinate with no ship, kick them back to playOneTurn
    // where they choose their action
    Ship<Character> shipToMove = theBoard.getShipAt(c);
    if (shipToMove == null) {
      out.println(tryAgainPrompt);
      return false;
    }
    // At this point, user has entered a valid coordinate occupied by a ship
    // Prompt user until they enter a placement in a valid format
    Placement p = readPlacement(
        "Ship located. Now please enter a new placement for the ship in the same format as you entered placements at the beginning of the game (e.g. M4V)");
    String shipType = shipToMove.getName();
    // Now use the placement to create a ship of the same type as shipToMove placed
    // at the new location
    Ship<Character> potentialNewShip;
    if (shipType == "submarine") {
      potentialNewShip = shipFactory.makeSubmarine(p);
    } else if (shipType == "destroyer") {
      potentialNewShip = shipFactory.makeDestroyer(p);
    } else if (shipType == "battleship") {
      potentialNewShip = shipFactory.makeBattleship(p);
    } else {
      potentialNewShip = shipFactory.makeCarrier(p);
    }
    // Now use a placementRuleChecker to check if the new placement is valid. If
    // not, kick them back to playOneTurn where they choose their action
    Boolean okayToMove = theBoard.checkForValidShipMove(potentialNewShip);
    if (!okayToMove) {
      out.println(tryAgainPrompt);
      return false;
    }
    shipToMove.move(p);
    numMoveShipsRemaining -= 1;
    return true;
  }

  /**
   * Goes through one turn for a single player
   *
   * @param enemyBoard is the enemy's board
   * @param enemyView  is the BoardTextView version of enemyBoard
   * @param enemyName  is the name of the other player
   */
  @Override
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
    out.println("Current state of the game:\n");
    String enemyHeader = enemyName + "'s ocean";
    out.println(view.displayMyBoardWithEnemyNextToIt(enemyView, "Your ocean", enemyHeader));
    out.println("Possible actions for Player " + name + ":\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (" + numMoveShipsRemaining + " remaining)\n" +
        "S - Sonar scan (" + numSonarScansRemaining + " remaining)\n\n" +
        "Player " + name + " what would you like to do?");
    while (true) {
      String action = input.readLine().toUpperCase();
      if (action.equals("F")) {
        fireAtEnemy(enemyBoard, enemyView);
        break;
      }
      if (action.equals("S")) {
        if (numSonarScansRemaining < 1) {
          out.println("You don't have any sonar scan actions left. Please type either F or M (if you have any move ship actions left).");
          continue;
        }
        sonarScanEnemyBoard(enemyBoard);
        break;
      }
      if (action.equals("M")) {
        if (numMoveShipsRemaining < 1) {
          out.println("You don't have any move ship actions left. Please type F or S (if you have any sonar scan actions left.)");
          continue;
        }
        Boolean validPlacement = moveShipOnMyBoard();
        if (!validPlacement) {
          continue;
        }
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
