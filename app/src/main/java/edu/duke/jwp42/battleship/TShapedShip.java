package edu.duke.jwp42.battleship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * This class allows us to construct a battleship in Version 2
 */

public class TShapedShip<T> extends BasicShip<T> {
  // The name associated with the ship
  public final String name;

  /**
   * Static method who purpose is to be called by the constructor and who output
   * will be fed to the super constructor
   * Makes a set of all the coordinates occupie by the t-shaped ship
   *
   * @param upperLeft is a placement for the upper left corner of the ship
   * @return a HashSet containing all Coordinate occupied by the t-shaped ship
   */
  public static final LinkedHashSet<Coordinate> makeCoords(Placement upperLeft) {
    LinkedHashSet<Coordinate> ans = new LinkedHashSet<Coordinate>();
    Coordinate where = upperLeft.getWhere();
    int row = where.getRow();
    int column = where.getColumn();
    char orientation = upperLeft.getOrientation();
    if (orientation != 'U' && orientation != 'D' && orientation != 'L' && orientation != 'R') {
      throw new IllegalArgumentException(
          "Placement orientation for a battleship must be in [U, D, L, R] but instead was " + orientation);
    }
    if (orientation == 'U') {
      Coordinate tip = new Coordinate(row, column + 1);
      ans.add(tip);
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + 1, column + i);
        ans.add(c);
      }
    }
    if (orientation == 'R') {
      Coordinate tip = new Coordinate(row + 1, column + 1);
      ans.add(tip);
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + i, column);
        ans.add(c);
      }
    }
    if (orientation == 'D') {
      Coordinate tip = new Coordinate(row + 1, column + 1);
      ans.add(tip);
      for (int i = 2; i > -1; i--) {
        Coordinate c = new Coordinate(row, column + i);
        ans.add(c);
      }
    }
    if (orientation == 'L') {
      Coordinate tip = new Coordinate(row + 1, column);
      ans.add(tip);
      for (int i = 2; i > -1; i--) {
        Coordinate c = new Coordinate(row + i, column + 1);
        ans.add(c);
      }
    }
    return ans;
  }

  /**
   * Construct a t-shaped ship by calling the super constructor
   *
   * @param name             is the name of the ship
   * @param upperLeft        is the placement corresponding to the upper left
   *                         corner of the ship
   * @param myDisplayInfo    contains information about what to display to the
   *                         user depending on whether or not the t-shaped ship
   *                         has been hit
   * @param enemyDisplayInfo is the same as the parameter above but contains info
   *                         to display to the enemy
   */
  public TShapedShip(String name, Placement upperLeft, ShipDisplayInfo<T> myDisplayInfo,
      ShipDisplayInfo<T> enemyDisplayInfo) {
    super(makeCoords(upperLeft), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }

  /**
   * Constructs a t-shaped ship using constructor chaining (calls the constructor
   * above)
   *
   * @param data  is the character to be displayed if the ship has not been hit
   *              (from the player's own perspective) or has been hit (from the
   *              enemy's perspective)
   * @param onHit is the character to be displayed if the ship has been hit (from
   *              the player's own perspective)
   */
  public TShapedShip(String name, Placement upperLeft, T data, T onHit) {
    this(name, upperLeft, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<>(null, data));
  }

  @Override
  public String getName() {
    return name;
  }

  // Moves a TShapedShip to a different spot on the board, possible in a different
  // orientation
  @Override
  public void move(Placement newPlacement) {
    Coordinate upperLeft = newPlacement.getWhere();
    int row = upperLeft.getRow();
    int column = upperLeft.getColumn();
    char orientation = newPlacement.getOrientation();
    // Populate an ArrayList with the new Coordinates in a specified order
    ArrayList<Coordinate> newCoords = new ArrayList<Coordinate>();
    if (orientation == 'U') {
      Coordinate b1 = new Coordinate(row, column + 1);
      Coordinate b2 = new Coordinate(row + 1, column);
      Coordinate b3 = new Coordinate(row + 1, column + 1);
      Coordinate b4 = new Coordinate(row + 1, column + 2);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
    }
    if (orientation == 'L') {
      Coordinate b1 = new Coordinate(row + 1, column);
      Coordinate b2 = new Coordinate(row + 2, column + 1);
      Coordinate b3 = new Coordinate(row + 1, column + 1);
      Coordinate b4 = new Coordinate(row, column + 1);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
    }
    if (orientation == 'D') {
      Coordinate b1 = new Coordinate(row + 1, column + 1);
      Coordinate b2 = new Coordinate(row, column + 2);
      Coordinate b3 = new Coordinate(row, column + 1);
      Coordinate b4 = new Coordinate(row, column);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
    }
    if (orientation == 'R') {
      Coordinate b1 = new Coordinate(row + 1, column + 1);
      Coordinate b2 = new Coordinate(row, column);
      Coordinate b3 = new Coordinate(row + 1, column);
      Coordinate b4 = new Coordinate(row + 2, column);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
    }
    Collection<Boolean> hitsCollection = myPieces.values();
    ArrayList<Boolean> hits = new ArrayList<Boolean>(hitsCollection);
    LinkedHashMap<Coordinate, Boolean> newPieces = new LinkedHashMap<Coordinate, Boolean>();
    for (int i = 0; i < newCoords.size(); i++) {
      newPieces.put(newCoords.get(i), hits.get(i));
    }
    myPieces = newPieces;
  }

}
