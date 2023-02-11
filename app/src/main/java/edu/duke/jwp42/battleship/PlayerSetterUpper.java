package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;

// This class takes care of figuring out which types of players will be playing the game and creating those two players
// main is the only function that calls setUpPlayers
// The purpose of putting this method in its own class is to make it easier to test
public class PlayerSetterUpper {
  protected final BufferedReader input;
  protected final PrintStream out;

  // Construct a PlayerSetterUpper
  public PlayerSetterUpper(BufferedReader input, PrintStream out) {
    this.input = input;
    this.out = out;
  }
  /**
   * Prompt user for which types of players will play the game (human, computer)
   * Construct the players
   *
   * @return an ArrayList containing the two players - we will pass this ArrayList
   *         to main
   */
  public ArrayList<Player> setUpPlayers() throws IOException {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
    Board<Character> b2 = new BattleShipBoard<Character>(10, 20, 'X');
    AbstractShipFactory<Character> factory = new V2ShipFactory();
    out.println("Please type two characters to indicate the types of players that will be playing Battleship\n" +
        "HH - human vs human\n" +
        "HC - human vs computer\n" +
        "CH - computer vs human\n" +
        "CC - computer vs computer");
    String playerTypes;
    while (true) {
      playerTypes = input.readLine().toUpperCase();
      if (!playerTypes.equals("HC") && !playerTypes.equals("HH") && !playerTypes.equals("CH")
          && !playerTypes.equals("CC")) {
        out.println("Please try again -> Input must be HH, HC, CH, or CC");
        continue;
      }
      break;
    }
    char firstPlayerType = playerTypes.charAt(0);
    char secondPlayerType = playerTypes.charAt(1);
    Player player1 = null;
    Player player2 = null;
    if (firstPlayerType == 'H') {
      player1 = new TextPlayer("A", b1, input, System.out, factory);
    } else {
      player1 = new ComputerPlayer(factory, b1);
    }
    if (secondPlayerType == 'H') {
      player2 = new TextPlayer("B", b2, input, System.out, factory);
    } else {
      player2 = new ComputerPlayer(factory, b2);
    }
    ArrayList<Player> players = new ArrayList<Player>();
    players.add(player1);
    players.add(player2);
    return players;
  }

}
