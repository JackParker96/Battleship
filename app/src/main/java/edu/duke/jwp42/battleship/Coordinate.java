package edu.duke.jwp42.battleship;

/**
 * This class represents a spot on the board as a Cartesian coordinate
 */
public class Coordinate {
  private final int row;
  private final int column;

  /**
   * Constructor for a Coodinate given two ints, one for the row and one for the
   * column
   *
   * @throws IllegalArgumentException is either row or column is negative
   */
  public Coordinate(int row, int column) {
    if (row < 0 || column < 0) {
      throw new IllegalArgumentException("Row and column must both be nonnegative");
    }
    this.row = row;
    this.column = column;
  }

  /**
   * Constructor for a Coordinate that takes in a string representation of the
   * coordinate
   *
   * @param descr is a string with two characters:
   *              The first character is a letter of the alphabet specifying the
   *              row coordinate
   *              The second character is a number in [0, 9] specifying the column
   *              coordinate
   * @throws IllegalArgumentException if any of the following occur:
   *                                  (a) descr.length() != 2
   *                                  (b) first character of descr is not in the
   *                                  alphabet
   *                                  (c) second character of descr is not in the
   *                                  interval [0,9]
   */
  public Coordinate(String descr) {
    descr = descr.toUpperCase();
    if (descr.length() != 2) {
      throw new IllegalArgumentException(
          "String representation of coordinate must have exactly two characters, but has " + descr.length()
              + " characters");
    }
    if (descr.charAt(0) < 'A' || descr.charAt(0) > 'Z') {
      throw new IllegalArgumentException("First character must be a letter of the alphabet, but is " + descr.charAt(0));
    }
    int rowNum = descr.charAt(0) - 'A';
    if (descr.charAt(1) < '0' || descr.charAt(1) > '9') {
      throw new IllegalArgumentException("Second character must be a number between 0 and 9 but is " + descr.charAt(1));
    }
    int columnNum = descr.charAt(1) - '0';
    this.row = rowNum;
    this.column = columnNum;
  }

  public int getRow() {
    return row;
  }

  public int getColumn() {
    return column;
  }

  @Override
  public boolean equals(Object o) {
    if (o.getClass().equals(getClass())) {
      Coordinate c = (Coordinate) o;
      return row == c.row && column == c.column;
    }
    return false;
  }

  @Override
  public String toString() {
    return "(" + row + ", " + column + ")";
  }

  @Override
  public int hashCode() {
    return toString().hashCode();
  }
}
