package edu.duke.jwp42.battleship;

public interface ShipDisplayInfo<T> {
  public T getInfo(Coordinate where, boolean hit);
}
