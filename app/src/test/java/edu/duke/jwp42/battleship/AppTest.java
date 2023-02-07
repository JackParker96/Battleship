package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.Collections;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.ResourceAccessMode;
import org.junit.jupiter.api.parallel.ResourceLock;
import org.junit.jupiter.api.parallel.Resources;

class AppTest {

  @Test
  public void test_getWinningPlayerB() throws IOException {
    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
    String inputString1 = "A0h\nA0h\nC0h\n";
    String inputString2 = "A0h\nA0h\nC0h\n";
    BufferedReader input1 = new BufferedReader(new StringReader(inputString1));
    BufferedReader input2 = new BufferedReader(new StringReader(inputString2));
    PrintStream output1 = new PrintStream(bytes1, true);
    PrintStream output2 = new PrintStream(bytes2, true);
    AbstractShipFactory<Character> f = new V1ShipFactory();
    Board<Character> b1 = new BattleShipBoard<Character>(4, 3, 'X');
    Board<Character> b2 = new BattleShipBoard<Character>(4, 3, 'X');
    TextPlayer player1 = new TextPlayer("A", b1, input1, output1, f) {
      protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(1, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(1, "Destroyer"));
      }
    };
    TextPlayer player2 = new TextPlayer("B", b2, input2, output2, f) {
      protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(1, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(1, "Destroyer"));
      }
    };
    App app = new App(player1, player2);
    app.doPlacementPhase();
    assertNull(app.getWinningPlayer());
    b1.fireAt(new Coordinate("A0"));
    assertNull(app.getWinningPlayer());
    b1.fireAt(new Coordinate("A1"));
    assertNull(app.getWinningPlayer());
    b1.fireAt(new Coordinate("C0"));
    assertNull(app.getWinningPlayer());
    b1.fireAt(new Coordinate("C1"));
    assertNull(app.getWinningPlayer());
    b1.fireAt(new Coordinate("C2"));
    assertEquals("B", app.getWinningPlayer());
  }

  @Test
  public void test_getWinningPlayerA() throws IOException {
    ByteArrayOutputStream bytes1 = new ByteArrayOutputStream();
    ByteArrayOutputStream bytes2 = new ByteArrayOutputStream();
    String inputString1 = "A0h\nA0h\nC0h\n";
    String inputString2 = "A0h\nA0h\nC0h\n";
    BufferedReader input1 = new BufferedReader(new StringReader(inputString1));
    BufferedReader input2 = new BufferedReader(new StringReader(inputString2));
    PrintStream output1 = new PrintStream(bytes1, true);
    PrintStream output2 = new PrintStream(bytes2, true);
    AbstractShipFactory<Character> f = new V1ShipFactory();
    Board<Character> b1 = new BattleShipBoard<Character>(4, 3, 'X');
    Board<Character> b2 = new BattleShipBoard<Character>(4, 3, 'X');
    TextPlayer player1 = new TextPlayer("A", b1, input1, output1, f) {
      protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(1, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(1, "Destroyer"));
      }
    };
    TextPlayer player2 = new TextPlayer("B", b2, input2, output2, f) {
      protected void setupShipCreationList() {
        shipsToPlace.addAll(Collections.nCopies(1, "Submarine"));
        shipsToPlace.addAll(Collections.nCopies(1, "Destroyer"));
      }
    };
    App app = new App(player1, player2);
    app.doPlacementPhase();
    assertNull(app.getWinningPlayer());
    b2.fireAt(new Coordinate("A0"));
    assertNull(app.getWinningPlayer());
    b2.fireAt(new Coordinate("A1"));
    assertNull(app.getWinningPlayer());
    b2.fireAt(new Coordinate("C0"));
    assertNull(app.getWinningPlayer());
    b2.fireAt(new Coordinate("C1"));
    assertNull(app.getWinningPlayer());
    b2.fireAt(new Coordinate("C2"));
    assertEquals("A", app.getWinningPlayer());
  }

  @Test
  @ResourceLock(value = Resources.SYSTEM_OUT, mode = ResourceAccessMode.READ_WRITE)
  public void test_main() throws IOException {
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream out = new PrintStream(bytes, true);
    InputStream input = getClass().getClassLoader().getResourceAsStream("input.txt");
    assertNotNull(input);
    InputStream expectedStream = getClass().getClassLoader().getResourceAsStream("output.txt");
    assertNotNull(expectedStream);
    InputStream oldIn = System.in;
    PrintStream oldOut = System.out;
    try {
      System.setIn(input);
      System.setOut(out);
      App.main(new String[0]);
    }
    finally {
      System.setIn(oldIn);
      System.setOut(oldOut);
    }
    String expected = new String(expectedStream.readAllBytes());
    String actual = bytes.toString();
    assertEquals(expected, actual);
  }
}
