package edu.duke.jwp42.battleship;

import java.util.HashSet;

public class RectangleShip extends BasicShip {
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

  public RectangleShip(Coordinate upperLeft, int width, int height) {
    super(makeCoords(upperLeft, width, height));
  }

}
