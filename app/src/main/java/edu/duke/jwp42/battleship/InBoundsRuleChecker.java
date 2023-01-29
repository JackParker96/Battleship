package edu.duke.jwp42.battleship;

public class InBoundsRuleChecker<T> extends PlacementRuleChecker<T> {

  // Method to check if theShip can be placed on theBoard without going out of bounds
  @Override
  protected boolean checkMyRule(Ship<T> theShip, Board<T> theBoard) {
    Iterable<Coordinate> coords = theShip.getCoordinates();
    for (Coordinate c : coords) {
      int row = c.getRow();
      int column = c.getColumn();
      if (row < 0 || column < 0 || row >= theBoard.getHeight() || column >= theBoard.getWidth()) {
        return false;
      }
    }
    return true;
  }

  public InBoundsRuleChecker(PlacementRuleChecker<T> next) {
    super(next);
  }
  
}
