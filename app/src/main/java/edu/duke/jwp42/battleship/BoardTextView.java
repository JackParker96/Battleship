package edu.duke.jwp42.battleship;

public class BoardTextView {
  private final Board toDisplay;

  public BoardTextView(Board toDisplay) {
    this.toDisplay = toDisplay;
  }

  public String makeHeader() {
    StringBuilder ans = new StringBuilder("  ");
    String sep = "";
    for (int i = 0; i < toDisplay.getWidth(); i++) {
      ans.append(sep);
      ans.append(i);
      sep = "|";
    }
    ans.append("\n");
    return ans.toString();
  }

  public String displayMyOwnBoard() {
    return "";
  }
}
