public class Robot {

    private GridPoint loc;
    private OccupancyGrid grid;
    private RobotScanner scanner;
    private int angle;

    public Robot(int x, int y, OccupancyGrid grid) {
        loc = new GridPoint(x, y, grid);
        this.grid = grid;
        scanner = new RobotScanner();
        angle = 0;
    }

    public GridPoint getLoc() {
        return loc;
    }

    public void setLoc(GridPoint loc) {
        this.loc = loc;
    }

    public OccupancyGrid getGrid() {
        return grid;
    }

    public void setGrid(OccupancyGrid grid) {
        this.grid = grid;
    }

    public RobotScanner getLaser() {
        return scanner;
    }

    public void setLaser(RobotScanner laser) {
        this.scanner = laser;
    }

    public int getAngle() {
        return angle;
    }

    public void setAngle(int angle) {
        this.angle = angle;
    }
}
