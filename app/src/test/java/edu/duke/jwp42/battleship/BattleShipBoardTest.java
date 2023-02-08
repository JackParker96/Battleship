package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  private <T> void checkWhatIsAtBoard(BattleShipBoard<T> b, T[][] expected) {
    int w = b.getWidth();
    int h = b.getHeight();
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        Coordinate c = new Coordinate(i, j);
        assertEquals(expected[i][j], b.whatIsAtForSelf(c));
      }
    }
  }

  private void sonarScanTestHelper(Board<Character> b, Coordinate center, int subs, int destroyers, int bships, int carriers) {
    HashMap<String, Integer> expected = new HashMap<String, Integer>();
    expected.put("submarine", subs);
    expected.put("destroyer", destroyers);
    expected.put("battleship", bships);
    expected.put("carrier", carriers);
    assertEquals(expected, b.doSonarScan(center));
  }
    
  @Test
  public void test_doSonarScan() {
    Board<Character> b = new BattleShipBoard<Character>(8, 8, 'X');
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> sub_b1h = f.makeSubmarine(new Placement(new Coordinate("B1"), 'H'));
    Ship<Character> bship_b3d = f.makeBattleship(new Placement(new Coordinate("B3"), 'D'));
    Ship<Character> carrier_d3r = f.makeCarrier(new Placement(new Coordinate("D3"), 'R'));
    Ship<Character> destroyer_g2h = f.makeDestroyer(new Placement(new Coordinate("G2"), 'H'));
    b.tryAddShip(sub_b1h);
    b.tryAddShip(destroyer_g2h);
    b.tryAddShip(carrier_d3r);
    b.tryAddShip(bship_b3d);
    sonarScanTestHelper(b, new Coordinate("D3"), 1, 1, 3, 6);
    assertThrows(IllegalArgumentException.class, () -> b.doSonarScan(new Coordinate("H8")));
    assertThrows(IllegalArgumentException.class, () -> b.doSonarScan(new Coordinate("i0")));
    sonarScanTestHelper(b, new Coordinate("D1"), 2, 0, 0, 2);
    sonarScanTestHelper(b, new Coordinate("D4"), 0, 1, 4, 7);
    sonarScanTestHelper(b, new Coordinate("H7"), 0, 0, 0, 0);
  }

  @Test
  public void test_allShipsSunk() {
    Board<Character> b = new BattleShipBoard<Character>(4, 3, 'X');
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_A0V = f.makeSubmarine(new Placement(new Coordinate("A0"), 'V'));
    Ship<Character> bship_C0H = f.makeBattleship(new Placement(new Coordinate("C0"), 'H'));
    b.tryAddShip(sub_A0V);
    b.tryAddShip(bship_C0H);
    assertFalse(b.allShipsSunk());
    b.fireAt(new Coordinate("A0"));
    assertFalse(b.allShipsSunk());
    b.fireAt(new Coordinate("B0"));
    assertFalse(b.allShipsSunk());
    b.fireAt(new Coordinate("C0"));
    assertFalse(b.allShipsSunk());
    b.fireAt(new Coordinate("C1"));
    assertFalse(b.allShipsSunk());
    b.fireAt(new Coordinate("C2"));
    assertFalse(b.allShipsSunk());
    b.fireAt(new Coordinate("C3"));
    assert (b.allShipsSunk());
  }

  @Test
  public void test_whatIsAtForEnemy() {
    Board<Character> b = new BattleShipBoard<Character>(4, 3, 'X');
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_A0V = f.makeSubmarine(new Placement(new Coordinate("A0"), 'V'));
    assertNull(b.tryAddShip(sub_A0V));
    Coordinate c1 = new Coordinate("A1");
    Coordinate c2 = new Coordinate("A2");
    Coordinate c3 = new Coordinate("B0");
    assertNull(b.fireAt(c1));
    assertNull(b.fireAt(c2));
    assertEquals('X', b.whatIsAtForEnemy(c1));
    assertEquals('X', b.whatIsAtForEnemy(c2));
    assertSame(sub_A0V, b.fireAt(c3));
    assertEquals('s', b.whatIsAtForEnemy(c3));
    assertEquals('*', b.whatIsAtForSelf(c3));
  }

  @Test
  public void test_fireAt() {
    Board<Character> b = new BattleShipBoard<Character>(3, 4, 'X');
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_B1V = f.makeSubmarine(new Placement(new Coordinate("B1"), 'v'));
    assertNull(b.tryAddShip(sub_B1V));
    assertNull(b.fireAt(new Coordinate("A0")));
    assertNull(b.fireAt(new Coordinate("A1")));
    assertNull(b.fireAt(new Coordinate("C3")));
    assertSame(sub_B1V, b.fireAt(new Coordinate("B1")));
    assertFalse(sub_B1V.isSunk());
    assertSame(sub_B1V, b.fireAt(new Coordinate("C1")));
    assertTrue(sub_B1V.isSunk());

  }

  @Test
  public void test_tryAddShip_with_placement_checking() {
    Board<Character> b = new BattleShipBoard<Character>(3, 4, 'X');
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_B1V = f.makeSubmarine(new Placement(new Coordinate("B1"), 'v'));
    Ship<Character> bship_A0H = f.makeBattleship(new Placement(new Coordinate("A0"), 'H'));
    Ship<Character> sub_B0H = f.makeSubmarine(new Placement(new Coordinate("B0"), 'h'));
    assertNull(b.tryAddShip(sub_B1V));
    assertEquals("That placement is invalid: the ship overlaps another ship.", b.tryAddShip(sub_B1V));
    assertEquals("That placement is invalid: the ship goes off the right of the board.", b.tryAddShip(bship_A0H));
    assertEquals("That placement is invalid: the ship overlaps another ship.", b.tryAddShip(sub_B0H));
  }

  @Test
  public void test_whatIsAt() {
    BattleShipBoard<Character> b1 = new BattleShipBoard<Character>(2, 3, 'X');
    Character[][] expected = new Character[3][2];
    checkWhatIsAtBoard(b1, expected);
    Coordinate c1 = new Coordinate(1, 0);
    RectangleShip<Character> s1 = new RectangleShip<Character>(c1, 's', '*');
    b1.tryAddShip(s1);
    expected[1][0] = 's';
    checkWhatIsAtBoard(b1, expected);
  }

  @Test
  public void test_width_and_height() {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }

  @Test
  public void test_invalid_dimension() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5, 'X'));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20, 'X'));
  }
}
