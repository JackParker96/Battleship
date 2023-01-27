package edu.duke.jwp42.battleship;

import java.util.HashSet;

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
  public static HashSet<Coordinate> makeCoords(Coordinate upperLeft, int width, int height) {
    HashSet<Coordinate> ans = new HashSet<Coordinate>();
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
  public RectangleShip(String name, Coordinate upperLeft, int width, int height, ShipDisplayInfo<T> myDisplayInfo) {
    super(makeCoords(upperLeft, width, height), myDisplayInfo);
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
    this(name, upperLeft, width, height, new SimpleShipDisplayInfo<T>(data, onHit));
  }

  // Same as above, but creates a ship that only occupies one square
  public RectangleShip(Coordinate upperLeft, T data, T onHit) {
    this("testship", upperLeft, 1, 1, data, onHit);
  }

  @Override
  public String getName() {
    return name;
  }

}
