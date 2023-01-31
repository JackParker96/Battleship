package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {
  @Test
  public void test_InBoundsRuleChecker() {
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> bship_A0V = f.makeBattleship(new Placement(new Coordinate("A0"), 'V'));
    Ship<Character> bship_A0H = f.makeBattleship(new Placement(new Coordinate("A0"), 'H'));
    Ship<Character> dstrB0H = f.makeDestroyer(new Placement(new Coordinate("B0"), 'H'));
    Ship<Character> subD2V = f.makeSubmarine(new Placement(new Coordinate("D2"), 'V'));
    Ship<Character> carrC1H = f.makeCarrier(new Placement(new Coordinate("C1"), 'h'));
    Ship<Character> sub_E0h = f.makeSubmarine(new Placement(new Coordinate("E0"), 'h'));
    //Ship<Character> sub_off_the_top = f.makeSubmarine(new Placement(new Coordinate(-1, 2), 'h'));
    //Ship<Character> sub_off_the_left = f.makeSubmarine(new Placement(new Coordinate(0, -1), 'h'));
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<Character>(null);
    Board<Character> b = new BattleShipBoard<Character>(3, 4, checker);
    assertNull(checker.checkMyRule(bship_A0V, b));
    assertNull(checker.checkMyRule(dstrB0H, b));
    assertEquals("That placement is invalid: the ship goes off the right of the board.", checker.checkMyRule(bship_A0H, b));
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", checker.checkMyRule(subD2V, b));
    assertEquals("That placement is invalid: the ship goes off the right of the board.", checker.checkMyRule(carrC1H, b));
    assertEquals("That placement is invalid: the ship goes off the bottom of the board.", checker.checkMyRule(sub_E0h, b));
    //assertEquals("That placement is invalid: the ship goes off the top of the board.", checker.checkMyRule(sub_off_the_top, b));
    //assertEquals("That placement is invalid: the ship goes off the left of the board.", checker.checkMyRule(sub_off_the_left, b));
  }

}
