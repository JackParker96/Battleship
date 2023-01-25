package edu.duke.jwp42.battleship;

public interface Board<T> {
  public int getWidth();

  public int getHeight();

  public boolean tryAddShip(Ship<T> toAdd);

  public T whatIsAt(Coordinate where);
}
