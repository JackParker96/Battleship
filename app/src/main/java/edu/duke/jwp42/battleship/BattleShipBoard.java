package edu.duke.jwp42.battleship;

import java.util.ArrayList;

/**
 * BattleShipBoard<T> is generic in the type of the ships that occupy the board.
 * The type T specifies how a ship is displayed graphically (in v1 T is simply a
 * Character)
 */
public class BattleShipBoard<T> implements Board<T> {
  private final int width;

  private final int height;

  private final ArrayList<Ship<T>> myShips;

  private final PlacementRuleChecker<T> placementChecker;

  // Constructs a BattleShipBoard with specified width (number of columns) height
  // (number of rows), and placement rule checker
  public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker) {
    // Board width must be strictly positive
    if (w <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
    }
    // Board height must be strictly positive
    if (h <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
    }
    this.width = w;
    this.height = h;
    this.myShips = new ArrayList<Ship<T>>();
    this.placementChecker = placementChecker;
  }

  // Constructs a BattleShipBoard using constructor chaining
  // Initializes placementChecker to a checker for no collisions and no out of
  // bounds placements
  public BattleShipBoard(int w, int h) {
    this(w, h, new InBoundsRuleChecker<T>(new NoCollisionRuleChecker<T>(null)));
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  /**
   * Checks if all placement rules for a ship are verified and if so, adds it to
   * myShips
   *
   * @param toAdd is the Ship<T> we want to add to the board
   * @return true if adding toAdd wouldn't violate any rules in placementChecker,
   *         return false otherwise
   */
  public String tryAddShip(Ship<T> toAdd) {
    String placementCheck = placementChecker.checkPlacement(toAdd, this);
    if (placementCheck == null) {
      myShips.add(toAdd);
    }
    return placementCheck;
  }

  // Allows us to peek at any Coordinate on the board and see whether a ship
  // occupies that Coordinate
  // If a ship does occupy that coordinate, this method returns the graphical
  // symbol for that particular ship at that particular Coordinate
  public T whatIsAt(Coordinate where) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(where)) {
        return s.getDisplayInfoAt(where);
      }
    }
    return null;
  }
}
