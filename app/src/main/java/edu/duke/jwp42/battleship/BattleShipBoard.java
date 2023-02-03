package edu.duke.jwp42.battleship;

import java.util.ArrayList;
import java.util.HashSet;

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

  // Keeps track of all the coordinates that have been fired at but that were misses
  private final HashSet<Coordinate> enemyMisses;

  // In V1, this holds a character that we display on the enemy's view of the board for squares that they have fired at but missed
  private final T missInfo;

  /**
   * Construct a BattleShipBoard
   *
   * @param w is the width of the board (number of columns)
   * @param h is the height of the board (number of rows)
   * @param placementChecker is an object that will enforce various criteria for placing ships on the board (e.g. must not go out of bounds of the board, must not collide with any ships already on the board)
   * @param missInfo is the graphical symbol which will appear on the enemy's view of the board for any square where they have fired and missed
   */
  public BattleShipBoard(int w, int h, PlacementRuleChecker<T> placementChecker, T missInfo) {
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
    this.enemyMisses = new HashSet<Coordinate>();
    this.missInfo = missInfo;
  }

  // Constructs a BattleShipBoard using constructor chaining
  // Initializes placementChecker to a checker for no collisions and no out of
  // bounds placements
  public BattleShipBoard(int w, int h, T missInfo) {
    this(w, h, new InBoundsRuleChecker<T>(new NoCollisionRuleChecker<T>(null)), missInfo);
  }

  // Checks if all ships on the board are sunk
  public boolean allShipsSunk() {
    for (Ship<T> s : myShips) {
      if (!s.isSunk()) {
        return false;
      }
    }
    return true;
  }
  
  public Ship<T> fireAt(Coordinate c) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(c)) {
        s.recordHitAt(c);
        return s;
      }
    }
    enemyMisses.add(c);
    return null;
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

  /**
   * Method for retrieving the graphical symbol corresponding to the current state of the board at a particular Coordinate (from the POV of the player who owns the board)
   *
   * @param where is the Coordinate where we want to retrieve the symbol from
   * @return the symbol (e.g. 's' if your own sub lives at 'where' or '*' if your ship at 'where' has been hit)
   */
  public T whatIsAtForSelf(Coordinate where) {
    return whatIsAt(where, true);
  }

  /**
   * Same as whatIsAtForSelf, but from the enemy's perspective
   *
   * @param where is the coordinate where we want to retrieve the graphical symbol from
   * @return the symbol (e.g. 's' if you have fired at 'where' and hit a submarine, missInfo if you have fired at 'where' and missed)
   */
  public T whatIsAtForEnemy(Coordinate where) {
    return whatIsAt(where, false);
  }

  protected T whatIsAt(Coordinate where, boolean isSelf) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(where)) {
        return s.getDisplayInfoAt(where, isSelf);
      }
    }
    if (!isSelf && enemyMisses.contains(where)) {
      return missInfo;
    }
    return null;
  }
}
