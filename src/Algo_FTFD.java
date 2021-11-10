import java.util.*;

public class Algo_FTFD {

    private Robot robot;
    private Set<GridPoint> activeAreas;
    private Set<Set<GridPoint>> frontierGroups;
    private Set<GridPoint> allFrontiers;

    public Algo_FTFD(Robot robot) {
        this.robot = robot;
        frontierGroups = new HashSet<>();
        allFrontiers = new HashSet<>();
    }

    public void initialWFD() {
        Queue<GridPoint> searchQueue = new LinkedList<>();
        Set<GridPoint> visited = new HashSet<>();

        searchQueue.add(robot.getLoc());

        while(!searchQueue.isEmpty()) {
            GridPoint p = searchQueue.poll();

            if(!visited.contains(p)) {
                visited.add(p);
                Set<GridPoint> connectingPoints = new HashSet<>();

                if (p.isFrontier()) {
                    allFrontiers.add(p);
                } else if(p.isFreeSpace()) {
                    for(GridPoint neighbor : p.getAllNeighbors()) {
                        searchQueue.add(neighbor);
                        connectingPoints.add(neighbor);
                    }
                }
            }

        }
        frontierGroups = AlteredKosaraju.groupFrontiers(allFrontiers);
    }

    public void ftfd() {
        if(allFrontiers.isEmpty()) {
            initialWFD();
            return;
        }

        Queue<GridPoint> searchQueue = new LinkedList<>();
        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> boundary = robot.getLaser().getScannerEndPoints(robot.getLoc().x, robot.getLoc().y, robot.getGrid(), robot.getAngle());
        Set<GridPoint> scan = robot.getLaser().getScannerReadings(robot.getLoc().x, robot.getLoc().y, robot.getGrid(), robot.getAngle());

        searchQueue.addAll(boundary);
        searchQueue.addAll(SetLib.intersection(allFrontiers, scan));

        while(!searchQueue.isEmpty()) {
            GridPoint c = searchQueue.poll();

            if(c.isFreeSpace()) {
                if(c.isFrontier()) {
                    allFrontiers.add(c);
                }
            }

            if(c.isFrontier() || c.isObstacle()) {
                for(GridPoint neighbor : c.getAllNeighbors()) {
                    if(!visited.contains(neighbor) && scan.contains(neighbor) && neighbor.isFreeSpace()) {
                        visited.add(neighbor);
                        searchQueue.add(neighbor);
                    }
                }
            }

        }

        for(GridPoint potentialFrontier : (HashSet<GridPoint>) SetLib.intersection(allFrontiers, scan)) {
            if(!potentialFrontier.isFrontier()) {
                allFrontiers.remove(potentialFrontier);
            }
        }

        frontierGroups = AlteredKosaraju.groupFrontiers(allFrontiers);
    }

}