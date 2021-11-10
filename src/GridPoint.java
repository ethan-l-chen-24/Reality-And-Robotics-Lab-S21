import java.util.Iterator;

public class GridPoint {

    public int x;
    public int y;
    OccupancyGrid g;

    public GridPoint(int x, int y, OccupancyGrid g) {
        this.x = x;
        this.y = y;
        this.g = g;
    }

    public boolean isFrontier() {

        if (g.grid[x][y] == 1) {

            for(GridPoint neighbor : getDirectNeighbors()) {
                if (g.grid[neighbor.x][neighbor.y] == 0) {
                    return true;
                }
            }
            return false;

        } else {
            return false;
        }
    }

    public boolean isFreeSpace() {
        if(g.grid[x][y] == 1) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isObstacle() {
        if(g.grid[x][y] == 2) {
            return true;
        } else {
            return false;
        }
    }

    public boolean hasFreeSpaceNeighbor() {
        for(GridPoint z : getAllNeighbors()) {
            if(g.grid[z.x][z.y] == 1) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        return "(" + x + ", " + y + ")";
    }

    @Override
    public boolean equals(Object o2) {
        if(!(o2 instanceof GridPoint)) {
            return false;
        } else {
            if(this.x == ((GridPoint) o2).x && this.y == ((GridPoint) o2).y) {
                return true;
            }
        }
        return false;
    }

    public Iterable<GridPoint> getDirectNeighbors() {
        return new Iterable<GridPoint>() {
            @Override
            public Iterator<GridPoint> iterator() {
                return new Iterator<GridPoint>() {

                    int counted = 0;

                    @Override
                    public boolean hasNext() {
                        if(counted < 4) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public GridPoint next() {
                        GridPoint p = null;
                        if(counted == 0) {
                            p = new GridPoint(x, y-1, g);
                        } else if(counted == 1) {
                            p = new GridPoint(x+1, y, g);
                        } else if(counted == 2) {
                            p = new GridPoint(x, y+1, g);
                        } else if(counted == 3){
                            p = new GridPoint(x-1, y, g);
                        }
                        counted++;
                        return p;
                    }
                };
            }
        };
    }

    public Iterable<GridPoint> getAllNeighbors() {
        return new Iterable<GridPoint>() {
            @Override
            public Iterator<GridPoint> iterator() {
                return new Iterator<GridPoint>() {

                    int counted = 0;

                    @Override
                    public boolean hasNext() {
                        if(counted < 8) {
                            return true;
                        } else {
                            return false;
                        }
                    }

                    @Override
                    public GridPoint next() {
                        GridPoint p = null;
                        if(counted == 0) {
                            p = new GridPoint(x, y-1, g);
                        } else if(counted == 1) {
                            p = new GridPoint(x+1, y-1, g);
                        } else if(counted == 2) {
                            p = new GridPoint(x+1, y, g);
                        } else if(counted == 3){
                            p = new GridPoint(x+1, y+1, g);
                        } else if(counted == 4) {
                            p = new GridPoint(x, y+1, g);
                        } else if(counted == 5) {
                            p = new GridPoint(x-1, y+1, g);
                        } else if(counted == 6) {
                            p = new GridPoint(x-1, y, g);
                        } else if(counted == 7){
                            p = new GridPoint(x-1, y-1, g);
                        }
                        counted++;
                        return p;
                    }
                };
            }
        };
    }

}
