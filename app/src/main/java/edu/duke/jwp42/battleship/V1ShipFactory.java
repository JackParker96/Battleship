package edu.duke.jwp42.battleship;

/**
 * A Factory class for making any of the four ships needed in version 1
 */
public class V1ShipFactory implements AbstractShipFactory<Character> {

  /**
   * Helper method to create a RectangleShip for version 1
   *
   * @param where is a Placement whose Coordinate specifies where the upper left corner of the ship should be
   * @param letter is the graphical representation of a square of the ship on the board
   * @param name is the name of the ship (in version 1, this is "submarine", "battleship", "carrier", or "destroyer")
   * @return a Ship<Character> according to the arguments passed in
   */
  protected Ship<Character> createShip(Placement where, int w, int h, char letter, String name) {
    if (where.getOrientation() == 'H') {
      int temp = w;
      w = h;
      h = temp;
    }
    Ship<Character> s = new RectangleShip<Character>(name, where.getWhere(), w, h, letter, '*');
    return s;
  }

  @Override
  public Ship<Character> makeSubmarine(Placement where) {
    return createShip(where, 1, 2, 's', "submarine");
  }

  @Override
  public Ship<Character> makeBattleship(Placement where) {
    return createShip(where, 1, 4, 'b', "battleship");
  }

  @Override
  public Ship<Character> makeCarrier(Placement where) {
    return createShip(where, 1, 6, 'c', "carrier");
  }

  @Override
  public Ship<Character> makeDestroyer(Placement where) {
    return createShip(where, 1, 3, 'd', "destroyer");
  }

}
