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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

public class TextPlayerTest {

  private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h, 'X');
    V2ShipFactory shipFactory = new V2ShipFactory();
    return new TextPlayer("A", board, input, output, shipFactory);
  }

  @Test
  public void test_playOneTurn_moveShip() throws IOException {
    String Prompt1 = "Please choose a ship you'd like to move, select any Coordinate occupied by that ship, and input the Coordinate.\n";
    String Prompt2 = "Ship located. Now please enter a new placement for the ship in the same format as you entered placements at the beginning of the game (e.g. M4V)\n";
    Board<Character> bA = new BattleShipBoard<Character>(3, 2, 'X');
    ByteArrayOutputStream bytesA = new ByteArrayOutputStream();
    String inputData = "m\nA0\nB2h\nm\nA0\nB1h\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytesA, true);
    V2ShipFactory f = new V2ShipFactory();
    TextPlayer playerA = new TextPlayer("A", bA, input, output, f);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    Ship<Character> sub_A0V = f.makeSubmarine(new Placement(new Coordinate("A0"), 'V'));
    bA.tryAddShip(sub_A0V);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A s| |  A                A  | |  A\n" +
        "B s| |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        Prompt1 + Prompt2+
        "A problem occurred. Either you entered a coordinate that doesn't have a ship, or you entered an invalid placement. Please select your action again. You can choose move ship again, or you can choose a different action\n" +
      Prompt1 + Prompt2;
    playerA.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytesA.toString());
  }

  @Test
  public void test_moveShipOnMyBoard() throws IOException {
    String Prompt1 = "Please choose a ship you'd like to move, select any Coordinate occupied by that ship, and input the Coordinate.\n";
    String Prompt2 = "Ship located. Now please enter a new placement for the ship in the same format as you entered placements at the beginning of the game (e.g. M4V)\n";
    Board<Character> b = new BattleShipBoard<Character>(7, 10, 'X');
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String inputData = "A0\nB1h\n" +
        "A\nE0\n" +
        "B2\nA6\nA6h\n" +
        "C2\nd0h\n" +
        "e1\ng0l\n" +
        "d4\na5d\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    V2ShipFactory f = new V2ShipFactory();
    TextPlayer player = new TextPlayer("A", b, input, output, f);
    // Make a sub and move it
    Ship<Character> subA0v = f.makeSubmarine(new Placement("A0v"));
    b.tryAddShip(subA0v);
    subA0v.recordHitAt(new Coordinate("B0"));
    player.moveShipOnMyBoard();
    String expected1 = Prompt1 + Prompt2;
    assertEquals(expected1, bytes.toString());
    bytes.reset();
    assert (subA0v.occupiesCoordinates(new Coordinate("B1")));
    assert (subA0v.occupiesCoordinates(new Coordinate("B2")));
    assert (subA0v.wasHitAt(new Coordinate("B2")));
    // Some invalid inputs
    player.moveShipOnMyBoard();
    String expected2 = Prompt1
        + "Please try again -> String representation of coordinate must have exactly two characters, but has 1 characters\n"
        +
        "A problem occurred. Either you entered a coordinate that doesn't have a ship, or you entered an invalid placement. Please select your action again. You can choose move ship again, or you can choose a different action\n";
    assertEquals(expected2, bytes.toString());
    bytes.reset();
    // More invalid inputs
    player.moveShipOnMyBoard();
    String expected3 = Prompt1 + Prompt2 +
        "Please try again -> Input must have length 3 but instead has length 2\n" +
        Prompt2
        + "A problem occurred. Either you entered a coordinate that doesn't have a ship, or you entered an invalid placement. Please select your action again. You can choose move ship again, or you can choose a different action\n";
    assertEquals(expected3, bytes.toString());
    bytes.reset();
    // Move a destroyer
    Ship<Character> destc0h = f.makeDestroyer(new Placement("c0h"));
    b.tryAddShip(destc0h);
    destc0h.recordHitAt(new Coordinate("C1"));
    player.moveShipOnMyBoard();
    String expected4 = Prompt1 + Prompt2;
    assertEquals(expected4, bytes.toString());
    bytes.reset();
    // Move a battleship
    Ship<Character> bshipe0d = f.makeBattleship(new Placement("e0d"));
    b.tryAddShip(bshipe0d);
    bshipe0d.recordHitAt(new Coordinate("F1"));
    player.moveShipOnMyBoard();
    String expected5 = Prompt1 + Prompt2;
    assertEquals(expected5, bytes.toString());
    bytes.reset();
    // Move a carrier
    Ship<Character> carrd4u = f.makeCarrier(new Placement("d4u"));
    b.tryAddShip(carrd4u);
    carrd4u.recordHitAt(new Coordinate("E4"));
    player.moveShipOnMyBoard();
    String expected6 = Prompt1 + Prompt2;
    assertEquals(expected6, bytes.toString());
    bytes.reset();
  }

  @Test
  public void test_playOneTurn_sonar_scan() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer playerA = createTextPlayer(3, 2, "K\ns\nA3\nA0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    playerA.playOneTurn(enemyBoard, enemyView, enemyName);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A  | |  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please try again -> You must type either f, m, or s to specify which action you'd like to take\n" +
        "Please enter a coordinate on your enemy board that you'd like to be the center of your sonar scan\n" +
        "Please try again -> Error - Center of sonar scan is not on the board\n" +
        "Result of sonar scan centered at coordinate (0, 0):\n\n" +
        "Submarines occupy 0 square(s)\n" +
        "Destroyers occupy 0 square(s)\n" +
        "Battleships occupy 0 square(s)\n" +
        "Carriers occupy 0 square(s)\n\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_sonarScanMyBoard() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader input = new BufferedReader(new StringReader(""));
    Board<Character> board = new BattleShipBoard<Character>(5, 4, 'X');
    V2ShipFactory f = new V2ShipFactory();
    TextPlayer player = new TextPlayer("A", board, input, output, f);
    Ship<Character> sub_b3h = f.makeSubmarine(new Placement(new Coordinate("B3"), 'h'));
    board.tryAddShip(sub_b3h);
    Ship<Character> dest_a2h = f.makeDestroyer(new Placement(new Coordinate("A2"), 'h'));
    board.tryAddShip(dest_a2h);
    Ship<Character> bship_a1r = f.makeBattleship(new Placement(new Coordinate("A1"), 'R'));
    board.tryAddShip(bship_a1r);
    Ship<Character> carrier_c0l = f.makeCarrier(new Placement(new Coordinate("C0"), 'l'));
    board.tryAddShip(carrier_c0l);
    String expected1 = "Result of sonar scan centered at coordinate (1, 2):\n\n" +
        "Submarines occupy 2 square(s)\n" +
        "Destroyers occupy 3 square(s)\n" +
        "Battleships occupy 4 square(s)\n" +
        "Carriers occupy 6 square(s)\n\n";
    player.sonarScanMyBoard(new Coordinate(1, 2));
    assertEquals(expected1, bytes.toString());
  }

  @Test
  public void test_playOneTurn_invalid_inputs() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(3, 2, "f\nA\nA0V\nA3\nC0\n*2\nA*\nA2\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> sub_A0V = f.makeSubmarine(new Placement(new Coordinate("A0"), 'V'));
    enemyBoard.tryAddShip(sub_A0V);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A  | |  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
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
    TextPlayer player = createTextPlayer(3, 2, "f\nA0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> sub_B0H = f.makeSubmarine(new Placement(new Coordinate("B0"), 'H'));
    enemyBoard.tryAddShip(sub_B0H);
    player.theBoard.tryAddShip(sub_B0H);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A  | |  A                A  | |  A\n" +
        "B s|s|  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You missed!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_carrier() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(6, 2, "F\nB0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(6, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> carrier_A0R = f.makeCarrier(new Placement(new Coordinate("A0"), 'R'));
    enemyBoard.tryAddShip(carrier_A0R);
    player.theBoard.tryAddShip(carrier_A0R);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean                   B's ocean\n" +
        "  0|1|2|3|4|5                    0|1|2|3|4|5\n" +
        "A  |c|c|c|c|  A                A  | | | | |  A\n" +
        "B c|c|c| | |  B                B  | | | | |  B\n" +
        "  0|1|2|3|4|5                    0|1|2|3|4|5\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a carrier!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_bship() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(4, 2, "f\nB1\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(4, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> bship_A0H = f.makeBattleship(new Placement(new Coordinate("A0"), 'd'));
    enemyBoard.tryAddShip(bship_A0H);
    player.theBoard.tryAddShip(bship_A0H);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean               B's ocean\n" +
        "  0|1|2|3                    0|1|2|3\n" +
        "A b|b|b|  A                A  | | |  A\n" +
        "B  |b| |  B                B  | | |  B\n" +
        "  0|1|2|3                    0|1|2|3\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a battleship!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_destroyer() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(3, 2, "f\nA0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> destroyer_A0H = f.makeDestroyer(new Placement(new Coordinate("A0"), 'H'));
    enemyBoard.tryAddShip(destroyer_A0H);
    player.theBoard.tryAddShip(destroyer_A0H);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A d|d|d A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a destroyer!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_playOneTurn_hit_sub() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(3, 2, "F\nA0\n", bytes);
    Board<Character> enemyBoard = new BattleShipBoard<Character>(3, 2, 'X');
    BoardTextView enemyView = new BoardTextView(enemyBoard);
    String enemyName = "B";
    V2ShipFactory f = new V2ShipFactory();
    Ship<Character> sub_A0V = f.makeSubmarine(new Placement(new Coordinate("A0"), 'V'));
    enemyBoard.tryAddShip(sub_A0V);
    player.theBoard.tryAddShip(sub_A0V);
    String expected = "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A s| |  A                A  | |  A\n" +
        "B s| |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n";
    player.playOneTurn(enemyBoard, enemyView, enemyName);
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_read_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(10, 20, "B2V\nC8H\na4v\nA0U\nA0D\nA0L\nA0R\n", bytes);
    String prompt = "Please enter a location for a ship:";
    Placement[] expected = new Placement[7];
    expected[0] = new Placement(new Coordinate(1, 2), 'V');
    expected[1] = new Placement(new Coordinate(2, 8), 'H');
    expected[2] = new Placement(new Coordinate(0, 4), 'V');
    expected[3] = new Placement(new Coordinate(0, 0), 'U');
    expected[4] = new Placement(new Coordinate(0, 0), 'D');
    expected[5] = new Placement(new Coordinate(0, 0), 'L');
    expected[6] = new Placement(new Coordinate(0, 0), 'R');
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
    V2ShipFactory f = new V2ShipFactory();
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
        + "Please try again -> Input must have length 3 but instead has length 2\n"
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
        + "Please try again -> Third letter of input must be in [H, V, U, D, L, R] but instead is P\n"
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
    // Test error handling for entering 'U', 'D', 'L', or 'R' for a
    // submarine/destroyer in V2
    ByteArrayOutputStream bytes9 = new ByteArrayOutputStream();
    TextPlayer player9 = createTextPlayer(4, 3, "C0u\nA0h\n", bytes9);
    player9.doOnePlacement("Destroyer", (p) -> f.makeDestroyer(p));
    assertEquals(
        dstrPrompt + "Please try again -> That placement is invalid: Invalid orientation for Destroyer\n"
            + dstrPrompt + expectedBoard,
        bytes9.toString());
    // Test error handling for enterin 'H' or 'V' for battleship/carrier in V2
    ByteArrayOutputStream bytes10 = new ByteArrayOutputStream();
    TextPlayer player10 = createTextPlayer(4, 3, "A0h\nA0u\n", bytes10);
    player10.doOnePlacement("Battleship", (p) -> f.makeBattleship(p));
    assertEquals(
        "Player A where do you want to place a Battleship?\n"
            + "Please try again -> Placement orientation for a battleship must be in [U, D, L, R] but instead was H\n"
            + "Player A where do you want to place a Battleship?\n" +
            "  0|1|2|3\n" +
            "A  |b| |  A\n" +
            "B b|b|b|  B\n" +
            "C  | | |  C\n" +
            "  0|1|2|3\n",
        bytes10.toString());
  }

  /**
   * public String helper_diff(String s1, String s2) {
   * StringBuilder sb = new StringBuilder(" \n");
   * for (int i = 0; i < s1.length(); i++) {
   * if (s1.charAt(i) != s2.charAt(i)) {
   * sb.append("s1 has " + s1.charAt(i) + ", but s2 has " + s2.charAt(i) + "\n");
   * }
   * }
   * return sb.toString();
   * }
   */

  @Test
  public void test_doAttackingPhase_A_wins() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player1 = createTextPlayer(3, 2, "f\nA0\nF\nA1\n", bytes);
    // Create Player 2 with a different name from Player 1
    Board<Character> b2 = new BattleShipBoard<Character>(3, 2, 'X');
    String inputData = "f\nA0\nf\nA0\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    V2ShipFactory f = new V2ShipFactory();
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
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             A's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player B:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player B what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A s| |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "A has won the game!\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_doAttackingPhase_B_wins() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player1 = createTextPlayer(3, 2, "f\nA0\nf\nA0\n", bytes);
    // Create Player 2 with a different name from Player 1
    Board<Character> b2 = new BattleShipBoard<Character>(3, 2, 'X');
    String inputData = "f\nA0\nf\nA1\n";
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    V2ShipFactory f = new V2ShipFactory();
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
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             A's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A  | |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player B:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player B what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             B's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A s| |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player A:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player A what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "Current state of the game:\n\n" +
        "     Your ocean             A's ocean\n" +
        "  0|1|2                    0|1|2\n" +
        "A *|s|  A                A s| |  A\n" +
        "B  | |  B                B  | |  B\n" +
        "  0|1|2                    0|1|2\n\n" +
        "Possible actions for Player B:\n\n" +
        "F - Fire at a square\n" +
        "M - Move a ship to another square (3 remaining)\n" +
        "S - Sonar scan (3 remaining)\n\n" +
        "Player B what would you like to do?\n" +
        "Please enter a coordinate on your enemy's board you'd like to fire at.\n" +
        "You hit a submarine!\n" +
        "B has won the game!\n";
    assertEquals(expected, bytes.toString());
  }

  @Test
  public void test_doPlacementPhase_and_hasLost() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    String inputString = "A0L\nA0L\nA2R\n";
    BufferedReader input = new BufferedReader(new StringReader(inputString));
    PrintStream output = new PrintStream(bytes, true);
    AbstractShipFactory<Character> f = new V2ShipFactory();
    Board<Character> b = new BattleShipBoard<Character>(7, 3, 'X');
    TextPlayer player = new TextPlayer("A", b, input, output, f) {
      protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(1, "Battleship"));
        shipsToPlace.addAll(Collections.nCopies(1, "Carrier"));
      }
    };
    player.doPlacementPhase();
    String expectedHeader = "  0|1|2|3|4|5|6\n";
    String expectedBody1 = "A  | | | | | |  A\n" +
        "B  | | | | | |  B\n" +
        "C  | | | | | |  C\n";
    String expectedBoard1 = expectedHeader + expectedBody1 + expectedHeader;
    String expectedBody2 = "A  |b| | | | |  A\n" +
        "B b|b| | | | |  B\n" +
        "C  |b| | | | |  C\n";
    String expectedBoard2 = expectedHeader + expectedBody2 + expectedHeader;
    String expectedBody3 = "A  |b| |c|c|c|c A\n" +
        "B b|b|c|c|c| |  B\n" +
        "C  |b| | | | |  C\n";
    String expectedBoard3 = expectedHeader + expectedBody3 + expectedHeader;
    String expectedPrompt = "Player A"
        + ": You have ten ships that you are going to place on your board one by one.\n\n" +
        "First, you will place two submarines, which are 1x2 rectangles.\n" +
        "Next, you will place three destroyers, which are 1x3 rectangles.\n\n" +
        "To place a submarine or a destroyer, you will type the upper left coordinate for the ship," +
        "followed by H (for a horizontal orientation) or V (vertical).\n" +
        "For example, M4H would place a ship horizontally starting at M4 and going to the right.\n\n" +
        "Next, you will place three battleships, which are shaped like a T.\n" +
        "Battleships can have four different orientations: Up, Down, Left, or Right:\n\n" +
        "  b      bbb       b        b\n" +
        " bbb      b       bb        bb\n " +
        "                  b        b\n\n" +
        " UP      DOWN     LEFT      RIGHT\n\n" +
        "To specify where you want to place your battleships, first type the coordinate of the upper left corner of the smallest rectangle containing your ship\n"
        +
        "Then type either U, D, L, or R to specify the orientation\n\n" +
        "Finally, you will place two carriers that are shaped like a Z and can have the same four orienations as a battleship\n\n"
        +
        "  c       c        ccc     cccc\n" +
        "  c       cc     cccc     ccc\n" +
        "  cc      cc\n" +
        "  cc       c\n" +
        "   c       c\n\n" +
        "  UP     DOWN     LEFT     RIGHT\n\n" +
        "You should specify how you want to place a carrier in the same way as for battleships\n\n";
    String expected = expectedBoard1 + "\n" + expectedPrompt + "\n" +
        "Player A where do you want to place a Battleship?\n" +
        expectedBoard2 +
        "Player A where do you want to place a Carrier?\n" +
        "Please try again -> That placement is invalid: the ship overlaps another ship.\n" +
        "Player A where do you want to place a Carrier?\n" +
        expectedBoard3;
    assertEquals(expected, bytes.toString());
    // Now let's test hasLost
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("A1"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("B0"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("B1"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("C1"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("B2"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("B3"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("B4"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("A3"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("A4"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("A5"));
    assertNull(player.hasLost());
    b.fireAt(new Coordinate("A6"));
    assertEquals("A", player.hasLost());
  }

}
