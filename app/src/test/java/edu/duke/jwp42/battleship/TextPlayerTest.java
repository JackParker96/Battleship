package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Collections;

import org.junit.jupiter.api.Test;

public class TextPlayerTest {

  private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
    V1ShipFactory shipFactory = new V1ShipFactory();
    return new TextPlayer("A", board, input, output, shipFactory);
  }

  @Test
  public void test_playOneTurn_invalid_inputs() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(3, 2, "A\nA0V\nA3\nC0\n*2\nA*\nA2\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_A0V = f.makeSubmarine(new Placement(new Coordinate("A0"), 'V'));
    enemyBoard.tryAddShip(sub_A0V);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A  | |  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "Please try again -> String representation of coordinate must have exactly two characters, but has 1 characters\n"
        +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "Please try again -> String representation of coordinate must have exactly two characters, but has 3 characters\n"
        +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "Please try again -> That coordinate is not on your enemy's board.\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "Please try again -> That coordinate is not on your enemy's board.\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "Please try again -> First character must be a letter of the alphabet, but is *\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "Please try again -> Second character must be a number between 0 and 9 but is *\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You missed!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_miss() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(3, 2, "A0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_B0H = f.makeSubmarine(new Placement(new Coordinate("B0"), 'H'));
    enemyBoard.tryAddShip(sub_B0H);
    player.theBoard.tryAddShip(sub_B0H);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A  | |  A                A  | |  A\n" +
        "B s|s|  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You missed!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_carrier() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(6, 2, "A0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(6, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> carrier_A0H = f.makeCarrier(new Placement(new Coordinate("A0"), 'H'));
    enemyBoard.tryAddShip(carrier_A0H);
    player.theBoard.tryAddShip(carrier_A0H);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean                   B's ocean\n" +
        "  0|1|2|3|4|5                    0|1|2|3|4|5\n" +
        "A c|c|c|c|c|c A                A  | | | | |  A\n" +
        "B  | | | | |  B                B  | | | | |  B\n" +
        "  0|1|2|3|4|5                    0|1|2|3|4|5\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a carrier!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_bship() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(4, 2, "A0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(4, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> bship_A0H = f.makeBattleship(new Placement(new Coordinate("A0"), 'H'));
    enemyBoard.tryAddShip(bship_A0H);
    player.theBoard.tryAddShip(bship_A0H);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean               B's ocean\n" +
        "  0|1|2|3                    0|1|2|3\n" +
        "A b|b|b|b A                A  | | |  A\n" +
        "B  | | |  B                B  | | |  B\n" +
        "  0|1|2|3                    0|1|2|3\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a battleship!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_destroyer() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(3, 2, "A0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> destroyer_A0H = f.makeDestroyer(new Placement(new Coordinate("A0"), 'H'));
    enemyBoard.tryAddShip(destroyer_A0H);
    player.theBoard.tryAddShip(destroyer_A0H);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A d|d|d A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a destroyer!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_sub() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(3, 2, "A0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V1ShipFactory f = new V1ShipFactory();
    Ship<Character> sub_A0V = f.makeSubmarine(new Placement(new Coordinate("A0"), 'V'));
    enemyBoard.tryAddShip(sub_A0V);
    player.theBoard.tryAddShip(sub_A0V);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A s| |  A                A  | |  A\n" +
        "B s| |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_read_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\n", bytes);
    String prompt = "Please enter a location for a ship:";
    Placement[] expected = new Placement[3];
    expected[0] = new Placement(new Coordinate(1, 2), 'V');
    expected[1] = new Placement(new Coordinate(2, 8), 'H');
    expected[2] = new Placement(new Coordinate(0, 4), 'V');
    for (int i = 0; i < expected.length; i++) {
      Placement p = player.readPlacement(prompt);
      assertEquals(p, expected[i]);
      assertEquals(prompt + "\n", bytes.toString());
      bytes.reset();
    }
  }

  // Could have written a helper method to make these test cases easier to write
  @Test
  public void test_doOnePlacement() throws IOException {
    String expectedHeader = "  0|1|2|3\n";
    String expectedBody = "A d|d|d|  A\n" + "B  | | |  B\n" + "C  | | |  C\n";
    String expectedBoard = expectedHeader + expectedBody + expectedHeader;
    V1ShipFactory f = new V1ShipFactory();
    String dstrPrompt = "Player A where do you want to place a Destroyer?\n";
    // Test a simple, correct placement of Destroyer at A0h
    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
    TextPlayer player1 = createTextPlayer(4, 3, "A0h\n", bytes1);
    player1.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(dstrPrompt + expectedBoard, bytes1.toString());
    // Test the EOFException error handling
    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
    TextPlayer player2 = createTextPlayer(4, 3, "", bytes2);
    assertThrows(EOFException.class, () -> {
      player2.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    });
    // Test error handling for an input of only 2 characters
    ByteArrayOutputStream bytes3 = new ByteArrayOutputStream();
    TextPlayer player3 = createTextPlayer(4, 3, "A0\nA0h\n", bytes3);
    player3.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(dstrPrompt
        + "Please try again -> Argument descr must have length 3 but instead has length 2\n"
        + dstrPrompt
        + expectedBoard, bytes3.toString());
    // Test error handling for input with invalid first character
    ByteArrayOutputStream bytes4 = new ByteArrayOutputStream();
    TextPlayer player4 = createTextPlayer(4, 3, "*0h\nA0h\n", bytes4);
    player4.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(dstrPrompt + "Please try again -> First character must be a letter of the alphabet, but is *\n"
        + dstrPrompt + expectedBoard, bytes4.toString());
    // Test error handling for input with invalid second character
    ByteArrayOutputStream bytes5 = new ByteArrayOutputStream();
    TextPlayer player5 = createTextPlayer(4, 3, "A*h\nA0h\n", bytes5);
    player5.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(dstrPrompt + "Please try again -> Second character must be a number between 0 and 9 but is *\n"
        + dstrPrompt + expectedBoard, bytes5.toString());
    // Test error handling for input with invalid orientation
    ByteArrayOutputStream bytes6 = new ByteArrayOutputStream();
    TextPlayer player6 = createTextPlayer(4, 3, "A0p\nA0h\n", bytes6);
    player6.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(dstrPrompt
        + "Please try again -> Third letter of argument descr must be 'H', 'V', 'h', or 'v' but instead is P\n"
        + dstrPrompt + expectedBoard, bytes6.toString());
    // Test error handling for input that goes off right side of board
    ByteArrayOutputStream bytes7 = new ByteArrayOutputStream();
    TextPlayer player7 = createTextPlayer(4, 3, "A2h\nA0h\n", bytes7);
    player7.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(
        dstrPrompt + "Please try again -> That placement is invalid: the ship goes off the right of the board.\n"
            + dstrPrompt + expectedBoard,
        bytes7.toString());
    // Test error handling for input that goes off bottom of board
    ByteArrayOutputStream bytes8 = new ByteArrayOutputStream();
    TextPlayer player8 = createTextPlayer(4, 3, "C0v\nA0h\n", bytes8);
    player8.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(
        dstrPrompt + "Please try again -> That placement is invalid: the ship goes off the bottom of the board.\n"
            + dstrPrompt + expectedBoard,
        bytes8.toString());
  }

  /**
  public String helper_diff(String s1, String s2) {
    StringBuilder sb = new StringBuilder(" \n");
    for (int i = 0; i < s1.length(); i++) {
      if (s1.charAt(i) != s2.charAt(i)) {
        sb.append("s1 has " + s1.charAt(i) + ", but s2 has " + s2.charAt(i) + "\n");
      }
    }
    return sb.toString();
  }
  */

  @Test
  public void test_doAttackingPhase_A_wins() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player1 = createTextPlayer(3, 2, "A0\nA1\n", bytes);
    // Create Player 2 with a different name from Player 1
    Board<Character> b2 = new BattleShipBoard<Character>(3, 2, 'X');
    String inputData = "A0\nA0\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    V1ShipFactory f = new V1ShipFactory();
    TextPlayer player2 = new TextPlayer("B", b2, input, output, f);
    //
    Ship<Character> s1 = f.makeSubmarine(new Placement(new Coordinate("A0"), 'H'));
    Ship<Character> s2 = f.makeSubmarine(new Placement(new Coordinate("A0"), 'H'));
    player1.theBoard.tryAddShip(s1);
    player2.theBoard.tryAddShip(s2);
    App app = new App(player1, player2);
    app.doAttackingPhase();
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A s|s|  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             A's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A s| |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "A has won the game!\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_doAttackingPhase_B_wins() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player1 = createTextPlayer(3, 2, "A0\nA0\n", bytes);
    // Create Player 2 with a different name from Player 1
    Board<Character> b2 = new BattleShipBoard<Character>(3, 2, 'X');
    String inputData = "A0\nA1\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    V1ShipFactory f = new V1ShipFactory();
    TextPlayer player2 = new TextPlayer("B", b2, input, output, f);
    //
    Ship<Character> s1 = f.makeSubmarine(new Placement(new Coordinate("A0"), 'H'));
    Ship<Character> s2 = f.makeSubmarine(new Placement(new Coordinate("A0"), 'H'));
    player1.theBoard.tryAddShip(s1);
    player2.theBoard.tryAddShip(s2);
    App app = new App(player1, player2);
    app.doAttackingPhase();
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A s|s|  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             A's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A s| |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             A's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A s| |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "B has won the game!\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_doPlacementPhase_and_hasLost() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String inputString = "A0h\nA0h\nC0h\n";
    BufferedReader input = new BufferedReader(new StringReader(inputString));
    PrintStream output = new PrintStream(bytes, true);
    AbstractShipFactory<Character> f = new V1ShipFactory();
    Board<Character> b = new BattleShipBoard<Character>(4, 3, 'X');
    TextPlayer player = new TextPlayer("A", b, input, output, f) {
      protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(1, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(1, "Destroyer"));
      }
    };
    player.doPlacementPhase();
    String expectedHeader = "  0|1|2|3\n";
    String expectedBody1 = "A  | | |  A\n" + "B  | | |  B\n" + "C  | | |  C\n";
    String expectedBoard1 = expectedHeader + expectedBody1 + expectedHeader;
    String expectedBody2 = "A s|s| |  A\n" + "B  | | |  B\n" + "C  | | |  C\n";
    String expectedBoard2 = expectedHeader + expectedBody2 + expectedHeader;
    String expectedBody3 = "A s|s| |  A\n" + "B  | | |  B\n" + "C d|d|d|  C\n";
    String expectedBoard3 = expectedHeader + expectedBody3 + expectedHeader;
    String expected = expectedBoard1 + "\n"
        + "--------------------------------------------------------------------------------\n" +
        "Player A: you are going to place the following ships (which are all " +
        "rectangular). For each ship, type the coordinate of the upper left " +
        "side of the ship, followed by either H (for horizontal) or V (for " +
        "vertical).  For example M4H would place a ship horizontally starting " +
        "at M4 and going to the right.  You have" +
        "\n\n" +
        "2 \"Submarines\" ships that are 1x2\n" +
        "3 \"Destroyers\" that are 1x3\n" +
        "3 \"Battleships\" that are 1x4\n" +
        "2 \"Carriers\" that are 1x6\n" +
        "--------------------------------------------------------------------------------\n" +
        "Player A where do you want to place a Submarine?\n" +
        expectedBoard2 +
        "Player A where do you want to place a Destroyer?\n" +
        "Please try again -> That placement is invalid: the ship overlaps another ship.\n" +
        "Player A where do you want to place a Destroyer?\n" +
        expectedBoard3;
    assertEquals(expected, bytes.toString());
    // Now let's test hasLost
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("A0"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("A1"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("C0"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("C1"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("C2"));
    assertEquals("A", player.hasLost());
  }

}
