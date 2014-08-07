import java.awt.Color;
import java.text.DecimalFormat;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Arrays;
import java.util.Random;

/**
 * Written by Ryan D'souza Brown University CS 015 Final Project Represents the ghosts in the Pacman game
 */

public class TheGhost extends PacmanItem {
  private final Color startColor;
  private long startPenTime;
  private final Queue<Point> prospectivePoints = new LinkedList<Point>();
  private static final Random theGenerator = new Random();
  
  private static final byte SIZE = 23;
  private final byte[][] theBoard = new byte[SIZE][SIZE];
  private static final Point[] corners = {new Point(1, 1), new Point(1, 22), new Point(22, 1), new Point(22, 22)};
  private final Point cornerPoint;
  
  private static final byte WALL = -1;
  private static final byte UNEXPLORED = Byte.MAX_VALUE;
  private static final byte GHOST = 0;
  private static final byte PACMAN = -2;
  
  private boolean isReleased = false;
  
  private final Point aPoint = new Point();
  
  public void updateBoard(final byte[][] pacmanBoard) {
    for (byte i = 0; i < pacmanBoard.length; i++) {
      for (byte y = 0; y < pacmanBoard[i].length; y++) {
        if (pacmanBoard[i][y] == Pacman.WALL) {
          this.theBoard[i][y] = this.WALL;
        }
        else if(pacmanBoard[i][y] == Pacman.PACMAN) { 
          this.theBoard[i][y] = this.PACMAN;
        }
        else {
          this.theBoard[i][y] = this.UNEXPLORED;
        }
      }
    }
    this.theBoard[this.y][this.x] = GHOST;
    
    aPoint.setX(x);
    aPoint.setY(y);
  }
  
  /** Move and change colors according to gameMode */
  public void move(final Point ghostLocation, final Mode gameMode) { 
    setColor(gameMode);
    
    if(gameMode == Mode.CHASE) {
      startBreadthFirstAlgorithm(ghostLocation);
    }
    
    else if(gameMode == Mode.FRIGHTENED) {
      frightenedMode(ghostLocation);
    }
    
    else if(gameMode == Mode.SCATTER) {
      scatterMode(ghostLocation);
    }
  }
  
  /**Scatter mode: Move randomly */
  private void scatterMode(final Point currentLoc) { 
    //Choose a randomDirection
    final Direction randomDirection = theDirections[theGenerator.nextInt(theDirections.length)];
    
    if(itemAtPoint(randomDirection, currentLoc) == WALL) {
      scatterMode(currentLoc);
    }
    else { 
      super.move(randomDirection);
    }
  }
  
  /**Frightened Mode: Move to corner of the board */
  private void frightenedMode(final Point currentLoc) { 
    //Randomly choose 1 of 4 corners, breadthFirst to it 
    
  }
  
  /**Change Ghost color based upon game mode */
  private void setColor(final Mode theMode) { 
    if(theMode == Mode.FRIGHTENED) {
      theColor = Color.GREEN;
    }
    else { 
      theColor = startColor;
    }
  }
  
  private Point pacmanLoc = null;
  
  /**
   * Checks all 4 directions around the point If that item does not have my number and it's not a wall 
   * Add it to the queue and set its number to mine + 1
   */
  private void availableInDirections(final Point current) {
    for (Direction theDirection : theDirections) {
      final Point newPoint = getNewPoint(current, theDirection);
      
      if(newPoint.getX() >= theBoard.length || newPoint.getY() >= theBoard[0].length) { 
        //Skip
      }
      else if(newPoint.getX() < 0 || newPoint.getY() < 0) { 
        //Skip
      }
      
      else if(itemAtPoint(newPoint) == PACMAN) {
        setValue(newPoint, itemAtPoint(current) + 1);
        pacmanLoc = newPoint;
        prospectivePoints.clear();
        return;
      }
      
      // If the value in that direction does not have the value I currently have
      // and is not a wall
      else if (itemAtPoint(newPoint) == UNEXPLORED) {
        // Add it to the queue
        prospectivePoints.add(newPoint);
        
        // Increment its value from mine
        setValue(newPoint, itemAtPoint(current) + 1);
      }
    }
  }
  
  private void startBreadthFirstAlgorithm(final Point startPoint) {
    prospectivePoints.clear();
    prospectivePoints.add(startPoint);
    
    while (!prospectivePoints.isEmpty()) {
      availableInDirections(prospectivePoints.remove());
    }
    //printBoard();
    
    if(pacmanLoc != null) { 
      super.move(pacmanToGhost());
    }
  }
  
