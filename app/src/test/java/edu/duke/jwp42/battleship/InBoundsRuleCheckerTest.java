package edu.duke.jwp42.battleship;

import org.junit.jupiter.api.Test;

public class InBoundsRuleCheckerTest {
  @Test
  public void test_InBoundsRuleChecker() {
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> bship1 = f.makeBattleship(new Placement(new Coordinate("A0"), 'V'));
    InBoundsRuleChecker<Character> checker1 = new InBoundsRuleChecker<Character>();
    Board<Character> b1 = new BattleShipBoard<Character>(3, 4);
  }

}
