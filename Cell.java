import java.awt.*;

public class Cell extends Rectangle{
    
    int xPosition;
    int yPosition;
    int neighborsActive;
    boolean flipActive;
    boolean active;

    Cell(int x, int y, boolean active) {
        this.xPosition = x;
        this.yPosition = y;
        this.neighborsActive = 0;
        this.flipActive = false;
        this.active = active;
    }

}
