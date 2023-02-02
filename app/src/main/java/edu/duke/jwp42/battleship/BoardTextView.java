package edu.duke.jwp42.battleship;

import java.util.function.Function;

/**
 * This class handles textual display of a Board
 * It returns a String representation of the Board to display to the user
 * This class can display either the user's own board or the enemy's board
 */
public class BoardTextView {
  private final Board<Character> toDisplay;

  /**
   * Constructs a BoardView, given the board it will display
   * 
   * @param toDisplay is the Board it will display
   * @throws IllegalArgumentException if toDisplay is larger than 10x26
   */
  public BoardTextView(Board<Character> toDisplay) {
    this.toDisplay = toDisplay;
    if (toDisplay.getWidth() > 10 || toDisplay.getHeight() > 26) {
      throw new IllegalArgumentException(
          "Board must be no larger than 10x26, but is " + toDisplay.getWidth() + "x" + toDisplay.getHeight());
    }
  }

  /**
   * Method to display the graphical view of toDisplay.
   * Can either display toDisplay from the perspective of the player who owns the
   * board or from the perspective of the enemy.
   *
   * @param getSquareFn is a Coordinate -> Character mapping. We can pass in a
   *                    function that displays character's from the player's own
   *                    perspective (if they are the owner of toDisplay) or from
   *                    the enemy's perspective.
   * @return the textual view of the board.
   */
  protected String displayAnyBoard(Function<Coordinate, Character> getSquareFn) {
    // Create the header/footer for the board
    StringBuilder headBuilder = new StringBuilder(" ");
    String sep = "";
    for (int i = 0; i < toDisplay.getWidth(); i++) {
      headBuilder.append(sep);
      headBuilder.append(i);
      sep = "|";
    }
    headBuilder.append("\n");
    String header = headBuilder.toString();
    // Create the body for the board
    StringBuilder bodyBuilder = new StringBuilder();
    String alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase();
    int w = toDisplay.getWidth();
    for (int row = 0; row < toDisplay.getHeight(); row++) {
      StringBuilder rowBuilder = new StringBuilder();
      char letter = alphabet.charAt(row);
      rowBuilder.append(letter);
      for (int column = 0; column < w; column++) {
        String filler = " ";
        Coordinate c = new Coordinate(row, column);
        if (getSquareFn.apply(c) != null) {
          Character ch = getSquareFn.apply(c);
          filler = Character.toString(ch);
        }
        rowBuilder.append(filler);
        if (column < w - 1) {
          rowBuilder.append("|");
        }
      }
      rowBuilder.append(letter);
      String rowString = rowBuilder.toString();
      bodyBuilder.append(rowString + "\n");
    }
    String body = bodyBuilder.toString();
    // Put everything together
    return header + body + header;
  }

  /**
   * Method for displaying the textual view of a player's own board.
   * Passes a lambda to the displayAnyBoard.
   * This lambda is a function mapping Coordinates to the self display info for
   * that Coordinate
   */
  public String displayMyOwnBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForSelf(c));
  }

  /**
   * Exact same ideas as displayMyOwnBoard, but this displays the textual view of
   * the enemy's board.
   */
  public String displayEnemyBoard() {
    return displayAnyBoard((c) -> toDisplay.whatIsAtForEnemy(c));
  }
}