  private Direction pacmanToGhost() { 
    byte itemAtNow = itemAtPoint(pacmanLoc);
    Point workBackwards = pacmanLoc;
    Direction moveDirection = null;
    
    while(itemAtNow != 0) { 
      for(Direction theDirection : theDirections) { 
        final Point newPoint = getNewPoint(workBackwards, theDirection);
        
        if(itemAtPoint(newPoint) == (itemAtNow - 1) && itemAtPoint(newPoint) != this.WALL) {
          moveDirection = theDirection; 
          workBackwards = newPoint;
          itemAtNow = itemAtPoint(workBackwards);
        }
      }
    }
    return getOppositeDirection(moveDirection);
  }
  
  /** Return the opposite direction */
  public static Direction getOppositeDirection(final Direction theDirection) { 
        if(theDirection == Direction.RIGHT) { 
      return Direction.LEFT;
    }
    else if(theDirection == Direction.LEFT) { 
      return Direction.RIGHT; 
    }
    else if(theDirection == Direction.UP) { 
      return Direction.DOWN;
    }
    else if(theDirection == Direction.DOWN) { 
      return Direction.UP;
    }
    else
      return null;
  }
  
  /** Updates the board at the given Point given the next Direction and the number */
  private void updateBoard(final Point thePoint, final Direction theDirection, final byte num) {
    updateBoard(super.getNewPoint(thePoint, theDirection), num);
  }
  
  /** Updates the board at the given Point with the given number */
  private void updateBoard(final Point thePoint, final byte num) {
    theBoard[(byte)thePoint.getY()][(byte) thePoint.getX()] = num;
  }
  
  /** Returns the item at a Point given the Point and its Direction */
  private byte itemAtPoint(final Direction theDirection, final Point thePoint) {
    return itemAtPoint(super.getNewPoint(thePoint, theDirection));
  }
  
  /** Returns the item at a Point */
  private byte itemAtPoint(final Point thePoint) {
    if(thePoint.getX() >= theBoard.length || thePoint.getY() >= theBoard[0].length) { 
      return PACMAN;
    }
    if(thePoint.getX() < 0 || thePoint.getY() < 0) { 
      return PACMAN;
    }
    return theBoard[(byte) thePoint.getY()][(byte) thePoint.getX()];
  }
  
  /** Set item at Point to a certain value */
  public void setValue(final Point thePoint, final int theNum) {
    theBoard[(byte) thePoint.getY()][(byte) thePoint.getX()] = (byte) theNum;
  }
  
  public Queue<Point> getProspectivePoints() {
    return this.prospectivePoints;
  }
  
  public void addPoint(final Point thePoint) {
    prospectivePoints.add(thePoint);
  }
  
  public void clearQ() {
    this.prospectivePoints.removeAll(prospectivePoints);
  }
  
  public Point getFirst() {
    return this.prospectivePoints.remove();
  }
  
  public Point getButKeepFirst() {
    return this.prospectivePoints.peek();
  }
  
  public void addPoints(final Point[] thePoints) {
    this.prospectivePoints.addAll(Arrays.asList(thePoints));
  }
  
  public Point[] getPoints() {
    return prospectivePoints.toArray(new Point[prospectivePoints.size()]);
  }
  
  /** Constructor */
  public TheGhost(Color theColor, int x, int y, final byte[][] pacmanGrid, final Mode gameMode) {
    super((byte)x, (byte)y, theColor);
    this.startColor = theColor;
    startPenTime = System.currentTimeMillis();
    this.updateBoard(pacmanGrid);
    
    if(theColor == Color.CYAN) { 
      cornerPoint = corners[0];
    }
    else if(theColor == Color.RED) { 
      cornerPoint = corners[1];
    }
    else if(theColor == Color.ORANGE) { 
      cornerPoint = corners[2];
    }
    else if(theColor == Color.PINK) { 
      cornerPoint = corners[3];
    }
    else {
      cornerPoint = corners[theGenerator.nextInt(corners.length)];
    }
  }
  
  /** Returns true if the ghost has been released from the pen */
  public boolean isReleased() { 
    return isReleased;
  }
  
  public void release() { 
    isReleased = true;
  }
  
  public void setInPen() { 
    isReleased = false;
  }
  
  /** @return timeTheGhost was in the pen */
  public long getPenTime() {
    return this.startPenTime;
  }
  
  public void setPenTime(long time) {
    this.startPenTime = time;
  }
  
  public String toString() {
    return "GHOST:\t" + name + "\tX: " + x + "\tY: " + y;
  }
  
  public void printBoard() {
    DecimalFormat df = new DecimalFormat("00");
    
    for (byte i = 0; i < theBoard.length; i++) {
      for (byte y = 0; y < theBoard[i].length; y++) {
        
        if (theBoard[i][y] == WALL) {
          System.out.print("XX\t");
        } else if (theBoard[i][y] == UNEXPLORED) {
          System.out.print("--\t");
        } else {
          System.out.print(df.format(theBoard[i][y]) + "\t");
        }
      }
      System.out.println();
    }
    System.out.println();
  }
}