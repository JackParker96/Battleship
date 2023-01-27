package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {
  @Test
  public void test_getDisplayInfoAt() {
    BasicShip<Character> s = new RectangleShip<Character>("submarine", new Coordinate("B1"), 2, 3, 's', '*');
    assertEquals("submarine", s.getName());
    assertThrows(IllegalArgumentException.class, () -> s.getDisplayInfoAt(new Coordinate("A1")));
    s.recordHitAt(new Coordinate(1, 1));
    assertEquals('*', s.getDisplayInfoAt(new Coordinate(1, 1)));
    assertEquals('s', s.getDisplayInfoAt(new Coordinate("B2")));
  }
  
  @Test
  public void test_rectangle_ship_constructor() {
    BasicShip<Character> s1 = new RectangleShip<Character>("carrier", new Coordinate(1, 2), 3, 2, 's', '*');
    assert(s1.occupiesCoordinates(new Coordinate(1, 2)));
    assert(s1.occupiesCoordinates(new Coordinate(1, 3)));
    assert(s1.occupiesCoordinates(new Coordinate(1, 4)));
    assert(s1.occupiesCoordinates(new Coordinate(2, 2)));
    assert(s1.occupiesCoordinates(new Coordinate(2, 3)));
    assert(s1.occupiesCoordinates(new Coordinate(2, 4)));
  }

  @Test
  public void test_recordHitAt_and_wasHitAt() {
    BasicShip<Character> s1 = new RectangleShip<Character>("battleship", new Coordinate("A0"), 2, 3, 's', '*');
    assertFalse(s1.isSunk());
    assertThrows(IllegalArgumentException.class, () -> s1.recordHitAt(new Coordinate(0, 2)));
    assertThrows(IllegalArgumentException.class, () -> s1.wasHitAt(new Coordinate("D0")));
    s1.recordHitAt(new Coordinate("A0"));
    assertFalse(s1.isSunk());
    s1.recordHitAt(new Coordinate("A1"));
    assertFalse(s1.isSunk());
    s1.recordHitAt(new Coordinate("B0"));
    assertFalse(s1.isSunk());
    s1.recordHitAt(new Coordinate("B1"));
    assertFalse(s1.isSunk());
    s1.recordHitAt(new Coordinate("C0"));
    assertFalse(s1.isSunk());
    s1.recordHitAt(new Coordinate("C1"));
    assert(s1.isSunk());
    assert(s1.wasHitAt(new Coordinate("A0")));
    assert(s1.wasHitAt(new Coordinate("A1")));
    assert(s1.wasHitAt(new Coordinate("B0")));
    assert(s1.wasHitAt(new Coordinate("B1")));
    assert(s1.wasHitAt(new Coordinate("C0")));
    assert(s1.wasHitAt(new Coordinate("C1")));
  }
  
  /**
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
  */

}
