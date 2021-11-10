// dynamically restructuring frontier detection

import javax.swing.plaf.basic.BasicScrollPaneUI;
import java.awt.*;
import java.util.*;

public class DRFD extends Algo {

    Set<GridPoint> allFrontiers;

    /**
     * CONSTRUCTOR
     * --------------------
     */

    public DRFD(Robot robot) {
        this.robot = robot;
        frontierGroups = new HashSet<>();
        allFrontiers = new HashSet<>();
    }

    /**
     * METHODS
     * --------------------
     */

    @Override
    public Set<Set<GridPoint>> getFrontiers() {

        if (allFrontiers.isEmpty()) { // if the initial frontiers have not yet been found, run the initialWFD
            initialDDFD();
            return frontierGroups;
        }

        GridPoint start = null;
        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> scan = robot.getActiveArea();
        Set<GridPoint> intersection = AlgoLib.intersection(allFrontiers, scan);
        HashMap<GridPoint, Set<GridPoint>> correspondingGroups = new HashMap<>();

        for (GridPoint p : allFrontiers) {
            for (GridPoint m : p.getDirectNeighbors()) {
                if (scan.contains(m)) {
                    intersection.add(m);
                    start = m;
                    correspondingGroups.put(p, AlgoLib.findSetContainingPoint(p, frontierGroups));
                }
            }
        }

        visited.addAll(allFrontiers);
        Set<GridPoint> toRemove = new HashSet<>();

        for (GridPoint p : intersection) {
            if (!p.isFrontier()) {
                toRemove.add(p);
                allFrontiers.remove(p);
            }
        }

        Set<Set<GridPoint>> restructured = new HashSet<>();
        for (Set<GridPoint> group : frontierGroups) {
            group.removeAll(toRemove);
            restructured.addAll(AlgoLib.groupFrontiers(group));
        }
        frontierGroups.addAll(restructured);

        Stack<GridPoint> stack = new Stack<>();
        stack.push(start);
        Set<GridPoint> currFrontier = new HashSet<>();
        HashMap<GridPoint, GridPoint> from = new HashMap<>();
        HashMap<GridPoint, Integer> distance = new HashMap<>();

        while (!stack.isEmpty()) {

            GridPoint p = stack.pop();
            visited.add(p);

            PriorityQueue<GridPoint> queue = new PriorityQueue<>(new Comparator<GridPoint>() {
                @Override
                public int compare(GridPoint a, GridPoint b) {
                    int aPriority = 0;
                    int bPriority = 0;

                    if (a.isFrontier()) {
                        if (p.directlyNeighbors(a)) {
                            aPriority = 2;
                        } else {
                            aPriority = 1;
                        }
                    }
                    if (b.isFrontier()) {
                        if (p.directlyNeighbors(b)) {
                            bPriority = 2;
                        } else {
                            bPriority = 1;
                        }
                    }
                    if (intersection.contains(a)) {
                        aPriority = 3;
                    }
                    if (intersection.contains(b)) {
                        bPriority = 3;
                    }

                    return aPriority - bPriority;
                }
            });

            if (p.isFrontier()) {
                currFrontier.add(p);
            }

            for (GridPoint c : p.getAllNeighbors()) {
                if (c != null && !allFrontiers.contains(c) && (c.isFrontier() || c.isObstacle() || intersection.contains(c))) {
                    if (c.equals(start) && distance.get(p) > 2) {
                        return frontierGroups;
                    }
                    if (!visited.contains(c)) {
                        queue.add(c);
                        from.put(c, p);
                        distance.put(c, distance.get(p) + 1);
                    }
                }
            }

            while (!queue.isEmpty()) {
                stack.push(queue.poll());
            }

        }

        return frontierGroups;

    }

    private GridPoint getClosestFrontier() {

        GridPoint min = robot.getLoc();
        int minX = min.x;
        int maxX = robot.getGrid().getWidth();
        int y = min.y;

        while (minX != maxX && minX + 1 != maxX) {
            int tempX = (minX + maxX) / 2;
            if (!robot.getGrid().grid[tempX][y].isUnexplored()) {
                minX = tempX;
            } else {
                maxX = tempX;
            }
        }

        return robot.getGrid().grid[minX][y];
    }

