package edu.duke.jwp42.battleship;

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
   * Returns string representation of board heade/footer (e.g. " 0|1|2|3|4 ")
   */
  public String makeHeader() {
    StringBuilder ans = new StringBuilder(" ");
    String sep = "";
    for (int i = 0; i < toDisplay.getWidth(); i++) {
      ans.append(sep);
      ans.append(i);
      sep = "|";
    }
    ans.append("\n");
    return ans.toString();
  }

  /**
   * Returns string representation of empty board row
   *
   * @param row is the number of the row to return
   * @throws IllegalArgumentException if y is not in [0, 25]
   */
  public String makeRow(int row) {
    if (row > 25 || row < 0) {
      throw new IllegalArgumentException("Argument row must be in [0, 25] but is " + row);
    }
    String alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase();
    char letter = alphabet.charAt(row);
    int w = toDisplay.getWidth();
    StringBuilder ans = new StringBuilder();
    ans.append(letter);
    for (int column = 0; column < w; column++) {
      String filler = " ";
      Coordinate c = new Coordinate(row, column);
      if (toDisplay.whatIsAt(c) != null) {
        Character ch = toDisplay.whatIsAt(c);
        filler = Character.toString(ch);
        //filler = "s";
      }
      ans.append(filler);
      if (column < w - 1) {
        ans.append("|");
      }
    }
    ans.append(letter);
    return ans.toString();
  }

  /**
   * Leverages the makeRow method to return the blank body of the board
   */
  public String makeBody() {
    StringBuilder ans = new StringBuilder();
    for (int i = 0; i < toDisplay.getHeight(); i++) {
      String row = makeRow(i);
      ans.append(row + "\n");
    }
    return ans.toString();
  }

  /**
   * Returns a string representation of the entire blank board
   */
  public String displayMyOwnBoard() {
    String header = makeHeader();
    String body = makeBody();
    return header + body + header;
  }
}
