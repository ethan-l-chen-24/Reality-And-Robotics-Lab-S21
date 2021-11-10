import java.util.*;

public class Algo_WFD {

    private Robot robot;
    private HashSet<HashSet<GridPoint>> frontierGroups;

    public Algo_WFD(Robot robot) {
        this.robot = robot;
        frontierGroups = new HashSet<>();
    }

    public void wfd() {
        HashSet<HashSet<GridPoint>> frontiers = new HashSet<>();
        Queue<GridPoint> searchQueue = new LinkedList<>();
        Queue<GridPoint> frontierQueue;
        Set<GridPoint> mapOpenList = new HashSet<>();
        Set<GridPoint> mapCloseList = new HashSet<>();
        Set<GridPoint> frontierOpenList = new HashSet<>();
        Set<GridPoint> frontierCloseList = new HashSet<>();

        searchQueue.add(robot.getLoc());

        while(!searchQueue.isEmpty()) {
            GridPoint p = searchQueue.poll();

            if(mapCloseList.contains(p)) {
                continue;
            }
            if (p.isFrontier()) {

                frontierQueue = new LinkedList<>();
                HashSet<GridPoint> frontier = new HashSet<>();
                frontierQueue.add(p);
                frontierOpenList.add(p);

                while(!frontierQueue.isEmpty()) {
                    GridPoint q = frontierQueue.poll();
                    if(mapCloseList.contains(q) || frontierCloseList.contains(q)) {
                        continue;
                    }
                    if(q.isFrontier()) {
                        frontier.add(q);
                        for(GridPoint w : q.getAllNeighbors()) {
                            if(!(frontierOpenList.contains(w) || frontierCloseList.contains(w) || mapCloseList.contains(w))) {
                                frontierQueue.add(w);
                                frontierOpenList.add(w);
                            }
                        }
                    }
                    frontierCloseList.add(q);
                }
                frontiers.add(frontier);
            }

            for(GridPoint v : p.getAllNeighbors()) {
                if(!(mapOpenList.contains(v) || mapCloseList.contains(v)) && p.hasFreeSpaceNeighbor()) {
                    searchQueue.add(v);
                    mapOpenList.add(v);
                }
            }
            mapCloseList.add(p);
        }

        frontierGroups = frontiers;
    }

}