    @Override
    public Set<Set<GridPoint>> graphics(Graphics g, int xOffset, int yOffset) throws InterruptedException {

        g.setColor(Color.BLUE);

        if (allFrontiers.isEmpty()) {
            initialDDFD();
            return frontierGroups;
        }

        GridPoint start = null;
        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> scan = robot.getActiveArea();
        Set<GridPoint> intersection = AlgoLib.intersection(allFrontiers, scan);
        if(intersection.isEmpty()) {
            return frontierGroups;
        }
        HashMap<GridPoint, Set<GridPoint>> correspondingGroups = new HashMap<>();

        for (GridPoint p : allFrontiers) {
            for (GridPoint m : p.getAllNeighbors()) {
                if (scan.contains(m) && intersection.contains(m)) {
                    intersection.add(p);
                    start = p;
                }
            }
            g.fillOval(p.x * xOffset, p.y * yOffset, xOffset, yOffset);
        }

        Thread.sleep(FFGraphics.longDelay);

        visited.addAll(allFrontiers);
        visited.removeAll(intersection);
        Set<GridPoint> toRemove = new HashSet<>();

        g.setColor(Color.CYAN);
        for (GridPoint p : intersection) {
            if (!p.isFrontier()) {
                toRemove.add(p);
                g.fillOval(p.x * xOffset, p.y * yOffset, xOffset, yOffset);
            }
            allFrontiers.remove(p);
        }

        Thread.sleep(FFGraphics.longDelay);

        Set<Set<GridPoint>> restructured = new HashSet<>();
        for (Set<GridPoint> group : frontierGroups) {
            group.removeAll(toRemove);
            restructured.addAll(AlgoLib.groupFrontiers(group));
        }
        frontierGroups = restructured;

        for(GridPoint p : intersection) {
            correspondingGroups.put(p, AlgoLib.findSetContainingPoint(p, frontierGroups));
        }

        Stack<GridPoint> stack = new Stack<>();
        stack.push(start);
        Set<GridPoint> currFrontier = null;
        currFrontier = correspondingGroups.get(start);

        HashMap<GridPoint, GridPoint> from = new HashMap<>();
        HashMap<GridPoint, Integer> distance = new HashMap<>();
        distance.put(start, 0);
        GridPoint curr = start;

        while (!stack.isEmpty()) {

            g.setColor(Color.orange);

            curr = stack.pop();
            visited.add(curr);

            PriorityQueue<GridPoint> queue = new PriorityQueue<>(new GPComparator(curr, intersection));

            if (curr.isFrontier()) {
                g.setColor(Color.BLUE);
                allFrontiers.add(curr);
                currFrontier.add(curr);
            } else if (curr.isObstacle()) {
                if(!frontierGroups.contains(currFrontier)) {
                    frontierGroups.add(currFrontier);
                }
                currFrontier = new HashSet<>();
            } else {
                correspondingGroups.get(curr).addAll(currFrontier);
                currFrontier = correspondingGroups.get(curr);
            }

            g.fillOval(curr.x * xOffset, curr.y * yOffset, xOffset, yOffset);
            Thread.sleep(FFGraphics.shortDelay);

            for (GridPoint c : curr.getAllNeighbors()) {
                if (c != null && c.equals(start) && visited.containsAll(intersection) && distance.get(curr) > 2) {
                    Thread.sleep(FFGraphics.longDelay);
                    if(!frontierGroups.contains(currFrontier)) {
                        frontierGroups.add(currFrontier);
                    }
                    return frontierGroups;
                }
                if (c != null && !allFrontiers.contains(c) && (c.isFrontier() || c.isObstacle() || intersection.contains(c))) {

                    if (!visited.contains(c)) {
                        queue.add(c);
                        from.put(c, curr);
                        distance.put(c, distance.get(curr) + 1);
                    }
                }
            }


            while (!queue.isEmpty()) {
                stack.push(queue.poll());
            }

        }

        Thread.sleep(FFGraphics.longDelay);
        return frontierGroups;
    }

    private void initialDDFD() {

        HashSet<GridPoint> visited = new HashSet<>();
        boolean first = true;
        Set<GridPoint> firstFrontier = new HashSet<>();
        Set<GridPoint> currFrontier = new HashSet<>();

        GridPoint p = getClosestFrontier();
        GridPoint start = p;

        Stack<GridPoint> stack = new Stack<>();
        HashMap<GridPoint, GridPoint> from = new HashMap<>();
        HashMap<GridPoint, Integer> distance = new HashMap<>();
        stack.push(p);
        distance.put(p, 0);

        while (!stack.isEmpty()) {
            p = stack.pop();
            GridPoint finalP = p;
            PriorityQueue<GridPoint> queue = new PriorityQueue<>(new Comparator<GridPoint>() {
                @Override
                public int compare(GridPoint a, GridPoint b) {
                    int aPriority = 0;
                    int bPriority = 0;

                    if (a.isFrontier()) {
                        if (finalP.directlyNeighbors(a)) {
                            aPriority = 2;
                        } else {
                            aPriority = 1;
                        }
                    }
                    if (b.isFrontier()) {
                        if (finalP.directlyNeighbors(b)) {
                            bPriority = 2;
                        } else {
                            bPriority = 1;
                        }
                    }

                    return aPriority - bPriority;
                }
            });

            visited.add(p);

            if (p.isFrontier()) {
                allFrontiers.add(p);
                if (first) {
                    firstFrontier.add(p);
                } else {
                    currFrontier.add(p);
                }
            } else {
                if (first && !firstFrontier.isEmpty()) {
                    frontierGroups.add(firstFrontier);
                    first = false;
                } else if (!currFrontier.isEmpty()) {
                    frontierGroups.add(currFrontier);
                }
                currFrontier = new HashSet<>();
            }

            for (GridPoint c : p.getAllNeighbors()) {
                if (c != null && (c.isFrontier() || c.isObstacle())) {
                    if (c.equals(start) && distance.get(p) > 2) {
                        if (first) {
                            frontierGroups.add(firstFrontier);
                        } else {
                            firstFrontier.addAll(currFrontier);
                        }
                        return;
                    }
                    if (!visited.contains(c)) {
                        queue.add(c);
                        from.put(c, p);
                        distance.put(c, distance.get(p) + 1);
                    }
                }
            }

            while (!queue.isEmpty()) {
                stack.push(queue.poll());
            }
        }
    }

