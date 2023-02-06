package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class V2ShipFactoryTest {

  private void checkShip(Ship<Character> testShip, String expectedName, char expectedLetter,
      Coordinate... expectedLocs) {
    assertEquals(expectedName, testShip.getName());
    for (Coordinate c : expectedLocs) {
      assert (testShip.occupiesCoordinates(c));
      assertEquals(expectedLetter, testShip.getDisplayInfoAt(c, true));
    }
  }

  @Test
  public void test_build_submarine() {
    V2ShipFactory f = new V2ShipFactory();
    // Check a submarine placed at A0v
    Placement v0_0 = new Placement("A0v");
    Ship<Character> sub1 = f.makeSubmarine(v0_0);
    checkShip(sub1, "submarine", 's', new Coordinate(0, 0), new Coordinate(1, 0));
    // Check a submarine placed at B1H
    Placement H1_1 = new Placement("B1H");
    Ship<Character> sub2 = f.makeSubmarine(H1_1);
    checkShip(sub2, "submarine", 's', new Coordinate("B1"), new Coordinate("B2"));
  }

  @Test
  public void test_build_battleship() {
    V2ShipFactory f = new V2ShipFactory();
    // Check a battleship placed at B1U
    Ship<Character> bshipUp = f.makeBattleship(new Placement(new Coordinate("B1"), 'u'));
    checkShip(bshipUp, "battleship", 'b', new Coordinate("B2"), new Coordinate("C1"), new Coordinate("C2"),
        new Coordinate("C3"));
    // Check a battleship placed at B1D
    Ship<Character> bshipDown = f.makeBattleship(new Placement(new Coordinate("B1"), 'd'));
    checkShip(bshipDown, "battleship", 'b', new Coordinate("B1"), new Coordinate("B2"), new Coordinate("B3"),
        new Coordinate("C2"));
    // Check a battleship placed at B1L
    Ship<Character> bshipLeft = f.makeBattleship(new Placement(new Coordinate("B1"), 'l'));
    checkShip(bshipLeft, "battleship", 'b', new Coordinate("C1"), new Coordinate("B2"), new Coordinate("C2"),
        new Coordinate("D2"));
    // Check a battleship placed at B1R
    Ship<Character> bshipRight = f.makeBattleship(new Placement(new Coordinate("B1"), 'r'));
    checkShip(bshipRight, "battleship", 'b', new Coordinate("B1"), new Coordinate("C1"), new Coordinate("D1"),
        new Coordinate("C2"));
  }

  @Test
  public void test_build_carrier() {
    V2ShipFactory f = new V2ShipFactory();
    // Check a carrier placed at C3U
    Ship<Character> cUp = f.makeCarrier(new Placement(new Coordinate("C3"), 'U'));
    checkShip(cUp, "carrier", 'c', new Coordinate("C3"), new Coordinate("D3"), new Coordinate("E3"),
        new Coordinate("F3"), new Coordinate("E4"), new Coordinate("F4"), new Coordinate("G4"));
    // Check a carrier placed at C3D
    Ship<Character> cDown = f.makeCarrier(new Placement(new Coordinate("C3"), 'D'));
    checkShip(cDown, "carrier", 'c', new Coordinate("C3"), new Coordinate("D3"), new Coordinate("E3"),
        new Coordinate("D4"), new Coordinate("E4"), new Coordinate("F4"), new Coordinate("G4"));
    // Check a carrier placed at C3L
    Ship<Character> cLeft = f.makeCarrier(new Placement(new Coordinate("C3"), 'L'));
    checkShip(cLeft, "carrier", 'c', new Coordinate("C5"), new Coordinate("C6"), new Coordinate("C7"),
        new Coordinate("D3"), new Coordinate("D4"), new Coordinate("D5"), new Coordinate("D6"));
    // Check a carrier placed at C3R
    Ship<Character> cRight = f.makeCarrier(new Placement(new Coordinate("C3"), 'R'));
    checkShip(cRight, "carrier", 'c', new Coordinate("C4"), new Coordinate("C5"), new Coordinate("C6"),
        new Coordinate("C7"), new Coordinate("D3"), new Coordinate("D4"), new Coordinate("D5"));
  }

  @Test
  public void test_build_destroyer() {
    V2ShipFactory f = new V2ShipFactory();
    // Check a carrier placed at A0v
    Placement v0_0 = new Placement("A0v");
    Ship<Character> dst1 = f.makeDestroyer(v0_0);
    checkShip(dst1, "destroyer", 'd', new Coordinate("A0"), new Coordinate("B0"), new Coordinate("C0"));
    // Check a carrier placed at B1H
    Placement H1_1 = new Placement("B1H");
    Ship<Character> dst2 = f.makeDestroyer(H1_1);
    checkShip(dst2, "destroyer", 'd', new Coordinate("B1"), new Coordinate("B2"), new Coordinate("B3"));
  }

}
