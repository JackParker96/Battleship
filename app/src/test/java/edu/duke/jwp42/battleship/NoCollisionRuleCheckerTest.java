package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class NoCollisionRuleCheckerTest {
  @Test
  public void test_NoCollisionRuleChecker() {
    NoCollisionRuleChecker<Character> checker = new NoCollisionRuleChecker<Character>(null);
    Board<Character> b = new BattleShipBoard<Character>(3, 4, checker, 'X');
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_A0H = f.makeSubmarine(new Placement(new Coordinate("A0"), 'H'));
    Ship<Character> sub_C0H = f.makeSubmarine(new Placement(new Coordinate("C0"), 'H'));
    Ship<Character> sub_A1V = f.makeSubmarine(new Placement(new Coordinate("A1"), 'V'));
    Ship<Character> bship_A2V = f.makeBattleship(new Placement(new Coordinate("A2"), 'V'));
    Ship<Character> dstr_D0H = f.makeDestroyer(new Placement(new Coordinate("D0"), 'h'));
    assertNull(checker.checkPlacement(sub_A0H, b));
    b.tryAddShip(sub_A0H);
    b.tryAddShip(sub_C0H);
    assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkPlacement(sub_A1V, b));
    assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkPlacement(sub_A0H, b));
    assertNull(checker.checkPlacement(bship_A2V, b));
    b.tryAddShip(bship_A2V);
    assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkPlacement(dstr_D0H, b));
  }

  @Test
  public void test_no_collisions_and_in_bounds() {
    InBoundsRuleChecker<Character> innerChecker = new InBoundsRuleChecker<Character>(null);
    NoCollisionRuleChecker<Character> checker = new NoCollisionRuleChecker<Character>(innerChecker);
    Board<Character> b = new BattleShipBoard<Character>(3, 4, checker, 'X');
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> dstr_A0H = f.makeDestroyer(new Placement(new Coordinate("A0"), 'H'));
    Ship<Character> carrier = f.makeCarrier(new Placement(new Coordinate("D0"), 'H'));
    Ship<Character> sub_E0H = f.makeSubmarine(new Placement(new Coordinate("E0"), 'H'));
    Ship<Character> bship_A2V = f.makeBattleship(new Placement(new Coordinate("A2"), 'V'));
    assertNull(checker.checkPlacement(dstr_A0H, b));
    b.tryAddShip(dstr_A0H);
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", checker.checkPlacement(sub_E0H, b));
    assertEquals("That placement is invalid: the ship goes off the right of the board.", checker.checkPlacement(carrier, b));
    assertEquals("That placement is invalid: the ship overlaps another ship.", checker.checkPlacement(bship_A2V, b));
  }

}
