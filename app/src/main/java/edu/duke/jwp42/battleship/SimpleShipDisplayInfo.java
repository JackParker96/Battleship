package edu.duke.jwp42.battleship;

public class SimpleShipDisplayInfo<T> implements ShipDisplayInfo<T> {
  // The T to return if the ship has not been hit
  private T myData;

  // The T to display if the ship has been hit
  private T onHit;

  public SimpleShipDisplayInfo(T myData, T onHit) {
    this.myData = myData;
    this.onHit = onHit;
  }

  // Returns the T cooresponding to a particular Coordinate, depending on whether or not a ship has been hit at that Coordinate
  public T getInfo(Coordinate where, boolean hit) {
    if (hit == true) {
      return onHit;
    }
    return myData;
  }
}
