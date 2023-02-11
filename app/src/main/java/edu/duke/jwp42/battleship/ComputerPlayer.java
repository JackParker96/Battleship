package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.function.Function;

public class ComputerPlayer extends Player {

  public ComputerPlayer(AbstractShipFactory<Character> factory, Board<Character> b) {
    super(factory, b, new BufferedReader(new StringReader("A0h\nb0h\nc0h\nd0h\ne0h\nf0d\nh0d\nj0d\nl0r\nn0r\n" +
                                                          "a0\nb0\nc0\nd0\ne0\nf0\ng0\nh0\ni0\nj0\nk0\nl0\nm0\nn0\no0\np0\nq0\nr0\ns0\nt0\n" +
                                                          "a1\nb1\nc1\nd1\ne1\nf1\ng1\nh1\ni1\nj1\nk1\nl1\nm1\nn1\no1\np1\nq1\nr1\ns1\nt1\n" +
                                                          "a2\nb2\nc2\nd2\ne2\nf2\ng2\nh2\ni2\nj2\nk2\nl2\nm2\nn2\no2\np2\nq2\nr2\ns2\nt2\n" +
                                                          "a3\nb3\nc3\nd3\ne3\nf3\ng3\nh3\ni3\nj3\nk3\nl3\nm3\nn3\no3\np3\nq3\nr3\ns3\nt3\n" +
                                                          "a4\nb4\nc4\nd4\ne4\nf4\ng4\nh4\ni4\nj4\nk4\nl4\nm4\nn4\no4\np4\nq4\nr4\ns4\nt4\n" +
                                                          "a5\nb5\nc5\nd5\ne5\nf5\ng5\nh5\ni5\nj5\nk5\nl5\nm5\nn5\no5\np5\nq5\nr5\ns5\nt5\n" +
                                                          "a6\nb6\nc6\nd6\ne6\nf6\ng6\nh6\ni6\nj6\nk6\nl6\nm6\nn6\no6\np6\nq6\nr6\ns6\nt6\n" +
                                                          "a7\nb7\nc7\nd7\ne7\nf7\ng7\nh7\ni7\nj7\nk7\nl7\nm7\nn7\no7\np7\nq7\nr7\ns7\nt7\n" +
                                                          "a8\nb8\nc8\nd8\ne8\nf8\ng8\nh8\ni8\nj8\nk8\nl8\nm8\nn8\no8\np8\nq8\nr8\ns8\nt8\n" +
                                                          "a9\nb9\nc9\nd9\ne9\nf9\ng9\nh9\ni9\nj9\nk9\nl9\nm9\nn9\no9\np9\nq9\nr9\ns9\nt9\n")), System.out, "Computer");    
  }

  /**
   * Method for a ComputerPlayer to go through an entire turn
   * A ComputerPlayer always chooses to fire at the enemy board
   * It iterates over all squares of the board
   */
  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
    Coordinate c = new Coordinate(input.readLine());
    enemyBoard.fireAt(c);
    if (enemyBoard.whatIsAtForEnemy(c) != 'X') {
      String shipType = null;
      if (enemyBoard.whatIsAtForEnemy(c) == 's') {
        shipType = "submarine";
      }
      else if (enemyBoard.whatIsAtForEnemy(c) == 'd') {
        shipType = "destroyer";
      }
      else if (enemyBoard.whatIsAtForEnemy(c) == 'b') {
        shipType = "battleship";
      }
      else {
        shipType = "carrier";
      }
      System.out.println("The computer hit your " + shipType + " at (" + c.getRow() + ", " + c.getColumn() + ")!");
    }
  }

  // Checks if the player has lost the game (all ships sunk)
  public String hasLost() {
    if (theBoard.allShipsSunk()) {
      return name;
    }
    return null;
  }

  public void doPlacementPhase() throws IOException {
    for (String shipName : shipsToPlace) {
      Function<Placement, Ship<Character>> createFn = shipCreationFns.get(shipName);
      Placement p = new Placement(input.readLine());
      Ship<Character> s = createFn.apply(p);
      theBoard.tryAddShip(s);
    }
  }
}
