package edu.duke.jwp42.battleship;

public class V2ShipFactory implements AbstractShipFactory<Character> {

  protected Ship<Character> createRectangleShip(Placement where, int w, int h, char letter, String name) {
    if (where.getOrientation() == 'H') {
      int temp = w;
      w = h;
      h = temp;
    }
    Ship<Character> s = new RectangleShip<Character>(name, where.getWhere(), w, h, letter, '*');
    return s;
  }

  protected Ship<Character> createTShapedShip(Placement where, char letter, String name) {
    return new TShapedShip<Character>(name, where, letter, '*');
  }

  protected Ship<Character> createZShapedShip(Placement where, char letter, String name) {
    return new ZShapedShip<Character>(name, where, letter, '*');
  }
    
  @Override
  public Ship<Character> makeSubmarine(Placement where) {
    return createRectangleShip(where, 1, 2, 's', "submarine");
  }

  @Override
  public Ship<Character> makeDestroyer(Placement where) {
    return createRectangleShip(where, 1, 3, 'd', "destroyer");
  }

  @Override
  public Ship<Character> makeBattleship(Placement where) {
    return createTShapedShip(where, 'b', "battleship");
  }

  @Override
  public Ship<Character> makeCarrier(Placement where) {
    return createZShapedShip(where, 'c', "carrier");
  }

}
