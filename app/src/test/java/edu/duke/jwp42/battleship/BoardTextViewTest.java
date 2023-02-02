package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class BoardTextViewTest {

  private void emptyBoardHelper(int w, int h, String expectedHeader, String expectedBody) {
    Board<Character> b1 = new BattleShipBoard<Character>(w, h, 'X');
    BoardTextView view = new BoardTextView(b1);
    //assertEquals(expectedHeader, view.makeHeader());
    //assertEquals(expectedBody, view.makeBody());
    //assertThrows(IllegalArgumentException.class, () -> view.makeRow(26));
    //assertThrows(IllegalArgumentException.class, () -> view.makeRow(-1));
    String expected = expectedHeader + expectedBody + expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }

  @Test
  public void test_displayEnemyBoard() {
    Board<Character> b = new BattleShipBoard<Character>(2, 2, 'X');
    BoardTextView view = new BoardTextView(b);
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub = f.makeSubmarine(new Placement(new Coordinate("A0"), 'H'));
    b.tryAddShip(sub);
    String myView1 =
      " 0|1\n" +
      "As|sA\n" +
      "B | B\n" +
      " 0|1\n";
    assertEquals(myView1, view.displayMyOwnBoard());
    String enemyView1 =
      " 0|1\n" +
      "A | A\n" +
      "B | B\n" +
      " 0|1\n";
    assertEquals(enemyView1, view.displayEnemyBoard());
    b.fireAt(new Coordinate("B0"));
    String myView2 =
      " 0|1\n" +
      "As|sA\n" +
      "B | B\n" +
      " 0|1\n";
    assertEquals(myView2, view.displayMyOwnBoard());
    String enemyView2 =
      " 0|1\n" +
      "A | A\n" +
      "BX| B\n" +
      " 0|1\n";
    assertEquals(enemyView2, view.displayEnemyBoard());
    b.fireAt(new Coordinate("A0"));
    String myView3 =
      " 0|1\n" +
      "A*|sA\n" +
      "B | B\n" +
      " 0|1\n";
    assertEquals(myView3, view.displayMyOwnBoard());
    String enemyView3 =
      " 0|1\n" +
      "As| A\n" +
      "BX| B\n" +
      " 0|1\n";
    assertEquals(enemyView3, view.displayEnemyBoard());
  }

  @Test
  public void test_display_empty_2by2() {
    String expectedHeader = " 0|1\n";
    String expectedBody = "A | A\n" + "B | B\n";
    emptyBoardHelper(2, 2, expectedHeader, expectedBody);
  }

  @Test
  public void test_display_empty_3by2() {
    String expectedHeader = " 0|1|2\n";
    String expectedBody = "A | | A\n" + "B | | B\n";
    emptyBoardHelper(3, 2, expectedHeader, expectedBody);
  }

  @Test
  public void test_display_empty_3by5() {
    String expectedHeader = " 0|1|2\n";
    String expectedBody = "A | | A\n" + "B | | B\n" + "C | | C\n" + "D | | D\n" + "E | | E\n";
    emptyBoardHelper(3, 5, expectedHeader, expectedBody);
  }

  @Test
  public void test_invalid_board_size() {
    Board<Character> wideBoard = new BattleShipBoard<Character>(11, 20, 'X');
    Board<Character> tallBoard = new BattleShipBoard<Character>(10, 27, 'X');
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(wideBoard));
    assertThrows(IllegalArgumentException.class, () -> new BoardTextView(tallBoard));
  }
  
  @Test
  public void test_display_nonempty_4by3() {
    Board<Character> b = new BattleShipBoard<Character>(4, 3, 'X');
    BoardTextView view = new BoardTextView(b);
    String expectedHeader = " 0|1|2|3\n";
    //assertEquals(expectedHeader, view.makeHeader());
    Coordinate c1 = new Coordinate(0, 0);
    Coordinate c2 = new Coordinate(2, 3);
    Coordinate c3 = new Coordinate(1, 2);
    RectangleShip<Character> s1 = new RectangleShip<Character>(c1, 's', '*');
    RectangleShip<Character> s2 = new RectangleShip<Character>(c2, 's', '*');
    RectangleShip<Character> s3 = new RectangleShip<Character>(c3, 's', '*');
    b.tryAddShip(s1);
    String expectedBody = "As| | | A\n" + "B | | | B\n" + "C | | | C\n";
    String expected = expectedHeader + expectedBody + expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
    b.tryAddShip(s2);
    expectedBody = "As| | | A\n" + "B | | | B\n" + "C | | |sC\n";
    expected = expectedHeader + expectedBody + expectedHeader;
    assertEquals(expected, view.displayMyOwnBoard());
  }
}
