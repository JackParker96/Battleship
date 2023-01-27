package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;

public class V1ShipFactoryTest {

  private void checkShip(Ship<Character> testShip, String expectedName, char expectedLetter,
      Coordinate... expectedLocs) {
    assertEquals(expectedName, testShip.getName());
    for (Coordinate c : expectedLocs) {
      assert (testShip.occupiesCoordinates(c));
      assertEquals(expectedLetter, testShip.getDisplayInfoAt(c));
    }
  }

  @Test
  public void test_build_submarine() {
    V1ShipFactory f = new V1ShipFactory();
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
    V1ShipFactory f = new V1ShipFactory();
    // Check a battleship placed at A0v
    Placement v0_0 = new Placement("A0v");
    Ship<Character> bship1 = f.makeBattleship(v0_0);
    checkShip(bship1, "battleship", 'b', new Coordinate("A0"), new Coordinate("B0"), new Coordinate("C0"),
        new Coordinate("D0"));
    // Check a battleship placed at B1H
    Placement H1_1 = new Placement("B1H");
    Ship<Character> bship2 = f.makeBattleship(H1_1);
    checkShip(bship2, "battleship", 'b', new Coordinate("B1"), new Coordinate("B2"), new Coordinate("B3"),
        new Coordinate("B4"));
  }

  @Test
  public void test_build_carrier() {
    V1ShipFactory f = new V1ShipFactory();
    // Check a carrier placed at A0v
    Placement v0_0 = new Placement("A0v");
    Ship<Character> carrier1 = f.makeCarrier(v0_0);
    checkShip(carrier1, "carrier", 'c', new Coordinate("A0"), new Coordinate("B0"), new Coordinate("C0"),
        new Coordinate("D0"), new Coordinate("E0"), new Coordinate("F0"));
    // Check a carrier placed at B1H
    Placement H1_1 = new Placement("B1H");
    Ship<Character> carrier2 = f.makeCarrier(H1_1);
    checkShip(carrier2, "carrier", 'c', new Coordinate("B1"), new Coordinate("B2"), new Coordinate("B3"),
        new Coordinate("B4"), new Coordinate("B5"), new Coordinate("B6"));
  }

  @Test
  public void test_build_destroyer() {
    V1ShipFactory f = new V1ShipFactory();
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
