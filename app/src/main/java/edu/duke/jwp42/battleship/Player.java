package edu.duke.jwp42.battleship;

import java.io.IOException;
import java.util.function.Function;

public interface Player {

  public void setupShipCreationMap();
  
  public  void setupShipCreationList();

  public void sonarScanMyBoard(Coordinate center) throws IOException;  

  public void fireAtEnemy(Board<Character> enemyBoard, BoardTextView enemyView) throws IOException;
   
  public void sonarScanEnemyBoard(Board<Character> enemyBoard) throws IOException;

  public Boolean moveShipOnMyBoard() throws IOException;

  public void playOneTurn(Board<Character> enemyBoard, BoardTextView enemyView, String enemyName) throws IOException;

  public String hasLost();

  public Placement readPlacement(String prompt) throws IOException;

  public void doOnePlacement(String shipName, Function<Placement, Ship<Character>> createFn) throws IOException;

  public void doPlacementPhase() throws IOException;

}
