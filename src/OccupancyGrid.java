import java.io.BufferedReader;
import java.io.FileReader;

/**
 * Occupancy Grid
 * --------------------
 *
 * stores a map of explored/unexplored regions, as well as relevant methods
 *
 * author -> Ethan Chen
 * date -> July 1, 2021
 */

public class OccupancyGrid {

    /**
     * VARIABLES
     * --------------------
     */

    public int[][] grid;


    /**
     * CONSTRUCTORS
     * --------------------
     */

    public OccupancyGrid(int width, int height, String filename) { // preloads a grid from a text file
        this.grid = new int[width][height];
        loadGrid(filename);
    }

    public OccupancyGrid(int width, int height) {
        this.grid = new int[width][height];
    }


    /**
     * METHODS
     * --------------------
     */

    /**
     * loads the grid from the txt file
     * @param filename the txt file that holds the grid
     */
    private void loadGrid(String filename) {

        try {

            BufferedReader gridReader = new BufferedReader(new FileReader(filename));

            String line;
            boolean firstLine = true;
            while((line = gridReader.readLine()) != null) {
                String[] tags = line.split(" ");
                int x = Integer.parseInt(tags[0]);
                int y = Integer.parseInt(tags[1]);
                boolean open = (tags[2].equals("open"));

                if(open) {
                    this.grid[x][y] = 1;
                } else {
                    this.grid[x][y] = 2;
                }
            }

        }
        catch (Exception e) {
            System.out.println("this is not a valid file");
        }

    }

}
