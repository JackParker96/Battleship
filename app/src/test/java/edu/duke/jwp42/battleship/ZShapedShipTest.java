package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

public class ZShapedShipTest {

  @Test
  public void test_invalid_orientation() {
    Placement p1 = new Placement(new Coordinate("A0"), 'V');
    Placement p2 = new Placement(new Coordinate("A0"), 'H');
    assertThrows(IllegalArgumentException.class, () -> new ZShapedShip<Character>("s1", p1, 'c', '*'));
    assertThrows(IllegalArgumentException.class, () -> new ZShapedShip<Character>("s2", p2, 'c', '*'));
  }
  
  @Test
  public void test_ZShapedShip_constructor() {
    Ship<Character> up = new ZShapedShip<Character>("up", new Placement(new Coordinate("D3"), 'U'), 'c', '*');
    assert (up.occupiesCoordinates(new Coordinate("D3")));
    assert (up.occupiesCoordinates(new Coordinate("E3")));
    assert (up.occupiesCoordinates(new Coordinate("F3")));
    assert (up.occupiesCoordinates(new Coordinate("G3")));
    assert (up.occupiesCoordinates(new Coordinate("F4")));
    assert (up.occupiesCoordinates(new Coordinate("G4")));
    assert (up.occupiesCoordinates(new Coordinate("H4")));
    assertFalse(up.occupiesCoordinates(new Coordinate("C3")));
    assertEquals("up", up.getName());
    Ship<Character> down = new ZShapedShip<Character>("down", new Placement(new Coordinate("D3"), 'D'), 'c', '*');
    assert (down.occupiesCoordinates(new Coordinate("D3")));
    assert (down.occupiesCoordinates(new Coordinate("E3")));
    assert (down.occupiesCoordinates(new Coordinate("F3")));
    assert (down.occupiesCoordinates(new Coordinate("E4")));
    assert (down.occupiesCoordinates(new Coordinate("F4")));
    assert (down.occupiesCoordinates(new Coordinate("G4")));
    assert (down.occupiesCoordinates(new Coordinate("H4")));
    Ship<Character> left = new ZShapedShip<Character>("left", new Placement(new Coordinate("D3"), 'L'), 'c', '*');
    assert (left.occupiesCoordinates(new Coordinate("D5")));
    assert (left.occupiesCoordinates(new Coordinate("D6")));
    assert (left.occupiesCoordinates(new Coordinate("D7")));
    assert (left.occupiesCoordinates(new Coordinate("E3")));
    assert (left.occupiesCoordinates(new Coordinate("E4")));
    assert (left.occupiesCoordinates(new Coordinate("E5")));
    assert (left.occupiesCoordinates(new Coordinate("E6")));
    Ship<Character> right = new ZShapedShip<Character>("right", new Placement(new Coordinate("D3"), 'R'), 'c', '*');
    assert (right.occupiesCoordinates(new Coordinate("D4")));
    assert (right.occupiesCoordinates(new Coordinate("D5")));
    assert (right.occupiesCoordinates(new Coordinate("D6")));
    assert (right.occupiesCoordinates(new Coordinate("D7")));
    assert (right.occupiesCoordinates(new Coordinate("E3")));
    assert (right.occupiesCoordinates(new Coordinate("E4")));
    assert (right.occupiesCoordinates(new Coordinate("E5")));
  }

}
