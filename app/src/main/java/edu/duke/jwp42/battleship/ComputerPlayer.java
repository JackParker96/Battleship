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

  public ComputerPlayer(AbstractShipFactory<Character> factory, Board<Character> b, BufferedReader input, PrintStream out, String name) {
    super(factory, b, new BufferedReader(new StringReader("A0h\nb0h\nc0h\nd0h\ne0h\nf0d\nh0d\nj0d\nl0r\nn0r\n" +
        "f\nA0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\nA0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\nA0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\nA0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n" +
        "f\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\nf\na0\n")), out, name);    
  }

  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException {
    Coordinate c = new Coordinate(input.readLine());
    enemyBoard.fireAt(c);
    if (enemyBoard.whatIsAtForEnemy(c) != null) {
      System.out.println("The computer hit your ship at A0!");
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
