package edu.duke.jwp42.battleship;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.io.Reader;
import java.io.InputStreamReader;

public class App {
  private final AbstractShipFactory<Character> shipFactory;
  private final Board<Character> theBoard;
  private final BoardTextView view;
  private final BufferedReader inputReader;
  private final PrintStream out;

  public App(Board<Character> theBoard, Reader inputSource, PrintStream out) {
    this.shipFactory = new V1ShipFactory();
    this.theBoard = theBoard;
    this.view = new BoardTextView(theBoard);
    this.inputReader = new BufferedReader(inputSource);
    this.out = out;
  }

  public Placement readPlacement(String prompt) throws IOException {
    out.println(prompt);
    String s = inputReader.readLine();
    return new Placement(s);
  }

  public void doOnePlacement() throws IOException {
    Placement p = readPlacement("Where would you like to put your ship?");
    Ship<Character> s  = shipFactory.makeDestroyer(p);
    theBoard.tryAddShip(s);
    out.println(view.displayMyOwnBoard());
  }
  
  public static void main(String[] args) throws IOException {
    Board<Character> b = new BattleShipBoard<Character>(10, 20);
    InputStreamReader isr = new InputStreamReader(System.in);
    App app = new App(b, isr, System.out);
    app.doOnePlacement();
  }
}  
