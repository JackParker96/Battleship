package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertFalse;

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
    InBoundsRuleChecker<Character> checker = new InBoundsRuleChecker<Character>(null);
    Board<Character> b = new BattleShipBoard<Character>(3, 4, checker);
    assert (checker.checkMyRule(bship_A0V, b));
    assert (checker.checkMyRule(dstrB0H, b));
    assertFalse(checker.checkMyRule(bship_A0H, b));
    assertFalse(checker.checkMyRule(subD2V, b));
    assertFalse(checker.checkMyRule(carrC1H, b));
  }

}
