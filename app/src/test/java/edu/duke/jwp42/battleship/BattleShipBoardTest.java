package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BattleShipBoardTest {
  private <T> void checkWhatIsAtBoard(BattleShipBoard<T> b, T[][] expected) {
    int w = b.getWidth();
    int h = b.getHeight();
    for (int i = 0; i < h; i++) {
      for (int j = 0; j < w; j++) {
        Coordinate c = new Coordinate(i, j);
        assertEquals(expected[i][j], b.whatIsAt(c));
      }
    }
  }

  @Test
  public void test_tryAddShip_with_placement_checking() {
    Board<Character> b = new BattleShipBoard<Character>(3, 4);
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
    BattleShipBoard<Character> b1 = new BattleShipBoard<Character>(2, 3);
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
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20);
    assertEquals(10, b1.getWidth());
    assertEquals(20, b1.getHeight());
  }

  @Test
  public void test_invalid_dimension() {
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, 0));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(0, 20));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(10, -5));
    assertThrows(IllegalArgumentException.class, () -> new BattleShipBoard<Character>(-8, 20));
  }
}
