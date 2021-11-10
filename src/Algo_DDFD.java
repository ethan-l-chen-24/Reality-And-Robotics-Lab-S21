import java.awt.*;
import java.util.*;

// deep diving frontier detection

public class Algo_DDFT extends Algo {

    boolean stop = false;
    GridPoint start;

    /**
     * CONSTRUCTOR
     * --------------------
     */

    public Algo_DDFT(Robot robot) {
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
        frontierGroups = new HashSet<>();
        GridPoint p = getClosestFrontier();
        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> firstFrontier = new HashSet<>();
        Set<GridPoint> currFrontier = new HashSet<>();
        boolean first = true;
        start = p;
        stop = false;

        DDFTGraphics(p, visited, firstFrontier, currFrontier, first, 0, g, xOffset, yOffset);
        return frontierGroups;
    }

    private void DDFTGraphics(GridPoint p, Set<GridPoint> visited, Set<GridPoint> firstFrontier, Set<GridPoint> currFrontier, boolean first, int stepsAway, Graphics g, int xOffset, int yOffset) throws InterruptedException {
        
        Stack<GridPoint> stack = new Stack<>();
        HashMap<GridPoint, GridPoint> from = new HashMap<>();
        HashMap<GridPoint, Integer> distance = new HashMap<>();
        stack.push(p);
        distance.put(p, 0);

        while(true) {
            g.setColor(Color.ORANGE);
            p = stack.pop();
            PriorityQueue<GridPoint> queue = new PriorityQueue<>(new Comparator<GridPoint>() {
                @Override
                public int compare(GridPoint a, GridPoint b) {
                    int aPriority = 0;
                    int bPriority = 0;

                    if (a.isFrontier()) {
                        aPriority = 1;
                    }
                    if (b.isFrontier()) {
                        bPriority = 1;
                    }

                    return aPriority - bPriority;
                }
            });

            visited.add(p);

            if(p.isFrontier()) {
                g.setColor(Color.BLUE);
                if(first) {
                    firstFrontier.add(p);
                } else {
                    currFrontier.add(p);
                }
            } else {
                if(first && !firstFrontier.isEmpty()) {
                    frontierGroups.add(firstFrontier);
                    first = false;
                } else if(!currFrontier.isEmpty()){
                    frontierGroups.add(currFrontier);
                }
                currFrontier = new HashSet<>();
            }

            for (GridPoint c : p.getAllNeighbors()) {
                if(c != null && (c.isFrontier() || c.isObstacle())) {
                    if (c.equals(start) && distance.get(p)>2) {
                        if(first) {
                            frontierGroups.add(firstFrontier);
                        } else {
                            firstFrontier.addAll(currFrontier);
                        }
                        return;
                    }
                    if (!visited.contains(c)) {
                        queue.add(c);
                        from.put(c, p);
                        distance.put(c, distance.get(p)+1);
                    }
                }
            }

            while (!queue.isEmpty()) {
                stack.push(queue.poll());
            }
        }

    }

    @Override
    public Set<Set<GridPoint>> getFrontiers() {
        frontierGroups = new HashSet<>();
        GridPoint p = getClosestFrontier();
        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> firstFrontier = new HashSet<>();
        Set<GridPoint> currFrontier = new HashSet<>();
        boolean first = true;
        start = p;
        stop = false;

        DDFT(p, visited, firstFrontier, currFrontier, first, 0);
        return frontierGroups;

    }

    public void DDFT(GridPoint p, Set<GridPoint> visited, Set<GridPoint> firstFrontier, Set<GridPoint> currFrontier, boolean first, int stepsAway) {

        Stack<GridPoint> stack = new Stack<>();
        HashMap<GridPoint, GridPoint> from = new HashMap<>();
        HashMap<GridPoint, Integer> distance = new HashMap<>();
        stack.push(p);
        distance.put(p, 0);

        while(true) {
            p = stack.pop();
            PriorityQueue<GridPoint> queue = new PriorityQueue<>(new Comparator<GridPoint>() {
                @Override
                public int compare(GridPoint a, GridPoint b) {
                    int aPriority = 0;
                    int bPriority = 0;

                    if (a.isFrontier()) {
                        aPriority = 1;
                    }
                    if (b.isFrontier()) {
                        bPriority = 1;
                    }

                    return aPriority - bPriority;
                }
            });

            visited.add(p);

            if(p.isFrontier()) {
                if(first) {
                    firstFrontier.add(p);
                } else {
                    currFrontier.add(p);
                }
            } else {
                if(first && !firstFrontier.isEmpty()) {
                    frontierGroups.add(firstFrontier);
                    first = false;
                } else if(!currFrontier.isEmpty()){
                    frontierGroups.add(currFrontier);
                }
                currFrontier = new HashSet<>();
            }

            for (GridPoint c : p.getAllNeighbors()) {
                if(c != null && (c.isFrontier() || c.isObstacle())) {
                    if (c.equals(start) && distance.get(p)>2) {
                        if(first) {
                            frontierGroups.add(firstFrontier);
                        } else {
                            firstFrontier.addAll(currFrontier);
                        }
                        return;
                    }
                    if (!visited.contains(c)) {
                        queue.add(c);
                        from.put(c, p);
                        distance.put(c, distance.get(p)+1);
                    }
                }
            }

            while (!queue.isEmpty()) {
                stack.push(queue.poll());
            }
        }

    }

    // recursive method limited by the recursive depth before resulting in stack overflow error
    private void DDFT_Recursive(GridPoint p, Set<GridPoint> visited, Set<GridPoint> firstFrontier, Set<GridPoint> currFrontier, boolean first, int stepsAway) {
        if(!stop) {

            PriorityQueue<GridPoint> queue = new PriorityQueue<>(new Comparator<GridPoint>() {
                @Override
                public int compare(GridPoint a, GridPoint b) {
                    int aPriority = 0;
                    int bPriority = 0;

                    if (a.isFrontier()) {
                        aPriority = 1;
                    }
                    if (b.isFrontier()) {
                        bPriority = 1;
                    }

                    return bPriority - aPriority;
                }
            });

            visited.add(p);

            if(p.isFrontier()) {
                if(first) {
                    firstFrontier.add(p);
                } else {
                    currFrontier.add(p);
                }
            } else {
                if(first && !firstFrontier.isEmpty()) {
                    frontierGroups.add(firstFrontier);
                    first = false;
                } else if(!currFrontier.isEmpty()){
                    frontierGroups.add(currFrontier);
                }
                currFrontier = new HashSet<>();
            }

            for (GridPoint c : p.getAllNeighbors()) {
                if(c != null && (c.isFrontier() || c.isObstacle())) {
                    if (c.equals(start) && stepsAway>2) {
                        stop = true;
                        if(first) {
                            frontierGroups.add(firstFrontier);
                        } else {
                            firstFrontier.addAll(currFrontier);
                        }
                        return;
                    }
                    if (!visited.contains(c)) {
                        queue.add(c);
                    }
                }
            }

            while (!queue.isEmpty()) {
                if(stop) {
                    return;
                }
                DDFT_Recursive(queue.poll(), visited, firstFrontier, currFrontier, first, stepsAway+1);
            }

        } else {
            return;
        }
    }
}