package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TShapedShipTest {

  @Test
  public void test_getCoordinates() {
    Ship<Character> s = new TShapedShip<Character>("Mr. T Ship", new Placement(new Coordinate("A0"), 'U'), 'b', '*');
    Iterable<Coordinate> coords = s.getCoordinates();
    for (Coordinate c : coords) {
      assertNotEquals(new Coordinate("A0"), c);
      assert(c.equals(new Coordinate("A1")) || c.equals(new Coordinate("B0")) || c.equals(new Coordinate("B1")) || c.equals(new Coordinate("B2")));
    }
  }

  @Test
  public void test_TShapedShip_constructor() {
    Ship<Character> upTShip = new TShapedShip<Character>("Up", new Placement(new Coordinate("B0"), 'U'), 'b', '*');
    assertEquals("Up", upTShip.getName());
    assert(upTShip.occupiesCoordinates(new Coordinate("B1")));
    assert(upTShip.occupiesCoordinates(new Coordinate("C0")));
    assert(upTShip.occupiesCoordinates(new Coordinate("C1")));
    assert(upTShip.occupiesCoordinates(new Coordinate("C2")));
    Ship<Character> downTShip = new TShapedShip<Character>("Down", new Placement(new Coordinate("B0"), 'D'), 'b', '*');
    assert(downTShip.occupiesCoordinates(new Coordinate("B0")));
    assert(downTShip.occupiesCoordinates(new Coordinate("B1")));
    assert(downTShip.occupiesCoordinates(new Coordinate("B2")));
    assert(downTShip.occupiesCoordinates(new Coordinate("C1")));
    Ship<Character> leftTShip = new TShapedShip<Character>("Left", new Placement(new Coordinate("B0"), 'l'), 'b', '*');
    assert(leftTShip.occupiesCoordinates(new Coordinate("C0")));
    assert(leftTShip.occupiesCoordinates(new Coordinate("B1")));
    assert(leftTShip.occupiesCoordinates(new Coordinate("C1")));
    assert(leftTShip.occupiesCoordinates(new Coordinate("D1")));
    Ship<Character> rightTShip = new TShapedShip<Character>("Left", new Placement(new Coordinate("B0"), 'r'), 'b', '*');
    assert(rightTShip.occupiesCoordinates(new Coordinate("B0")));
    assert(rightTShip.occupiesCoordinates(new Coordinate("C0")));
    assert(rightTShip.occupiesCoordinates(new Coordinate("D0")));
    assert(rightTShip.occupiesCoordinates(new Coordinate("C1")));
  }
  
}
