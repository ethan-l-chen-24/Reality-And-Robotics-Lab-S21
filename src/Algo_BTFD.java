import java.awt.*;
import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Algo_EBT extends Algo {

    /**
     * CONSTRUCTOR
     * --------------------
     */

    public Algo_EBT(Robot robot) {
        this.robot = robot;
        frontierGroups = new HashSet<>();
    }

    /**
     * METHODS
     * --------------------
     */

    private GridPoint getClosestFrontier() {

        GridPoint min = robot.getLoc();
        int minX = min.x;
        int maxX = robot.getGrid().getWidth();
        int y = min.y;

        while (minX != maxX && minX + 1 != maxX) {
            int tempX = (minX + maxX) / 2;
            if (robot.getGrid().grid[tempX][y].isFreeSpace()) {
                minX = tempX;
            } else {
                maxX = tempX;
            }
        }

        return robot.getGrid().grid[minX][y];
    }

    @Override
    public Set<Set<GridPoint>> graphics(Graphics g, int xOffset, int yOffset) throws InterruptedException {
        GridPoint p = getClosestFrontier();
        Set<GridPoint> visited = new HashSet<>();
        Stack<GridPoint> stack = new Stack<>();
        Set<GridPoint> allFrontiers = new HashSet<>();
        g.setColor(Color.ORANGE);

        stack.push(p);

        GridPoint c = null;
        while (!stack.isEmpty()) {
            c = stack.pop();
            g.fillOval(c.x*xOffset, c.y*yOffset, xOffset, yOffset);

            if (c.isFrontier()) {
                allFrontiers.add(c);
            }

            for (GridPoint neighbor : c.getAllNeighbors()) {
                if (!visited.contains(neighbor) && !neighbor.isObstacle() && (neighbor.hasUnexploredNeighbor()||neighbor.hasDirectObstacleNeighbor()) && !neighbor.isUnexplored()) {
                    stack.push(neighbor);
                }
                visited.add(neighbor);
            }
            Thread.sleep(FFGraphics.shortDelay);
        }
        Thread.sleep(FFGraphics.longDelay);

        return AlgoLib.groupFrontiers(allFrontiers);
    }

    @Override
    public Set<Set<GridPoint>> getFrontiers() {
        GridPoint p = getClosestFrontier();
        Set<GridPoint> visited = new HashSet<>();
        Stack<GridPoint> stack = new Stack<>();
        Set<GridPoint> allFrontiers = new HashSet<>();

        stack.push(p);

        GridPoint c = null;
        while (!stack.isEmpty()) {
            c = stack.pop();

            if (c.isFrontier()) {
                allFrontiers.add(c);
            }

            for (GridPoint neighbor : c.getAllNeighbors()) {
                if (neighbor != null) {
                    if (!visited.contains(neighbor) && !neighbor.isObstacle() && (neighbor.hasUnexploredNeighbor() || neighbor.hasDirectObstacleNeighbor()) && !neighbor.isUnexplored()) {
                        stack.push(neighbor);
                    }
                    visited.add(neighbor);
                }
            }
        }

        return AlgoLib.groupFrontiers(allFrontiers);
    }
}