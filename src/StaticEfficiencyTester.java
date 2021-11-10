import java.awt.*;
import java.util.ArrayList;
import java.util.Timer;

public class EfficiencyTester {

    static long start;
    static long end;
    static ArrayList<Double> timeStamps = new ArrayList<>();

    public static void main(String[] args) {
        System.out.println("smart");

        OccupancyGrid eleven = new OccupancyGrid(11, 11, "src/Grids/tester-11.txt");
        Robot r11 = new Robot(5, 5, eleven);

        start = System.currentTimeMillis();
        r11.getFrontiers();
        end = System.currentTimeMillis();

        timeStamps.add((double) (end - start)/1000);

        System.out.println(timeStamps);

        OccupancyGrid twentyOne = new OccupancyGrid(21, 21, "src/Grids/tester-21.txt");
        Robot r21 = new Robot(10, 10, twentyOne);

        start = System.currentTimeMillis();
        r21.getFrontiers();
        end = System.currentTimeMillis();

        timeStamps.add((double) (end - start)/1000);

        System.out.println(timeStamps);

        OccupancyGrid fiftyOne = new OccupancyGrid(51, 51, "src/Grids/tester-51.txt");
        Robot r51 = new Robot(25, 25, fiftyOne);

        start = System.currentTimeMillis();
        r51.getFrontiers();
        end = System.currentTimeMillis();

        timeStamps.add((double) (end - start)/1000);

        System.out.println(timeStamps);

        OccupancyGrid oneHundredOne = new OccupancyGrid(101, 101, "src/Grids/tester-101.txt");
        Robot r101 = new Robot(50, 50, oneHundredOne);

        start = System.currentTimeMillis();
        r101.getFrontiers();
        end = System.currentTimeMillis();

        timeStamps.add((double) (end - start)/1000);

        System.out.println(timeStamps);

        OccupancyGrid fiveHundredOne = new OccupancyGrid(501, 501, "src/Grids/tester-501.txt");
        Robot r501 = new Robot(250, 250, fiveHundredOne);

        start = System.currentTimeMillis();
        r501.getFrontiers();
        end = System.currentTimeMillis();

        timeStamps.add((double) (end - start)/1000);

        System.out.println(timeStamps);

        OccupancyGrid oneThousandOne = new OccupancyGrid(1001, 1001, "src/Grids/tester-1001.txt");
        Robot r1001 = new Robot(500, 500, oneThousandOne);

        start = System.currentTimeMillis();
        r1001.getFrontiers();
        end = System.currentTimeMillis();

        timeStamps.add((double) (end - start)/1000);

        System.out.println(timeStamps);

        OccupancyGrid fiveThousandOne = new OccupancyGrid(5001, 5001, "src/Grids/tester-5001.txt");
        Robot r5001 = new Robot(2500, 2500, fiveThousandOne);

        start = System.currentTimeMillis();
        r5001.getFrontiers();
        end = System.currentTimeMillis();

        timeStamps.add((double) (end - start)/1000);

        System.out.println(timeStamps);


    }


}
