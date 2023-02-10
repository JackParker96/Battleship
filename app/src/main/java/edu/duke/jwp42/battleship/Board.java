package edu.duke.jwp42.battleship;

import java.util.HashMap;

public interface Board<T> {
  public int getWidth();

  public int getHeight();

  public boolean allShipsSunk();

  public Ship<T> fireAt(Coordinate c);

  public Boolean checkForValidShipMove(Ship<T> potentialMovedShip);

  public String tryAddShip(Ship<T> toAdd);

  public T whatIsAtForSelf(Coordinate where);

  public T whatIsAtForEnemy(Coordinate where);

  public Ship<T> getShipAt(Coordinate c);

  public HashMap<String, Integer> doSonarScan(Coordinate center);
}
