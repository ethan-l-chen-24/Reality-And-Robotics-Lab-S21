import java.sql.SQLOutput;

/**
 * Tester
 * --------------------
 *
 * a console tester for my algorithms
 *
 * @author Ethan Chen
 * @date_created July 14, 2021
 */

public class UseabilityTester {

    public static void main(String[] args) {

        OccupancyGrid grid = new OccupancyGrid(10, 10, "src/Grids/grid-0.txt");

        Robot r = new Robot(5, 5, grid);

        printGrid(grid.grid);
        System.out.println(r.getFrontiers());

        r.move(1, 1);
        printGrid(grid.grid);
        System.out.println(r.getFrontiers());

    }

    /**
     * prints out the state of the occupancy grid in the console
     * @param grid the occupancy grid
     */
    public static void printGrid(GridPoint[][] grid) {

        System.out.print("  y 0");
        for(int x = 1; x<grid.length; x++) {
            if(x < 10) {
                System.out.print(" ");
            }
            System.out.print(" " + x);
        }
        System.out.println("");
        System.out.print("x");

        for(int i = 0; i<grid.length; i++) {
            System.out.println();
            System.out.print(i + "  ");
            if(i < 10) {
                System.out.print(" ");
            }

            for(int j = 0; j<grid[0].length; j++)  {
                System.out.print(grid[i][j].state() + "  ");

            }
        }

        System.out.println();
        System.out.println();
    }

}
