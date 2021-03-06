public class Point { 
  private byte x;
  private byte y;
  
  public Point(final byte x, final byte y) { 
    this.x = x;
    this.y = y;
  }
  
  public Point() { 
    this.x = (byte) 0;
    this.y = (byte) 0;
  }
  
  public Point(final int x, final int y) { 
    this.x = (byte) x;
    this.y = (byte) y;
  }
  
  public byte getX() { 
    return this.x;
  }
  
  public byte getY() {
    return this.y;
  }
  
  public void setX(final byte newX) { 
    this.x = newX;
  }
  
  public void setY(final byte newY) { 
    this.y = newY;
  }
  
  public void addX(final byte addX) { 
    this.x += addX;
  }
  
  public void addY(final byte addY) { 
    this.y += addY;
  }
  
  public void addX(final int addX) { 
    this.x += (byte) addX;
  }
  
  public void addY(final int addY) { 
    this.y += (byte) addY;
  }
  
  @Override
  public String toString() { 
    return "X: " + this.x + "\tY: " + y;
  }
  
  public boolean equals(final Point theOtherPoint) { 
    return (this.x == theOtherPoint.getX() && this.y == theOtherPoint.getY());
  }
}