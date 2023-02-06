package edu.duke.jwp42.battleship;

public class Placement {
  private final Coordinate where;
  private final char orientation;

  /**
   * Constructor for a Placement that takes in a coordinate and an orientation
   *
   * @throws IllegalArgumentException whenever the upper case version of
   *                                  orientation isn't 'H' or 'V
   */
  public Placement(Coordinate where, char orientation) {
    char upper = Character.toUpperCase(orientation);
    if (upper != 'H' && upper != 'V' && upper != 'U' && upper != 'D' && upper != 'L' && upper != 'R') {
      throw new IllegalArgumentException("Orientation should be one of [H, V, U, D, L, R] but instead is " + orientation);
    }
    this.where = where;
    this.orientation = upper;
  }

  /**
   * Constructor for a Placement that takes in the string representation of a
   * Placement
   *
   * @throws IllegalArgumentException whenever either of the following are true:
   *                                  (a) The length of descr isn't equal to 3
   *                                  (b) The third character of descr isn't 'H',
   *                                  'V', 'h' or 'v'
   */
  public Placement(String descr) {
    descr = descr.toUpperCase();
    if (descr.length() != 3) {
      throw new IllegalArgumentException("Input must have length 3 but instead has length " + descr.length());
    }
    String coordStr = descr.substring(0, 2);
    Coordinate where = new Coordinate(coordStr);
    char orientation = descr.charAt(2);
    if (orientation != 'H' && orientation != 'V') {
      throw new IllegalArgumentException(
          "Third letter of input must be in [H, V, U, D, L, R] but instead is " + orientation);
    }
    this.where = where;
    this.orientation = orientation;
  }

  public Coordinate getWhere() {
    return where;
  }

  public char getOrientation() {
    return orientation;
  }

  @Override
  public String toString() {
    StringBuilder ans = new StringBuilder();
    String alphabet = "abcdefghijklmnopqrstuvwxyz".toUpperCase();
    int row = where.getRow();
    int column = where.getColumn();
    char upperOrientation = Character.toUpperCase(orientation);
    char rowLetter = alphabet.charAt(row);
    ans.append(rowLetter);
    ans.append(column);
    ans.append(upperOrientation);
    ;
    return ans.toString();
  }

  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Placement p = (Placement) o;
      return where.equals(p.where) && orientation == p.orientation;
    }
    return false;
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }

}
