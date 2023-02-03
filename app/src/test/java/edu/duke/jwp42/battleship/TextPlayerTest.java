package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
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
    String expected = expectedBoard1 + "\n" + "--------------------------------------------------------------------------------\n" +
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
    assertFalse(player.hasLost());
    b.fireAt(new Coordinate("A0"));
    assertFalse(player.hasLost());
    b.fireAt(new Coordinate("A1"));
    assertFalse(player.hasLost());
    b.fireAt(new Coordinate("C0"));
    assertFalse(player.hasLost());
    b.fireAt(new Coordinate("C1"));
    assertFalse(player.hasLost());
    b.fireAt(new Coordinate("C2"));
    assert(player.hasLost());
  }

}
