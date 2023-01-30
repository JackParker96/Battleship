package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;

/**
 * A class for a player in a text-based game of battleship
 */
public class TextPlayer {
  private final AbstractShipFactory<Character> shipFactory;
  private final Board<Character> theBoard;
  private final BoardTextView view;
  private final BufferedReader input;
  private final PrintStream out;
  private final String name;

  // Constructs a TextPlayer
  public TextPlayer(String name, Board<Character> theBoard, BufferedReader input, PrintStream out,
      AbstractShipFactory<Character> factory) {
    this.shipFactory = factory;
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.input = input;
    this.out = out;
    this.name = name;
  }

  // Prompts the TextPlayer for a Placement and returns the given placement
  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = input.readLine();
    return new Placement(s);
  }

  // Calls the readPlacement() method above, creates a destroyer based on that
  // placement, tries to add that destroyer to the TextPlayer's board, then
  // displays the TextPlayer's board with the newly placed destroyer
  public void doOnePlacement() throws IOException {
    Placement p = readPlacement("Player " + name + " where do you want to place a destroyer?");
    Ship<Character> s = shipFactory.makeDestroyer(p);
    theBoard.tryAddShip(s);
    out.println(view.displayMyOwnBoard());
  }

  /**
   * (1) Display the TextPlayer's empty board
   * (2) Print a prompt explaining how to enter a placement and which ships the player can place
   * (3) Call the doOnePlacement() method from above
   */
  public void doPlacementPhase() throws IOException {
    view.displayMyOwnBoard();
    String prompt = "--------------------------------------------------------------------------------\n" +
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
        "--------------------------------------------------------------------------------";
    out.println(prompt);
    doOnePlacement();
  }

}
