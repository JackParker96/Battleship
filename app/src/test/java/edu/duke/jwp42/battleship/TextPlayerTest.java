package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class TextPlayerTest {

  private TextPlayer createTextPlayer(int w, int h, String inputData, OutputStream bytes) {
    BufferedReader input = new BufferedReader(new StringReader(inputData));
    PrintStream output = new PrintStream(bytes, true);
    Board<Character> board = new BattleShipBoard<Character>(w, h);
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

  @Test
  public void test_do_one_placement() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(4, 3, "A0h\n", bytes);
    String expectedHeader = "  0|1|2|3\n";
    String expectedBody = "Ad|d|d| A\n" + "B | | | B\n" + "C | | | C\n";
    String expected = expectedHeader + expectedBody + expectedHeader;
    player.doOnePlacement();
    assertEquals("Player A where do you want to place a destroyer?\n" + expected + "\n", bytes.toString());
  }

  @Test
  public void test_doPlacementPhase() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    TextPlayer player = createTextPlayer(4, 3, "A0h\n", bytes);
    player.doPlacementPhase();
    String expectedHeader = "  0|1|2|3\n";
    String expectedBody = "Ad|d|d| A\n" + "B | | | B\n" + "C | | | C\n";
    String expectedBoard = expectedHeader + expectedBody + expectedHeader;
    String expected = "--------------------------------------------------------------------------------\n" +
        "Player A: you are going to place the following ships (which are all" +
        "rectangular). For each ship, type the coordinate of the upper left" +
        "side of the ship, followed by either H (for horizontal) or V (for" +
        "vertical).  For example M4H would place a ship horizontally starting" +
        "at M4 and going to the right.  You have" +
        "\n\n" +
        "2 \"Submarines\" ships that are 1x2\n" +
        "3 \"Destroyers\" that are 1x3\n" +
        "3 \"Battleships\" that are 1x4\n" +
        "2 \"Carriers\" that are 1x6\n" +
        "--------------------------------------------------------------------------------\n" +
        "Player A where do you want to place a destroyer?\n" +
        expectedBoard + "\n";
    assertEquals(expected, bytes.toString());
  }

}
