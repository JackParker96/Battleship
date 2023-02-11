package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;

/**
 * An App has two fields, one for each Player
 */
public class App {
  private final Player player1;
  private final Player player2;

  // Construct an App by passing in two Players
  public App(Player player1, Player player2) {
    this.player1 = player1;
    this.player2 = player2;
  }

  /**
   * Executes the entire placement phase of the game
   * Asks Player A to place all ten of their ships, then asks Player B to place
   * all 10 of their ships
   */
  public void doPlacementPhase() throws IOException {
    player1.doPlacementPhase();
    player2.doPlacementPhase();
  }

  /**
   * Executes the entire attacking phase of the game, alternating back and forth
   * between the two players until one of them has won
   */
  public void doAttackingPhase() throws IOException {
    while (true) {
      player1.playOneTurn(player2.theBoard, player2.view, player2.name);
      if (getWinningPlayer() != null) {
        player1.out.println(getWinningPlayer() + " has won the game!");
        return;
      }
      player2.playOneTurn(player1.theBoard, player1.view, player1.name);
      if (getWinningPlayer() != null) {
        player2.out.println(getWinningPlayer() + " has won the game!");
        return;
      }
    }
  }

  /**
   * Checks if the game has been won
   *
   * @return the name of the winning player or null if neither player has won the
   *         game yet
   */
  public String getWinningPlayer() {
    if (player1.theBoard.allShipsSunk()) {
      return player2.name;
    }
    if (player2.theBoard.allShipsSunk()) {
      return player1.name;
    }
    return null;
  }

  public static void main(String[] args) throws IOException {
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    PrintStream out = System.out;
    PlayerSetterUpper setup = new PlayerSetterUpper(input, out);
    ArrayList<Player> players = setup.setUpPlayers();
    Player player1 = players.get(0);
    Player player2 = players.get(1);
    App app = new App(player1, player2);
    app.doPlacementPhase();
    app.doAttackingPhase();
  }
}
