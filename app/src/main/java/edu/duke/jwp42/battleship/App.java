package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.InputStreamReader;

/**
 * An App has two fields, one for each TextPlayer
 */
public class App {
  private final TextPlayer player1;
  private final TextPlayer player2;

  // Construct an App by passing in two TextPlayers
  public App(TextPlayer player1, TextPlayer player2) {
    this.player1 = player1;
    this.player2 = player2;
  }

  // Call doPlacementPhase for Player 1 and Player 2
  public void doPlacementPhase() throws IOException {
    player1.doPlacementPhase();
    player2.doPlacementPhase();
  }

  /**
   * Checks if the game has been won
   *
   * @return player1 if player2 has lost, and vice versa, or return null if
   *         neither player has won yet
   */
  public TextPlayer getWinningPlayer() {
    if (player1.hasLost()) {
      return player2;
    }
    if (player2.hasLost()) {
      return player1;
    }
    return null;
  }

  public static void main(String[] args) throws IOException {
    Board<Character> b1 = new BattleShipBoard<Character>(10, 20, 'X');
    Board<Character> b2 = new BattleShipBoard<Character>(10, 20, 'X');
    AbstractShipFactory<Character> factory = new V1ShipFactory();
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    TextPlayer player1 = new TextPlayer("A", b1, input, System.out, factory);
    TextPlayer player2 = new TextPlayer("B", b2, input, System.out, factory);
    App app = new App(player1, player2);
    app.doPlacementPhase();
  }
}
