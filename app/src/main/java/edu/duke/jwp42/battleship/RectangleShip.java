package edu.duke.jwp42.battleship;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * Class RectangleShip<T> is generic in the way that it is displayed to the user
 * (in V1, T is always a Character)
 */
public class RectangleShip<T> extends BasicShip<T> {
  public final String name;

  /**
   * The purpose of this method is so that we can pass it to super() in the
   * constructor below
   *
   * @param upperLeft is the coordinate for the upper left part of the ship
   * @param width     is the width of the ship
   * @param height    is the height of the ship
   *
   *                  Returns a HashSet containing all the coordinates that a ship
   *                  with the specified upperLeft, width, and height would occupy
   */
  public static LinkedHashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height) {
    LinkedHashSet<Coordinate> ans = new LinkedHashSet<Coordinate>();
    for (int w = 0; w < width; w++) {
      for (int h = 0; h < height; h++) {
        Coordinate c = new Coordinate(upperLeft.getRow() + h, upperLeft.getColumn() + w);
        ans.add(c);
      }
    }
    return ans;
  }

  /**
   * Constructs a RectangleShip by calling the super constructor (the constructor
   * for a BasicShip)
   */
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo,
      ShipDisplayInfo<T> enemyDisplayInfo) {
    super(makeCoords(upperLeft, width, height), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }

  /**
   * Constructs a RectangleShip using constructor chaining
   * i.e. this constructor calls the constructor above.
   *
   * This is a convenient constructor to have, because it takes care of declaring
   * a SimpleShipDisplayInfo. All we have to do is pass in data and onHit
   */
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, T data, T onHit) {
    this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit),
        new SimpleShipDisplayInfo<>(null, data));
  }

  // Same as above, but creates a ship that only occupies one square
  public RectangleShip(Coordinate upperLeft, T data, T onHit) {
    this("testship", upperLeft, 1, 1, data, onHit);
  }

  @Override
  public String getName() {
    return name;
  }

  // Moves a RectangleShip to a new spot on the board, possibly in a different
  // orientation
  @Override
  public void move(Placement newPlacement) {
    Coordinate upperLeft = newPlacement.getWhere();
    int row = upperLeft.getRow();
    int column = upperLeft.getColumn();
    char orientation = newPlacement.getOrientation();
    // Populate an ArrayList with the new Coordinates in a specified order
    ArrayList<Coordinate> newCoords = new ArrayList<Coordinate>();
    int n = myPieces.size();
    if (orientation == 'V') {
      for (int i = 0; i < n; i++) {
        Coordinate c = new Coordinate(row + i, column);
        newCoords.add(c);
      }
    }
    if (orientation == 'H') {
      for (int i = 0; i < n; i++) {
        Coordinate c = new Coordinate(row, column + i);
        newCoords.add(c);
      }
    }
    // Modify myPieces by exchanging old Coordinates with new
    Collection<Boolean> hitsCollection = myPieces.values();
    ArrayList<Boolean> hits = new ArrayList<Boolean>(hitsCollection);
    LinkedHashMap<Coordinate, Boolean> newPieces = new LinkedHashMap<Coordinate, Boolean>();
    for (int i = 0; i < newCoords.size(); i++) {
      newPieces.put(newCoords.get(i), hits.get(i));
    }
    myPieces = newPieces;
  }

}
