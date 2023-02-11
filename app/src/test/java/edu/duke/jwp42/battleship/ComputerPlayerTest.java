package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import java.io.IOException;

public class ComputerPlayerTest {
  @Test
  public void test_computerPlayer() throws IOException {
    Board<Character> b = new BattleShipBoard<Character>(10, 20, 'X');
    V2ShipFactory f = new V2ShipFactory();
    Player cp = new ComputerPlayer(f, b);
    cp.doPlacementPhase();
    Board<Character> enemyBoard = new BattleShipBoard<Character>(10, 20, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    enemyBoard.tryAddShip(f.makeBattleship(new Placement("a0d")));
    enemyBoard.tryAddShip(f.makeBattleship(new Placement("T5u")));
    enemyBoard.tryAddShip(f.makeBattleship(new Placement("m8l")));
    enemyBoard.tryAddShip(f.makeSubmarine(new Placement("C0h")));
    enemyBoard.tryAddShip(f.makeDestroyer(new Placement("d0h")));
    enemyBoard.tryAddShip(f.makeCarrier(new Placement("k0r")));
    assertNull(cp.hasLost());
    for (int i = 0; i < 20; i++) {
      for (int j = 0; j < 10; j++) {
        cp.playOneTurn(enemyBoard, enemyView, enemyName);
        Coordinate c = new Coordinate(i, j);
        cp.theBoard.fireAt(c);
      }
    }
    assertEquals("Computer", cp.hasLost());
  }

}
