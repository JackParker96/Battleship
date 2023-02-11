package edu.duke.jwp42.battleship;

import java.util.ArrayList;
import java.util.HashMap;
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

  // Keeps track of all the coordinates that have been fired at but that were
  // misses
  private final HashSet<Coordinate> enemyMisses;

  // In V1, this holds a character that we display on the enemy's view of the
  // board for squares that they have fired at but missed
  private final T missInfo;

  /**
   * Construct a BattleShipBoard
   *
   * @param w                is the width of the board (number of columns)
   * @param h                is the height of the board (number of rows)
   * @param placementChecker is an object that will enforce various criteria for
   *                         placing ships on the board (e.g. must not go out of
   *                         bounds of the board, must not collide with any ships
   *                         already on the board)
   * @param missInfo         is the graphical symbol which will appear on the
   *                         enemy's view of the board for any square where they
   *                         have fired and missed
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

  /**
   * Fire at a specified coordinate on the board
   *
   * @param c is the Coordinate to fire at
   * @return the ship that was hit or null if not ship was hit
   */
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
   * Checks if a ship can be moved to a particular spot on the board
   *
   * @param potentialMovedShip is the hypothetical new ship placed at its new placement
   * @return true if the move is legal and false if not
   */
  public Boolean checkForValidShipMove(Ship<T> potentialMovedShip) {
    String placementCheck = placementChecker.checkPlacement(potentialMovedShip, this);
    if (placementCheck == null) {
      return true;
    }
    return false;
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
   * Method for retrieving the graphical symbol corresponding to the current state
   * of the board at a particular Coordinate (from the POV of the player who owns
   * the board)
   *
   * @param where is the Coordinate where we want to retrieve the symbol from
   * @return the symbol (e.g. 's' if your own sub lives at 'where' or '*' if your
   *         ship at 'where' has been hit)
   */
  public T whatIsAtForSelf(Coordinate where) {
    return whatIsAt(where, true);
  }

  /**
   * Same as whatIsAtForSelf, but from the enemy's perspective
   *
   * @param where is the coordinate where we want to retrieve the graphical symbol
   *              from
   * @return the symbol (e.g. 's' if you have fired at 'where' and hit a
   *         submarine, missInfo if you have fired at 'where' and missed)
   */
  public T whatIsAtForEnemy(Coordinate where) {
    return whatIsAt(where, false);
  }

  protected T whatIsAt(Coordinate where, boolean isSelf) {
    if (!isSelf && enemyMisses.contains(where)) {
      return missInfo;
    }
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(where)) {
        return s.getDisplayInfoAt(where, isSelf);
      }
    }
    return null;
  }

  /**
   * Get the ship at a particular coordinate
   *
   * @param c is the coordinate you want to get the ship from
   * @returns the ship that occupies the coordinate or null otherwise
   */
  public Ship<T> getShipAt(Coordinate c) {
    for (Ship<T> s : myShips) {
      if (s.occupiesCoordinates(c)) {
        return s;
      }
    }
    return null;
  }
  
  /**
   * Method to perform a sonar scan of the board
   *
   * @param center is the Coordinate of the center of the sonar scan
   * @return a mapping from the four ship names to the total number of squares
   *         occupied by that ship within the sonar scan diamond-shaped range
   * @throws IllegalArgumentException if center is not on the board
   */
  @Override
  public HashMap<String, Integer> doSonarScan(Coordinate center) {
    HashMap<String, Integer> ans = new HashMap<String, Integer>();
    ans.put("submarine", 0);
    ans.put("destroyer", 0);
    ans.put("battleship", 0);
    ans.put("carrier", 0);
    if (center.getRow() >= height || center.getColumn() >= width) {
      throw new IllegalArgumentException("Error - Center of sonar scan is not on the board");
    }
    int cRow = center.getRow();
    int cCol = center.getColumn();
    for (int i = 0; i < height; i++) {
      for (int j = 0; j < width; j++) {
        int manhattanDistance = Math.abs(cRow - i) + Math.abs(cCol - j);
        if (manhattanDistance <= 3) {
          Coordinate c = new Coordinate(i, j);
          for (Ship<T> s : myShips) {
            if (s.occupiesCoordinates(c)) {
              int current = ans.get(s.getName());
              ans.put(s.getName(), current + 1);
            }
          }
        }
      }
    }
    return ans;
  }
}
