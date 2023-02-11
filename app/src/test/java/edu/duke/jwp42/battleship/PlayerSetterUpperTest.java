package edu.duke.jwp42.battleship;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;

import org.junit.jupiter.api.Test;

public class PlayerSetterUpperTest {

  @Test
  public void test_setUpPlayers() throws IOException {
    String inputString = "m\ncc\n" +
                         "hc\n" +
                         "hh\n" +
                         "ch\n";
    ByteArrayOutputStream bytes = new ByteArrayOutputStream();
    PrintStream output = new PrintStream(bytes, true);
    BufferedReader input = new BufferedReader(new StringReader(inputString));
    PlayerSetterUpper setup = new PlayerSetterUpper(input, output);
    String prompt = "Please type two characters to indicate the types of players that will be playing Battleship\n" +
        "HH - human vs human\n" +
        "HC - human vs computer\n" +
        "CH - computer vs human\n" +
      "CC - computer vs computer\n";
    setup.setUpPlayers();
    String expected1 = prompt + "Please try again -> Input must be HH, HC, CH, or CC\n";
    assertEquals(expected1, bytes.toString());
    bytes.reset();
    setup.setUpPlayers();
    String expected2 = prompt;
    assertEquals(expected2, bytes.toString());
    bytes.reset();
    setup.setUpPlayers();
    String expected3 = prompt;
    assertEquals(expected3, bytes.toString());
    bytes.reset();
    setup.setUpPlayers();
    String expected4 = prompt;
    assertEquals(expected4, bytes.toString());
    bytes.reset();
  }
  
}
