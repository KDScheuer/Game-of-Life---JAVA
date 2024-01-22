import java.awt.*;
import javax.swing.*;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel {
    
    static final int SCREEN_WIDTH = 1000;
    static final int SCREEN_HEIGHT = 1000;

    // CHANGE THESE TO EFFECT HOW THE GAME PROGRESSES
    static final int UNIT_SIZE = 25;
    static final int DELAY = 400;
    static final int START_ACTIVE_CHANCE = 2;
    static final int SPAWN_BOX_SIZE = 10;
    ///////////////////////////////////////////////////

    static final int SPAWN_START_X = (SCREEN_WIDTH / 2) - (UNIT_SIZE * SPAWN_BOX_SIZE);
    static final int SPAWN_END_X = (SCREEN_WIDTH / 2) + (UNIT_SIZE * SPAWN_BOX_SIZE);    
    static final int SPAWN_START_Y = (SCREEN_HEIGHT / 2) - (UNIT_SIZE * SPAWN_BOX_SIZE);
    static final int SPAWN_END_Y = (SCREEN_HEIGHT / 2) + (UNIT_SIZE * SPAWN_BOX_SIZE); 
    
    int x[];
    int y[];
    List<Cell> objectList = new ArrayList<>();

    Random random = new Random();
    Timer timer;

    GamePanel() {
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.black);
        this.setFocusable(true);
        startGame();
    }

    public void startGame() {
        x = new int[(int)(SCREEN_WIDTH / UNIT_SIZE)];
        y = new int[(int)(SCREEN_HEIGHT / UNIT_SIZE)];

        for(int i = 0; i < SCREEN_WIDTH; i += UNIT_SIZE) {
            x[i / UNIT_SIZE] = i;
        }
        for(int i = 0; i < SCREEN_HEIGHT; i += UNIT_SIZE) {
            y[i / UNIT_SIZE] = i;
        }

        generateStartPositions();
        
    }

    public void nextGeneration() {
        for(int i = 0; i < objectList.size(); i++) {
            if (objectList.get(i).flipActive) {
                objectList.get(i).active = !objectList.get(i).active;
            }
            objectList.get(i).flipActive = false;
            objectList.get(i).neighborsActive = 0;
        }
    }

    public void generateStartPositions() {
        
        for(int i = 0; i < x.length; i++) {
            for(int n = 0; n < y.length; n++) {
                if(random.nextInt(START_ACTIVE_CHANCE) == 0 &&
                x[i] >= SPAWN_START_X && x[i] <= SPAWN_END_X &&
                y[n] >= SPAWN_START_Y && y[n] <= SPAWN_END_Y) {
                    objectList.add(new Cell(x[i], y[n], true));
                }
                else {
                    objectList.add(new Cell(x[i], y[n], false));
                }
            }
        }
    }

    public void paintComponent(Graphics g) {
        for(int i = 0; i < objectList.size(); i++) {
            if(objectList.get(i).active == true) {
                g.setColor(Color.blue);
                g.fillRect(objectList.get(i).xPosition, objectList.get(i).yPosition, UNIT_SIZE, UNIT_SIZE);
            }
            else {
                g.setColor(Color.black);
                g.fillRect(objectList.get(i).xPosition, objectList.get(i).yPosition, UNIT_SIZE, UNIT_SIZE);
            }
        }
    }

    public void checkNeighbors() {
        for(int i = 0; i < objectList.size(); i++) {
            Cell currentCell = objectList.get(i);

            checkNeighborStatus(currentCell,  -1, -1);
            checkNeighborStatus(currentCell, 0, -1);
            checkNeighborStatus(currentCell, 1, -1);
            checkNeighborStatus(currentCell, -1, 0);
            checkNeighborStatus(currentCell, 1, 0);
            checkNeighborStatus(currentCell,  -1, 1);
            checkNeighborStatus(currentCell, 0, 1);
            checkNeighborStatus(currentCell, 1, 1);

            if (currentCell.neighborsActive == 3 && currentCell.active == false) {
                currentCell.flipActive = true;
            }
            else if (currentCell.neighborsActive >= 4 && currentCell.active) {
                currentCell.flipActive = true;
            }
            else if (currentCell.neighborsActive <= 1 && currentCell.active) {
                currentCell.flipActive = true;
            } 
        }
    }

    private void checkNeighborStatus(Cell currentCell, int xDirection, int yDirection) {
        int neighborX = currentCell.xPosition + (xDirection * UNIT_SIZE);
        int neighborY = currentCell.yPosition + (yDirection * UNIT_SIZE);

        if(neighborX >= 0 && neighborX < SCREEN_WIDTH && neighborY >= 0 && neighborY < SCREEN_HEIGHT) {
            Cell neighborCell = findCell(neighborX, neighborY);

            if (neighborCell != null && neighborCell.active) {
                currentCell.neighborsActive++;
            }

        }
    }

    private Cell findCell(int x, int y) {
        for (Cell cell : objectList) {
            if (cell.xPosition == x && cell.yPosition == y) {
                return cell;
            }
        }
        return null;
    }

    { 
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                checkNeighbors();
                nextGeneration();
                repaint();
            }
        }, DELAY, DELAY);
    }
}
