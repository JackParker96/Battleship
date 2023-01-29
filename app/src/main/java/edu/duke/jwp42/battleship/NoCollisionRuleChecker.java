package edu.duke.jwp42.battleship;

public class NoCollisionRuleChecker<T> extends PlacementRuleChecker<T> {

  @Override
  protected boolean checkMyRule(Ship<T> theShip, Board<T> theBoard) {
    // use .getCoordinate() method of Ship to get iterable of Coordinates 
    //use .whatIsAt(Coordinate c) method of Board to make sure all squares needed by theShip are null
    Iterable<Coordinate> coords = theShip.getCoordinates();
    for (Coordinate c : coords) {
      if (theBoard.whatIsAt(c) != null) {
        return false;
      }
    }
    return true;
  }

  public NoCollisionRuleChecker(PlacementRuleChecker<T> next) {
    super(next);
  }
  
}
