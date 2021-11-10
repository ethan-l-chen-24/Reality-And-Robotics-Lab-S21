import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Algo_Naive {

    private Robot robot;
    private Set<Set<GridPoint>> frontierGroups;

    public Algo_Naive(Robot robot) {
        this.robot = robot;
        frontierGroups = new HashSet<>();
    }

    public void naive() {

        Set<GridPoint> frontiers = new HashSet<>();
        OccupancyGrid g = robot.getGrid();

        for(int i = 0; i<g.grid.length; i++) {
            for(int j = 0; j<g.grid[0].length; j++) {
                GridPoint p = new GridPoint(i, j, g);
                if(p.isFrontier()) {
                    frontiers.add(p);
                }
            }
        }

        frontierGroups = AlteredKosaraju.groupFrontiers(frontiers);
    }

}
