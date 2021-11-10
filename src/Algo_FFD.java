import java.util.HashSet;
import java.util.Set;

public class Algo_FFD {

    private Robot robot;
    private Set<GridPoint> activeAreas;
    private Set<Set<GridPoint>> frontierGroups;
    private Set<GridPoint> allFrontiers;

    public Algo_FFD(Robot robot) {
        this.robot = robot;
        frontierGroups = new HashSet<>();
        allFrontiers = new HashSet<>();
    }

    public void ffd() {

    }

}
