package edu.duke.jwp42.battleship;

public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

  // Method to check if theShip can be placed on theBoard without going out of
  // bounds
  @Override
  protected String checkMyRule(Ship<T> theShip, Board<T> theBoard) {
    Iterable<Coordinate> coords = theShip.getCoordinates();
    for (Coordinate c : coords) {
      int row = c.getRow();
      int column = c.getColumn();
      /**
      if (row < 0) {
        String offLeftOfBoardErrorMessage = "That placement is invalid: the ship goes off the left of the board.";
        return offLeftOfBoardErrorMessage;
      }
      if (column < 0) {
        String offTopOfBoardErrorMessage = "That placement is invalid: the ship goes off the top of the board.";
        return offTopOfBoardErrorMessage;
      }
      */
      if (row >= theBoard.getHeight()) {
        String offBottomOfBoardErrorMessage = "That placement is invalid: the ship goes off the bottom of the board.";
        return offBottomOfBoardErrorMessage;
      }
      if (column >= theBoard.getWidth()) {
        String offRightOfBoardErrorMessage = "That placement is invalid: the ship goes off the right of the board.";
        return offRightOfBoardErrorMessage;
      }
    }
    return null;
  }

  public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
    super(next);
  }

}
