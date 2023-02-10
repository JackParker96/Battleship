package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.LinkedHashMap;

import org.junit.jupiter.api.Test;

public class RectangleShipTest {

  @Test
  public void test_move() {
    V2ShipFactory f = new V2ShipFactory();
    // Test moving a sub from vertical to horizontal
    Ship<Character> sub_B1v = f.makeSubmarine(new Placement(new Coordinate("B1"), 'v'));
    sub_B1v.recordHitAt(new Coordinate("C1"));
    sub_B1v.move(new Placement(new Coordinate("D2"), 'h'));
    assert (sub_B1v.occupiesCoordinates(new Coordinate("D2")));
    assert (sub_B1v.occupiesCoordinates(new Coordinate("D3")));
    assert (sub_B1v.wasHitAt(new Coordinate("D3")));
    assertFalse(sub_B1v.occupiesCoordinates(new Coordinate("B1")));
    // Test moving a destroyer from horizontal to vertical
    Ship<Character> dstr_E0h = f.makeDestroyer(new Placement(new Coordinate("E0"), 'h'));
    dstr_E0h.recordHitAt(new Coordinate("E1"));
    dstr_E0h.recordHitAt(new Coordinate("E2"));
    dstr_E0h.move(new Placement(new Coordinate("B2"), 'v'));
    assert (dstr_E0h.occupiesCoordinates(new Coordinate("B2")));
    assert (dstr_E0h.occupiesCoordinates(new Coordinate("C2")));
    assert (dstr_E0h.occupiesCoordinates(new Coordinate("D2")));
    assert (dstr_E0h.wasHitAt(new Coordinate("C2")));
    assert (dstr_E0h.wasHitAt(new Coordinate("D2")));
    // Test moving a sub from horizontal to horizontal
    Ship<Character> sub_C2h = f.makeSubmarine(new Placement("C2h"));
    sub_C2h.recordHitAt(new Coordinate("C2"));
    sub_C2h.move(new Placement("D0h"));
    assert (sub_C2h.occupiesCoordinates(new Coordinate("D0")));
    assert (sub_C2h.occupiesCoordinates(new Coordinate("D1")));
    assert(sub_C2h.wasHitAt(new Coordinate("D0")));
    // Test moving a destroyer from vertical to vertical
  }

  @Test
  public void test_getCoordinates() {
    BasicShip<Character> s = new RectangleShip<Character>("ship", new Coordinate("A0"), 1, 2, 's', '*');
    Iterable<Coordinate> coords = s.getCoordinates();
    for (Coordinate c : coords) {
      assertNotEquals(c, new Coordinate("A1"));
      assert (c.equals(new Coordinate("A0")) || c.equals(new Coordinate("B0")));
    }
  }

  @Test
  public void test_getDisplayInfoAt() {
    BasicShip<Character> s = new RectangleShip<Character>("submarine", new Coordinate("B1"), 2, 3, 's', '*');
    assertEquals("submarine", s.getName());
    assertThrows(IllegalArgumentException.class, () -> s.getDisplayInfoAt(new Coordinate("A1"), true));
    s.recordHitAt(new Coordinate(1, 1));
    assertEquals('*', s.getDisplayInfoAt(new Coordinate(1, 1), true));
    assertEquals('s', s.getDisplayInfoAt(new Coordinate("B2"), true));
    assertNull(s.getDisplayInfoAt(new Coordinate("B2"), false));
    assertEquals('s', s.getDisplayInfoAt(new Coordinate(1, 1), false));
  }

  @Test
  public void test_rectangle_ship_constructor() {
    BasicShip<Character> s1 = new RectangleShip<Character>("carrier", new Coordinate(1, 2), 3, 2, 's', '*');
    assert (s1.occupiesCoordinates(new Coordinate(1, 2)));
    assert (s1.occupiesCoordinates(new Coordinate(1, 3)));
    assert (s1.occupiesCoordinates(new Coordinate(1, 4)));
    assert (s1.occupiesCoordinates(new Coordinate(2, 2)));
    assert (s1.occupiesCoordinates(new Coordinate(2, 3)));
    assert (s1.occupiesCoordinates(new Coordinate(2, 4)));
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
    assert (s1.isSunk());
    assert (s1.wasHitAt(new Coordinate("A0")));
    assert (s1.wasHitAt(new Coordinate("A1")));
    assert (s1.wasHitAt(new Coordinate("B0")));
    assert (s1.wasHitAt(new Coordinate("B1")));
    assert (s1.wasHitAt(new Coordinate("C0")));
    assert (s1.wasHitAt(new Coordinate("C1")));
  }

  /**
   * public void test_makeCoords_helper(Coordinate c, int w, int h,
   * HashSet<Coordinate> expected) {
   * RectangleShip s1 = new RectangleShip();
   * HashSet<Coordinate> actual = s1.makeCoords(c, w, h);
   * assertEquals(actual, expected);
   * }
   * 
   * @Test
   *       public void test_makeCoords() {
   *       Coordinate c = new Coordinate(1, 2);
   *       HashSet<Coordinate> expected1 = new HashSet<Coordinate>();
   *       expected1.add(new Coordinate(1, 2));
   *       expected1.add(new Coordinate(1, 3));
   *       expected1.add(new Coordinate(1, 4));
   *       expected1.add(new Coordinate(2, 2));
   *       expected1.add(new Coordinate(2, 3));
   *       expected1.add(new Coordinate(2, 4));
   *       test_makeCoords_helper(c, 3, 2, expected1);
   *       }
   */

}
