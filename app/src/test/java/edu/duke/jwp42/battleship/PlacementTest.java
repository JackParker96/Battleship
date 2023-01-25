package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlacementTest {
  @Test
  public void test_placement_standard_constructor() {
    Coordinate c1 = new Coordinate("A1");
    Placement p1 = new Placement(c1, 'H');
    Placement p2 = new Placement(c1, 'h');
    Placement p3 = new Placement(c1, 'v');
    Placement p4 = new Placement(c1, 'V');
    assertEquals(c1, p1.getWhere());
    assertEquals('H', p1.getOrientation());
    assertEquals('H', p2.getOrientation());
    assertEquals('V', p3.getOrientation());
    assertEquals('V', p4.getOrientation());
    assertThrows(IllegalArgumentException.class, () -> new Placement(c1, 'a'));
  }

  @Test
  public void test_toString() {
    Coordinate c1 = new Coordinate(2, 3);
    Placement p1 = new Placement(c1, 'h');
    assertEquals(p1.toString(), "C3H");
  }

  @Test
  public void test_equals() {
    Coordinate c1 = new Coordinate("A1");
    Coordinate c2 = new Coordinate("C4");
    Placement p1 = new Placement(c1, 'V');
    Placement p2 = new Placement(c1, 'v');
    Placement p3 = new Placement(c2, 'v');
    Placement p4 = new Placement(c1, 'h');
    assertEquals(p1, p1);
    assertEquals(p1, p2);
    assertNotEquals(p1, p3);
    assertNotEquals(p1, "A1V");
  }

  @Test
  public void test_hashCode() {
    Coordinate c1 = new Coordinate(3, 0);
    Coordinate c2 = new Coordinate(5, 6);
    Placement p1 = new Placement(c1, 'h');
    Placement p2 = new Placement(c1, 'H');
    Placement p3 = new Placement(c1, 'v');
    Placement p4 = new Placement(c2, 'h');
    assertEquals(p1.hashCode(), p2.hashCode());
    assertNotEquals(p1.hashCode(), p3.hashCode());
    assertNotEquals(p1.hashCode(), p4.hashCode());
    assertNotEquals(p3.hashCode(), p4.hashCode());
  }

  @Test
  public void test_string_constructor_valid_cases() {
    Placement p1 = new Placement("B3v");
    Placement p2 = new Placement("A0V");
    Placement p3 = new Placement("Z9H");
    Placement p4 = new Placement("G5h");
    Coordinate c1 = new Coordinate(1, 3);
    Coordinate c2 = new Coordinate(0, 0);
    Coordinate c3 = new Coordinate(25, 9);
    Coordinate c4 = new Coordinate(6, 5);
    assertEquals(c1, p1.getWhere());
    assertEquals(c2, p2.getWhere());
    assertEquals(c3, p3.getWhere());
    assertEquals(c4, p4.getWhere());
    assertEquals('V', p1.getOrientation());
    assertEquals('V', p2.getOrientation());
    assertEquals('H', p3.getOrientation());
    assertEquals('H', p4.getOrientation());
  }

  @Test
  public void test_string_constructor_invalid_cases() {
    assertThrows(IllegalArgumentException.class, () -> new Placement(""));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A1"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A10a"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A1a"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("(3v)"));
    assertThrows(IllegalArgumentException.class, () -> new Placement("A*h"));
  }

}
