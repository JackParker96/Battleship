package edu.duke.jwp42.battleship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Class for making a carrier for Version 2
 */
public class ZShapedShip<T> extends BasicShip<T> {
  // The name of the ship
  public final String name;

  /**
   * A static method for creatign a HashSet of all the coordinates the Z-shaped
   * ship occupies
   * The purpose of this method is to pass to the super constructor in the
   * constructor below
   *
   * @param upperLeft is the Placement whose coordinate is the upper left
   *                  coordinate of the ship
   */
  public static final LinkedHashSet<Coordinate> makeCoords(Placement upperLeft) {
    LinkedHashSet<Coordinate> ans = new LinkedHashSet<Coordinate>();
    Coordinate where = upperLeft.getWhere();
    int row = where.getRow();
    int column = where.getColumn();
    char orientation = upperLeft.getOrientation();
    if (orientation != 'U' && orientation != 'D' && orientation != 'L' && orientation != 'R') {
      throw new IllegalArgumentException(
          "Placement orientation for a carrier must be in [U, D, L, R] but instead was " + orientation);
    }
    if (orientation == 'U') {
      for (int i = 0; i < 4; i++) {
        Coordinate c = new Coordinate(row + i, column);
        ans.add(c);
      }
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + 2 + i, column + 1);
        ans.add(c);
      }
    }
    if (orientation == 'R') {
      for (int i = 4; i > 0; i--) {
        Coordinate c = new Coordinate(row, column + i);
        ans.add(c);
      }
      for (int i = 2; i > -1; i--) {
        Coordinate c = new Coordinate(row + 1, column + i);
        ans.add(c);
      }
    }
    if (orientation == 'D') {
      for (int i = 4; i > 0; i--) {
        Coordinate c = new Coordinate(row + i, column + 1);
        ans.add(c);
      }
      for (int i = 2; i > -1; i--) {
        Coordinate c = new Coordinate(row + i, column);
        ans.add(c);
      }
    }
    if (orientation == 'L') {
      for (int i = 0; i < 4; i++) {
        Coordinate c = new Coordinate(row + 1, column + i);
        ans.add(c);
      }
      for (int i = 2; i < 5; i++) {
        Coordinate c = new Coordinate(row, column + i);
        ans.add(c);
      }
    }
    return ans;
  }

  /**
   * Constructs a ZShapedShip by making a call to the super constructor
   */
  public ZShapedShip(String name, Placement upperLeft, ShipDisplayInfo<T> myDisplayInfo,
      ShipDisplayInfo<T> enemyDisplayInfo) {
    super(makeCoords(upperLeft), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }

  // Constructs a ZShapedShip using constructor chaining
  public ZShapedShip(String name, Placement upperLeft, T data, T onHit) {
    this(name, upperLeft, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<>(null, data));
  }

  @Override
  public String getName() {
    return name;
  }

  // Moves a ZShapedShip to a different spot on the board, possible in a different orientation
  @Override
  public void move(Placement newPlacement) {
    Coordinate upperLeft = newPlacement.getWhere();
    int row = upperLeft.getRow();
    int column = upperLeft.getColumn();
    char orientation = newPlacement.getOrientation();
    // Populate an ArrayList with the new Coordinates in a specified order
    ArrayList<Coordinate> newCoords = new ArrayList<Coordinate>();
    if (orientation == 'U') {
      Coordinate b1 = new Coordinate(row, column);
      Coordinate b2 = new Coordinate(row + 1, column);
      Coordinate b3 = new Coordinate(row + 2, column);
      Coordinate b4 = new Coordinate(row + 3, column);
      Coordinate b5 = new Coordinate(row + 2, column + 1);
      Coordinate b6 = new Coordinate(row + 3, column + 1);
      Coordinate b7 = new Coordinate(row + 4, column + 1);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
      newCoords.add(b5);
      newCoords.add(b6);
      newCoords.add(b7);
    }
    if (orientation == 'L') {
      Coordinate b1 = new Coordinate(row + 1, column);
      Coordinate b2 = new Coordinate(row + 1, column + 1);
      Coordinate b3 = new Coordinate(row + 1, column + 2);
      Coordinate b4 = new Coordinate(row + 1, column + 3);
      Coordinate b5 = new Coordinate(row, column + 2);
      Coordinate b6 = new Coordinate(row, column + 3);
      Coordinate b7 = new Coordinate(row, column + 4);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
      newCoords.add(b5);
      newCoords.add(b6);
      newCoords.add(b7);
    }
    if (orientation == 'D') {
      Coordinate b1 = new Coordinate(row + 4, column + 1);
      Coordinate b2 = new Coordinate(row + 3, column + 1);
      Coordinate b3 = new Coordinate(row + 2, column + 1);
      Coordinate b4 = new Coordinate(row + 1, column + 1);
      Coordinate b5 = new Coordinate(row + 2, column);
      Coordinate b6 = new Coordinate(row + 1, column);
      Coordinate b7 = new Coordinate(row, column);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
      newCoords.add(b5);
      newCoords.add(b6);
      newCoords.add(b7);
    }
    if (orientation == 'R') {
      Coordinate b1 = new Coordinate(row, column + 4);
      Coordinate b2 = new Coordinate(row, column + 3);
      Coordinate b3 = new Coordinate(row, column + 2);
      Coordinate b4 = new Coordinate(row, column + 1);
      Coordinate b5= new Coordinate(row + 1, column + 2);
      Coordinate b6 = new Coordinate(row + 1, column + 1);
      Coordinate b7 = new Coordinate(row + 1, column);
      newCoords.add(b1);
      newCoords.add(b2);
      newCoords.add(b3);
      newCoords.add(b4);
      newCoords.add(b5);
      newCoords.add(b6);
      newCoords.add(b7);
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
