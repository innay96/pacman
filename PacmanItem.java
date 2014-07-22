/** Represents a Pacman object
  * Like a ghost, pellet, energizer, or pacman */
public abstract class PacmanItem {
  protected int x, y;
  protected Direction facingDirection;
  protected int startX, startY;
  
  public PacmanItem(final int x, final int y){
    this.x = x;
    this.y = y;
    
    this.startX = x;
    this.startY = y;
    
    facingDirection = Direction.UP;
  }
  
  public void move(Direction theD){
    switch(theD) {
      case UP:
        this.y--;
        facingDirection = Direction.UP;
        break;
        
      case DOWN:
        this.y++;
        facingDirection = Direction.DOWN;
        break;
        
      case LEFT:
        this.x--;
        facingDirection = Direction.LEFT;
        break;
        
      case RIGHT:
        this.x++;
        facingDirection = Direction.RIGHT;
        break;
        
      default:
        break;
    }
  }
  
  public int getStartX() { return this.startX; }
  public int getStartY() { return this.startY; }
  
  public void returnToStartPosition() {
    this.x = this.startX;
    this.y = this.startY;
    this.facingDirection = Direction.UP;
  }
  
  public Direction getFacingDirection() {
    return facingDirection;
  }
  
  public void setFacingDirection(Direction facing) {
    this.facingDirection = facing;
  }
  
  public enum Direction {
    UP, DOWN, LEFT, RIGHT; 
  }
  
  public int getX() {
    return this.x;
  }
  
  public int getY() {
    return this.y;
  }
  
  public void setX(int x) {
    this.x = x;
  }
  
  public void setY(int y) {
    this.y = y;
  }
}