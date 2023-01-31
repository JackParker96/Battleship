package edu.duke.jwp42.battleship;

/**
 * A PlacementRuleChecker class overrides checkMyRule to check its own rule
 * It also has another PlacementRuleChecker called next
 * Checking that a particular placement of a particular ship on a particular
 * 
 * 
 * board is legal requires chaining together whichever PlacementRuleCheckers
 * make sense for the particular rules of the game being implemented
 */
public abstract class PlacementRuleChecker<T> {
  // The next PlacementRuleChecker to check
  // If next is null, then we're done checking rules and the placement is legal
  private final PlacementRuleChecker<T> next;

  // Construct a PlacementRuleChecker by passing in a PlacementRuleChecker named next
  public PlacementRuleChecker(PlacementRuleChecker<T> next) {
    this.next = next;
  }

  // Class that extend PlacementRuleChecker will override this method to check their particular rule
  protected abstract String checkMyRule(Ship<T> theShip, Board<T> theBoard);

  /**
   * Recursive function for checking a chain of rules
   *
   * @param theShip is the Ship we want to place
   * @param theBoard is the Board on which we want to place theShip
   * @return true if all the rules for ship placement are satisfied or false if any single rule is not satisfied
   */
  public String checkPlacement(Ship<T> theShip, Board<T> theBoard) {
    String errorMessage = checkMyRule(theShip, theBoard);
    if (errorMessage != null) {
      return errorMessage;
    }
    if (next != null) {
      return next.checkPlacement(theShip, theBoard);
    }
    return null;
  }
}
