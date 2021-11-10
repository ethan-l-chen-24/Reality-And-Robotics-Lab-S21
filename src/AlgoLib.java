import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class AlgLib {

    public static Set intersection(Set s1, Set s2) {
        Set intersection = new HashSet();

        for(Object c : s1) {
            if(s2.contains(c)) {
                intersection.add(c);
            }
        }

        return intersection;
    }

    // taken and altered from geeksForGeeks
    public static Set<GridPoint> bresenham(GridPoint start, GridPoint end) {

        Set<GridPoint> pointsInLine = new HashSet<>();
        int x1 = start.x; int y1 = start.y;
        int x2 = end.x; int y2 = end.y;

        int m_new = 2 * (y2 - y1);
        int slope_error_new = m_new - (x2 - x1);

        for (int x = x1, y = y1; x <= x2; x++)
        {
            pointsInLine.add(new GridPoint(x, y, start.g));

            // Add slope to increment angle formed
            slope_error_new += m_new;

            // Slope error reached limit, time to
            // increment y and update slope error.
            if (slope_error_new >= 0)
            {
                y++;
                slope_error_new -= 2 * (x2 - x1);
            }
        }

        return pointsInLine;
    }

    public static Set<Set<GridPoint>> groupFrontiers(Set<GridPoint> frontiers) {
        Set<GridPoint> visited = new HashSet<>();
        Set<Set<GridPoint>> frontierGroups = new HashSet<>();
        Stack<GridPoint> l = new Stack<>();
        for(GridPoint point : frontiers) {
            if(!visited.contains(point)) {
                HashSet<GridPoint> frontier = new HashSet<>();
                visited.add(point);
                l.add(point);

                while(!l.isEmpty()) {
                    for(GridPoint neighbor : point.getAllNeighbors()) {
                        for(GridPoint existingFrontier : frontiers) {
                            if(neighbor.equals(existingFrontier)) {
                                frontier.add(neighbor);
                            }
                        }
                    }
                }
                frontierGroups.add(frontier);
            }
        }
        return frontierGroups;
    }

}
