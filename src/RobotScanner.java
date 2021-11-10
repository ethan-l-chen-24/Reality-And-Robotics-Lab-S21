import java.util.HashSet;
import java.util.Set;

public class RobotScanner {

    public static final int scannerRadius = 2;

    public static Set<GridPoint> getScannerReadings(int xCoord, int yCoord, OccupancyGrid grid, double radiansOffAxis) {

        Set<GridPoint> allPoints = new HashSet<>();

        double dxTerminalC = scannerRadius*Math.cos(radiansOffAxis);
        double dyTerminalC = scannerRadius*Math.sin(radiansOffAxis);
        double dxTerminalCC = scannerRadius*Math.cos(radiansOffAxis + Math.PI/2);
        double dyTerminalCC = scannerRadius*Math.sin(radiansOffAxis + Math.PI/2);



        for(int x = Math.max(0, xCoord-scannerRadius); x<=xCoord+scannerRadius; x++) {
            for(int y = Math.max(0, yCoord-scannerRadius); y<=yCoord+scannerRadius; y++) {

                if(Math.pow(x-xCoord, 2) + Math.pow(y-yCoord, 2) <= Math.pow(scannerRadius, 2)) {
                    if((x-xCoord)*(dyTerminalC) - (y-yCoord)*(dxTerminalC-xCoord) <= 0) {
                        if((x-xCoord)*(dyTerminalCC-yCoord) - (y-yCoord)*(dxTerminalCC-xCoord) >= 0) {
                            allPoints.add(new GridPoint(x, y, grid));
                        }
                    }
                }

            }
        }

        return allPoints;

    }

    public static Set<GridPoint> getScannerEndPoints(int xCoord, int yCoord, OccupancyGrid grid, double radiansOffAxis) {
        Set<GridPoint> fullReading = getScannerReadings(xCoord, yCoord, grid, radiansOffAxis);
        Set<GridPoint> boundary = new HashSet<>();

        for(GridPoint p : fullReading) {
            for(GridPoint neighbor : p.getAllNeighbors()) {
                if(!neighbor.isFreeSpace()) {
                    boundary.add(p);
                }
            }
        }

        return boundary;
    }

}
