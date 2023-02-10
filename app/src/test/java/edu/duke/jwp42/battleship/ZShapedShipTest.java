package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class ZShapedShipTest {

  @Test
  public void test_move() {
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> carrA2u = f.makeCarrier(new Placement("A2u"));
    carrA2u.recordHitAt(new Coordinate("A2"));
    carrA2u.recordHitAt(new Coordinate("C2"));
    carrA2u.recordHitAt(new Coordinate("D3"));
    // Move U to D
    carrA2u.move(new Placement("A5d"));
    assert(carrA2u.occupiesCoordinates(new Coordinate("A5")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("B5")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("B6")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("C5")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("C6")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("D6")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("E6")));
    assert(carrA2u.wasHitAt(new Coordinate("B5")));
    assert(carrA2u.wasHitAt(new Coordinate("C6")));
    assert(carrA2u.wasHitAt(new Coordinate("E6")));

    carrA2u.move(new Placement("F2r"));
    assert(carrA2u.occupiesCoordinates(new Coordinate("F3")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("f4")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("f5")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("f6")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("g2")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("g3")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("g4")));
    assert(carrA2u.wasHitAt(new Coordinate("f4")));
    assert(carrA2u.wasHitAt(new Coordinate("f6")));
    assert(carrA2u.wasHitAt(new Coordinate("g3")));

    carrA2u.move(new Placement("I0L"));
    assert(carrA2u.occupiesCoordinates(new Coordinate("i2")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("i3")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("i4")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("j0")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("j1")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("j2")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("j3")));
    assert(carrA2u.wasHitAt(new Coordinate("i3")));
    assert(carrA2u.wasHitAt(new Coordinate("j0")));
    assert(carrA2u.wasHitAt(new Coordinate("j2")));
 
    carrA2u.move(new Placement("A0u"));
    assert(carrA2u.occupiesCoordinates(new Coordinate("a0")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("b0")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("c0")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("d0")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("C1")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("D1")));
    assert(carrA2u.occupiesCoordinates(new Coordinate("E1")));
    assert(carrA2u.wasHitAt(new Coordinate("a0")));
    assert(carrA2u.wasHitAt(new Coordinate("c0")));
    assert(carrA2u.wasHitAt(new Coordinate("d1")));
  }
  
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
