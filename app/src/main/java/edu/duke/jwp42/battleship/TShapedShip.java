package edu.duke.jwp42.battleship;

import java.util.HashSet;

public class TShapedShip<T> extends BasicShip<T> {
  public final String name;

  public static final HashSet<Coordinate> makeCoords(Placement upperLeft) {
    HashSet<Coordinate> ans = new HashSet<Coordinate>();
    Coordinate where = upperLeft.getWhere();
    int row = where.getRow();
    int column = where.getColumn();
    char orientation = upperLeft.getOrientation();
    if (orientation == 'U') {
      Coordinate tip = new Coordinate(row, column + 1);
      ans.add(tip);
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + 1, column + i);
        ans.add(c);
      }
    }
    if (orientation == 'R') {
      Coordinate tip = new Coordinate(row + 1, column + 1);
      ans.add(tip);
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + i, column);
        ans.add(c);
      }
    }
    if (orientation == 'D') {
      Coordinate tip = new Coordinate(row + 1, column + 1);
      ans.add(tip);
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row, column + i);
        ans.add(c);
      }
    }
    if (orientation == 'L') {
      Coordinate tip = new Coordinate(row + 1, column);
      ans.add(tip);
      for (int i = 0; i < 3; i++) {
        Coordinate c = new Coordinate(row + i, column + 1);
        ans.add(c);
      }
    }
    return ans;
  }

  public TShapedShip(String name, Placement upperLeft, ShipDisplayInfo<T> myDisplayInfo,
      ShipDisplayInfo<T> enemyDisplayInfo) {
    super(makeCoords(upperLeft), myDisplayInfo, enemyDisplayInfo);
    this.name = name;
  }

  public TShapedShip(String name, Placement upperLeft, T data, T onHit) {
    this(name, upperLeft, new SimpleShipDisplayInfo<T>(data, onHit), new SimpleShipDisplayInfo<>(null, data));
  }

  @Override
  public String getName() {
    return name;
  }

}
