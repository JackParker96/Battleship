package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class SimpleShipDisplayInfoTest {
  @Test
  public void test_getInfo() {
    Character myData = 's';
    Character onHit = 'x';
    SimpleShipDisplayInfo<Character> displayInfo = new SimpleShipDisplayInfo<Character>(myData, onHit);
    assertEquals('x', displayInfo.getInfo(new Coordinate(0, 0), true));
    assertEquals('s', displayInfo.getInfo(new Coordinate(0, 1), false));
  }

}
