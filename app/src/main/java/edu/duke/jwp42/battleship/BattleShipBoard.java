package edu.duke.jwp42.battleship;

import java.util.ArrayList;

/**
 * BattleShipBoard<T> is generic in the type of the ships that occupy the board.
 * The type T specifies how a ship is displayed graphically (in v1 T is simply a Character)
 */
public class BattleShipBoard<T> implements Board<T> {
  private final int width;

  private final int height;

  private final ArrayList<Ship<T> > myShips;

  // Constructs a BattleShipBoard with specified width (number of columns) and height (number of rows)
  public BattleShipBoard(int w, int h) {
    // Board width must be strictly positive
    if (w <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's width must be positive but is " + w);
    }
    //Board height must be strictly positive
    if (h <= 0) {
      throw new IllegalArgumentException("BattleShipBoard's height must be positive but is " + h);
    }
    this.width = w;
    this.height = h;
    this.myShips = new ArrayList<Ship<T>>();
  }

  public int getHeight() {
    return height;
  }

  public int getWidth() {
    return width;
  }

  public boolean tryAddShip(Ship<T> toAdd) {
    myShips.add(toAdd);
    return true;
  }

  // Allows us to peek at any Coordinate on the board and see whether a ship occupies that Coordinate
  // If a ship does occupy that coordinate, this method returns the graphical symbol for that particular ship at that particular Coordinate
  public T whatIsAt(Coordinate where) {
    for (Ship<T> s: myShips) {
      if (s.occupiesCoordinates(where)){
        return s.getDisplayInfoAt(where);
      }
    }
    return null;
  }
}
