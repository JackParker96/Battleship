package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashSet;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {

  public void test_makeCoords_helper(Coordinate c, int w, int h, HashSet<Coordinate> expected) {
    RectangleShip s1 = new RectangleShip();
    HashSet<Coordinate> actual = s1.makeCoords(c, w, h);
    assertEquals(actual, expected);
  }
  
  @Test
  public void test_makeCoords() {
    Coordinate c = new Coordinate(1, 2);
    HashSet<Coordinate> expected1 = new HashSet<Coordinate>();
    expected1.add(new Coordinate(1, 2));
    expected1.add(new Coordinate(1, 3));
    expected1.add(new Coordinate(1, 4));
    expected1.add(new Coordinate(2, 2));
    expected1.add(new Coordinate(2, 3));
    expected1.add(new Coordinate(2, 4));
    test_makeCoords_helper(c, 3, 2, expected1);
  }

}