    private class GPComparator implements Comparator<GridPoint> {

        private GridPoint finalCurr;
        private Set<GridPoint> intersection;

        private GPComparator(GridPoint finalCurr, Set<GridPoint> intersection) {
            this.finalCurr = finalCurr;
            this.intersection = intersection;
        }

        @Override
        public int compare(GridPoint a, GridPoint b) {
            int aPriority = 0;
            int bPriority = 0;

                if (a.isFrontier()) {
                    if (finalCurr.directlyNeighbors(a)) {
                        aPriority = 2;
                    } else {
                        aPriority = 1;
                    }
                }
                if (b.isFrontier()) {
                    if (finalCurr.directlyNeighbors(b)) {
                        bPriority = 2;
                    } else {
                        bPriority = 1;
                    }
                }
                if (intersection.contains(a)) {
                    aPriority = 3;
                }
                if (intersection.contains(b)) {
                    bPriority = 3;
                }

            return aPriority - bPriority;
        }

        @Override
        public boolean equals(Object obj) {
            return false;
        }
    }















    public Set<Set<GridPoint>> graphicsKosaraju(Graphics g, int xOffset, int yOffset) throws InterruptedException {

        g.setColor(Color.BLUE);

        if (allFrontiers.isEmpty()) { // if the initial frontiers have not yet been found, run the initialWFD
            initialDDFD();
            return frontierGroups;
        }

        GridPoint start = null;
        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> scan = robot.getActiveArea();
        Set<GridPoint> intersection = AlgoLib.intersection(allFrontiers, scan);
        if(intersection.isEmpty()) {
            return frontierGroups;
        }
        HashMap<GridPoint, Set<GridPoint>> correspondingGroups = new HashMap<>();

        for (GridPoint p : allFrontiers) {
            for (GridPoint m : p.getAllNeighbors()) {
                if (scan.contains(m) && intersection.contains(m)) {
                    intersection.add(p);
                    start = p;
                }
            }
            g.fillOval(p.x * xOffset, p.y * yOffset, xOffset, yOffset);
        }

        Thread.sleep(FFGraphics.longDelay);

        visited.addAll(allFrontiers);
        visited.removeAll(intersection);
        Set<GridPoint> toRemove = new HashSet<>();

        g.setColor(Color.CYAN);
        for (GridPoint p : intersection) {
            if (!p.isFrontier()) {
                toRemove.add(p);
                g.fillOval(p.x * xOffset, p.y * yOffset, xOffset, yOffset);
            }
            allFrontiers.remove(p);
        }

        Thread.sleep(FFGraphics.longDelay);

        Stack<GridPoint> stack = new Stack<>();
        stack.push(start);

        HashMap<GridPoint, GridPoint> from = new HashMap<>();
        HashMap<GridPoint, Integer> distance = new HashMap<>();
        distance.put(start, 0);
        GridPoint curr = start;

        while (!stack.isEmpty()) {

            g.setColor(Color.orange);

            curr = stack.pop();
            visited.add(curr);

            PriorityQueue<GridPoint> queue = new PriorityQueue<>(new GPComparator(curr, intersection));

            if (curr.isFrontier()) {
                g.setColor(Color.BLUE);
                allFrontiers.add(curr);
            }

            g.fillOval(curr.x * xOffset, curr.y * yOffset, xOffset, yOffset);
            Thread.sleep(FFGraphics.shortDelay);

            for (GridPoint c : curr.getAllNeighbors()) {
                if (c != null && c.equals(start) && visited.containsAll(intersection) && distance.get(curr) > 2) {
                    Thread.sleep(FFGraphics.longDelay);
                    frontierGroups = AlgoLib.groupFrontiers(allFrontiers);
                    return frontierGroups;
                }
                if (c != null && !allFrontiers.contains(c) && (c.isFrontier() || c.isObstacle() || intersection.contains(c))) {

                    if (!visited.contains(c)) {
                        queue.add(c);
                        from.put(c, curr);
                        distance.put(c, distance.get(curr) + 1);
                    }
                }
            }

            while (!queue.isEmpty()) {
                stack.push(queue.poll());
            }

        }

        Thread.sleep(FFGraphics.longDelay);
        frontierGroups = AlgoLib.groupFrontiers(allFrontiers);
        return frontierGroups;
    }

}
