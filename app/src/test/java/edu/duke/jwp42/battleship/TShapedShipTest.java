package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TShapedShipTest {

  @Test
  public void test_move() {
    V2ShipFactory f = new V2ShipFactory();
    // Move TShip from U to D
    Ship<Character> bship_A2u = f.makeBattleship(new Placement("A2u"));
    bship_A2u.recordHitAt(new Coordinate("A3"));
    bship_A2u.recordHitAt(new Coordinate("B4"));
    bship_A2u.move(new Placement("D4d"));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("D4")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("D5")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("D6")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("E5")));
    assert(bship_A2u.wasHitAt(new Coordinate("E5")));
    assert(bship_A2u.wasHitAt(new Coordinate("D4")));
    assertFalse(bship_A2u.wasHitAt(new Coordinate("D5")));
    assertFalse(bship_A2u.wasHitAt(new Coordinate("D6")));
    assertFalse(bship_A2u.occupiesCoordinates(new Coordinate("A3")));
    // Move TShip from D to L
    bship_A2u.move(new Placement("G5l"));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("H5")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("H6")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("G6")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("I6")));
    assert(bship_A2u.wasHitAt(new Coordinate("H5")));
    assert(bship_A2u.wasHitAt(new Coordinate("G6")));
    // Move TShip from L to R
    bship_A2u.move(new Placement("H2r"));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("H2")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("I2")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("J2")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("I3")));
    assert(bship_A2u.wasHitAt(new Coordinate("I3")));
    assert(bship_A2u.wasHitAt(new Coordinate("J2")));
    // Move TShip from R to U
    bship_A2u.move(new Placement("e1u"));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("e2")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("f1")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("f2")));
    assert(bship_A2u.occupiesCoordinates(new Coordinate("f3")));
    assert(bship_A2u.wasHitAt(new Coordinate("e2")));
    assert(bship_A2u.wasHitAt(new Coordinate("f3")));
  }
  
  @Test
  public void test_invalid_orientation() {
    Placement p1 = new Placement(new Coordinate("A0"), 'V');
    Placement p2 = new Placement(new Coordinate("A0"), 'H');
    assertThrows(IllegalArgumentException.class, () -> new TShapedShip<Character>("s1", p1, 'c', '*'));
    assertThrows(IllegalArgumentException.class, () -> new TShapedShip<Character>("s2", p2, 'c', '*'));
  }
  
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
