// parallel-computing frontier detection

import java.awt.*;
import java.util.*;

public class Algo_PCFD extends Algo {

    Set<GridPoint> allFrontiers;

    /**
     * CONSTRUCTOR
     * --------------------
     */

    public Algo_PCFD(Robot robot) {
        this.robot = robot;
        frontierGroups = new HashSet<>();
        allFrontiers = new HashSet<>();
    }

    @Override
    public Set<Set<GridPoint>> getFrontiers() {

        if (allFrontiers.isEmpty()) { // if the initial frontiers have not yet been found, run the initialWFD
            initialDDFD();
            return frontierGroups;
        }

        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> scan = robot.getActiveArea();
        Set<GridPoint> intersection = AlgoLib.intersection(allFrontiers, scan);
        visited.addAll(allFrontiers);
        boolean first = true;
        boolean second = false;
        boolean specialCase = false;

        GridPoint corner1 = null;
        Set<GridPoint> boundary1 = null;
        GridPoint corner2 = null;
        Set<GridPoint> boundary2 = null;
        GridPoint corner3 = null

        for (GridPoint p : intersection) {
            if (first) {
                if (p.hasUnexploredNeighbor()) {
                    corner1 = p;
                    boundary1 = AlgoLib.findSetContainingPoint(p, frontierGroups);

                    if (!p.isFrontier()) {
                        boundary1.remove(corner1);
                    }
                    first = false;
                    second = true;
                }

            } else if(second){
                if (p.hasUnexploredNeighbor()) {
                    corner2 = p;
                    boundary2 = AlgoLib.findSetContainingPoint(p, frontierGroups);

                    if (!p.isFrontier()) {
                        boundary2.remove(corner2);
                    }
                    break;
                }
                second = false;
            } else {
                specialCase = true;
            }
        }

        if(specialCase) {
            specialCase
        }

        Stack<GridPoint> stack1 = new Stack<>();
        Stack<GridPoint> stack2 = new Stack<>();
        stack1.push(corner1);
        stack2.push(corner2);

        while(!stack1.isEmpty() || !stack2.isEmpty()) {
            if(!stack1.isEmpty()) {
                GridPoint p = stack1.pop();
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
                for (GridPoint c : p.getAllNeighbors()) {
                    if(c != null && (c.isFrontier() || c.isObstacle())) {
                        if (!visited.contains(c)) {
                            queue.add(c);
                        }
                    }
                }


            }

            if(!stack2.isEmpty()) {

            }
        }

        return frontierGroups;

    }

    private void initialDDFD() {

        HashSet<GridPoint> visited = new HashSet<>();
        boolean first = true;
        Set<GridPoint> firstFrontier = new HashSet<>();
        Set<GridPoint> currFrontier = new HashSet<>();

        GridPoint p = robot.getLoc();
        GridPoint start = p;

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
                allFrontiers.add(p);
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
    public Set<Set<GridPoint>> graphics(Graphics g, int xOffset, int yOffset) throws InterruptedException {
        return null;
    }

    /**
     * METHODS
     * --------------------
     */

}
