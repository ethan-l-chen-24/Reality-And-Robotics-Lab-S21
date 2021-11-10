import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class Algo_NaiveAA {

    private Robot robot;
    private Set<GridPoint> activeAreas;
    private Set<Set<GridPoint>> frontierGroups;
    private Set<GridPoint> allFrontiers;

    public Algo_NaiveAA(Robot robot) {
        this.robot = robot;
        activeAreas = new HashSet<>();
        frontierGroups = new HashSet<>();
        allFrontiers = new HashSet<>();
    }

    public void naive() {

        updateActiveAreas();

        for(GridPoint p : activeAreas) {
            if(p.isFrontier()) {
                allFrontiers.add(p);
            } else if(allFrontiers.contains(p)) {
                allFrontiers.remove(p);
            }
        }

        frontierGroups = AlteredKosaraju.groupFrontiers(allFrontiers);
    }

    public void updateActiveAreas() {
        activeAreas.addAll(robot.getLaser().getScannerReadings(robot.getLoc().x, robot.getLoc().y, robot.getGrid(), robot.getAngle()));
    }

}
