package edu.duke.jwp42.battleship;

import java.util.HashMap;

/**
 * The BasicShip class is just a 1x1 ship (takes up only one square on the board).
 * When a BasicShip is on the board, it's represented graphically by the letter 's'
 */
public class BasicShip implements Ship<Character> {
  protected HashMap<Coordinate, Boolean> myPieces;

  public BasicShip(Coordinate c) {
    myPieces = new HashMap<Coordinate, Boolean>();
    myPieces.put(c, false);
  }

  public BasicShip(Iterable<Coordinate> where) {
    for (Coordinate c : where) {
      myPieces.put(c, false);
    }
  }
  
  @Override
  public boolean occupiesCoordinates(Coordinate where) {
    if (myPieces.get(where) != null) {
      return true;
    }
    return false;
  }

  @Override
  public boolean isSunk() {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public void recordHitAt(Coordinate where) {
    // TODO Auto-generated method stub
    
  }

  @Override
  public boolean wasHitAt(Coordinate where) {
    // TODO Auto-generated method stub
    return false;
  }

  @Override
  public Character getDisplayInfoAt(Coordinate where) {
    return 's';
  }
  
}
