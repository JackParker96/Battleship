package edu.duke.jwp42.battleship;

import java.util.HashSet;
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
  public static final HashSet<Coordinate> makeCoords(Placement upperLeft) {
    HashSet<Coordinate> ans = new LinkedHashSet<Coordinate>();
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
      for (int i = 0; i < 4; i++) {
        Coordinate c = new Coordinate(row, column + 1 + i);
        ans.add(c);
      }
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + 1, column + i);
        ans.add(c);
      }
    }
    if (orientation == 'D') {
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + i, column);
        ans.add(c);
      }
      for (int i = 0; i < 4; i++) {
        Coordinate c = new Coordinate(row + 1 + i, column + 1);
        ans.add(c);
      }
    }
    if (orientation == 'L') {
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row, column + 2 + i);
        ans.add(c);
      }
      for (int i = 0; i < 4; i++) {
        Coordinate c = new Coordinate(row + 1, column + i);
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

  @Override
  public void move(Placement newPlacement) {
    
  }

}
