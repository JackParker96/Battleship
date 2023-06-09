package edu.duke.jwp42.battleship;

import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * The BasicShip class is just a 1x1 ship (takes up only one square on the
 * board).
 * When a BasicShip is on the board, it's represented graphically by the letter
 * 's'
 */
public abstract class BasicShip<T> implements Ship<T> {

  protected ShipDisplayInfo<T> myDisplayInfo;

  protected ShipDisplayInfo<T> enemyDisplayInfo;

  /**
   * myPieces is a HashMap that keeps track of information about the squares that
   * a BasicShip occupies
   *
   * Let c be any coordinate. If you do myPieces.get(c), there are three
   * possibilities for the return value:
   * (a) null: The BasicShip does not occupy Coordinate c
   * (b) True: The BasicShip does occupy Coordinate c and has been hit
   * (c) False: The BasicShip does occupy Coordinate c and has not been hit
   */
  protected LinkedHashMap<Coordinate, Boolean> myPieces;

  /**
   * Constructs a BasicShip
   *
   * @param where            is any Iterable of Coordinates (likely a HashSet)
   *                         that
   *                         we want the constructed BasicShip to occupy
   * @param myDisplayInfo    is an object that contains information about how we
   *                         want
   *                         the BasicShip to be displayed (see documentation for
   *                         ShipDisplayInfo)
   * @param enemyDispalyInfo is similar to above but contains information about
   *                         how we want an enemy's BasicShip to be displayed.
   *
   *                         Initializes this.myPieces by putting every coordinate
   *                         in
   *                         the Iterable into the HashTable and labeling it false
   *                         (not hit)
   */
  public BasicShip(Iterable<Coordinate> where, ShipDisplayInfo<T> myDisplayInfo, ShipDisplayInfo<T> enemyDisplayInfo) {
    this.myDisplayInfo = myDisplayInfo;
    this.enemyDisplayInfo = enemyDisplayInfo;
    this.myPieces = new LinkedHashMap<Coordinate, Boolean>();
    for (Coordinate c : where) {
      this.myPieces.put(c, false);
    }
  }

  /**
   * Helper method that checks if a ship occupies Coordinate c
   * 
   * @throws IllegalArgumentException if the ship does not occupy Coordinate c
   */
  protected void checkCoordinateInThisShip(Coordinate c) throws IllegalArgumentException {
    if (myPieces.get(c) == null) {
      throw new IllegalArgumentException();
    }
  }

  // This method allows us to check whether or not a BasicShip occupies a
  // particular Coordinate on the board
  @Override
  public boolean occupiesCoordinates(Coordinate where) {
    if (myPieces.get(where) != null) {
      return true;
    }
    return false;
  }

  // Checks if the ship is sunk or not
  @Override
  public boolean isSunk() {
    for (boolean bool : myPieces.values()) {
      if (bool == false) {
        return false;
      }
    }
    return true;
  }

  @Override
  public void recordHitAt(Coordinate where) throws IllegalArgumentException {
    checkCoordinateInThisShip(where);
    myPieces.put(where, true);
  }

  @Override
  public boolean wasHitAt(Coordinate where) {
    checkCoordinateInThisShip(where);
    return myPieces.get(where);
  }

  @Override
  public T getDisplayInfoAt(Coordinate where, boolean myShip) {
    boolean hit = wasHitAt(where);
    if (myShip) {
      return myDisplayInfo.getInfo(where, hit);
    }
    return enemyDisplayInfo.getInfo(where, hit);
  }

  @Override
  public Iterable<Coordinate> getCoordinates() {
    return myPieces.keySet();
  }

  // Move ship to new location
  @Override
  abstract public void move(Placement newPlacement);

}
