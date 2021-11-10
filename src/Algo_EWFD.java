import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Set;

public class Algo_EWFD {

    private Robot robot;
    private Set<GridPoint> activeAreas;
    private Set<Set<GridPoint>> frontierGroups;
    private Set<GridPoint> allFrontiers;

    public Algo_EWFD(Robot robot) {
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
                if (p.isFrontier()) {
                    allFrontiers.add(p);
                } else if(p.isFreeSpace()) {
                    for(GridPoint neighbor : p.getAllNeighbors()) {
                        searchQueue.add(neighbor);
                    }
                }
            }

        }
        frontierGroups = AlteredKosaraju.groupFrontiers(allFrontiers);
    }

    public void ewfd() {
        if(allFrontiers.isEmpty()) {
            initialWFD();
            return;
        }

        Queue<GridPoint> searchQueue = new LinkedList<>();
        Set<GridPoint> visited = new HashSet<>();
        Set<GridPoint> scan = robot.getLaser().getScannerReadings(robot.getLoc().x, robot.getLoc().y, robot.getGrid(), robot.getAngle());
        Set<GridPoint> intersectionSet = SetLib.intersection(visited, scan);

        searchQueue.addAll(intersectionSet);

        while(!searchQueue.isEmpty()) {
            GridPoint c = searchQueue.poll();
            visited.add(c);
            if(c.isFreeSpace()) {
                if(c.isFrontier()) {
                    allFrontiers.add(c);
                } else if(allFrontiers.contains(c)) {
                    allFrontiers.remove(c);
                }

                Set<GridPoint> neighborSet = new HashSet<>();
                for(GridPoint neighbor : c.getAllNeighbors()) {
                    neighborSet.add(neighbor);
                }

                Set<GridPoint> newSearch = SetLib.intersection(neighborSet, scan);
                searchQueue.addAll(newSearch);

            }
        }

        frontierGroups = AlteredKosaraju.groupFrontiers(allFrontiers);
    }

}
