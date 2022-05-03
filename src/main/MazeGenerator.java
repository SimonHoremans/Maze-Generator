package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MazeGenerator {
    public int columns;
    public int rows;
    public int[] startCoordinates;
    public boolean[][] mazeArray;
    public List<int[]> track;
    public List<int[][]> maze;
    public boolean[][][] mazeWalls;
    public final int[][] directions = new int[][]{
            {0, 1},
            {1, 0},
            {0, -1},
            {-1, 0}
    };

    public MazeGenerator(int columns, int rows, int[] startCoordinates) {
        this.columns = columns;
        this.rows = rows;
        if(!inMazeArray(startCoordinates)) {
            throw new IndexOutOfBoundsException("The start coordinates do not respect the boundaries of your maze.");
        }
        mazeArray = new boolean[columns][rows];
        mazeArray[startCoordinates[0]][startCoordinates[1]] = true;
        this.startCoordinates = startCoordinates;
        track = new ArrayList<>(Arrays.asList(startCoordinates));
        maze = new ArrayList<>();
        mazeWalls = new boolean[2][][];
        mazeWalls[0] = new boolean[rows][columns-1];
        mazeWalls[1] = new boolean[columns][rows-1];
    }

    public void generateMaze() {
//        int counterthingy = 0;
        while(track.size() > 0) {
            int currentTrackIndex = track.size()-1;
            int[] currentPoint = track.get(currentTrackIndex);
            List<int[]> possibleDirections = getDirections(currentPoint);

//            System.out.println(nobspls(possibleDirections));
//            System.out.println(nobspls(track));
//            System.out.println();

            if(possibleDirections.size() > 0) {
                int[] newPoint = getRandomPoint(possibleDirections);
                maze.add(new int[][]{currentPoint, newPoint});
                mazeArray[newPoint[0]][newPoint[1]] = true;
                track.add(newPoint);
            } else {
                track.remove(currentTrackIndex);
            }

//            counterthingy++;
//            if(counterthingy > 10) {
//                break;
//            }

//            System.out.println(track.toString());
        }
    }

    public void generateWalls() {
        for(int[][] blocks : maze) {
            int xA = blocks[0][0];
            int yA = blocks[0][1];
            int xB = blocks[1][0];
            int yB = blocks[1][1];

            int orientation;
            int line;
            int wall;

            int yDiff = yB - yA;
            int xDiff = xB - xA;

            int diff;

            if (xDiff != 0) {
                orientation = 0;
                line = yA;
                wall = xA;
                diff = xDiff;
            } else {
                orientation = 1;
                line = xA;
                wall = yA;
                diff = yDiff;
            }
            if(diff < 0) {
                wall -= 1;
            }

            mazeWalls[orientation][line][wall] = true;
        }
    }

    public boolean[][][] getMazeWalls() {
        return mazeWalls;
    }

    public boolean[][] getMazeArray() {
        return mazeArray;
    }

    private int[] getRandomPoint(List<int[]> points) {
        int randomIndex = (int)Math.round(Math.random()*(points.size()-1));
        return points.get(randomIndex);
    }

    private boolean inMazeArray(int[] point) {
        int x = point[0];
        int y = point[1];

        return (x >= 0 && x < columns) &&
                (y >= 0 && y < rows);
    }

    private List<int[]> getDirections(int[] point) {
        List<int[]> possibleDirections = new ArrayList<>();

        for(int[] direction : this.directions) {
            int [] possibleDirection = new int[]{
                    direction[0] + point[0],
                    direction[1] + point[1]
            };
            if(inMazeArray(possibleDirection) && !mazeArray[possibleDirection[0]][possibleDirection[1]]) {
                possibleDirections.add(possibleDirection);
            }
        }

        return possibleDirections;
    }

    public List<int[][]> getMaze() {
        return maze;
    }

    public boolean[] getWallsPoint(int[] point) {
        boolean[] walls = new boolean[4];

        if(point[0] < columns-1) {
            walls[1] = mazeWalls[0][point[1]][point[0]];
        } else {
            walls[1] = false;
        }

        if(point[0] > 0) {
            walls[3] = mazeWalls[0][point[1]][point[0]-1];
        } else {
            walls[3] = false;
        }

        if(point[1] < rows-1) {
            walls[2] = mazeWalls[1][point[0]][point[1]];
        } else {
            walls[2] = false;
        }

        if(point[1] > 0) {
            walls[0] = mazeWalls[1][point[0]][point[1]-1];
        } else {
            walls[0] = false;
        }

        return walls;

    }

//    public String nobspls(List<int[]> list) {
//        String returnString = "[";
//        for(int[] point : list) {
//            returnString += Arrays.toString(point) + ", ";
//        }
//        returnString += "]";
//        return returnString;
//    }

    public static String nobspls(List<int[][]> list) {
        String returnString = "[";
        for(int[][] point : list) {
            returnString += Arrays.deepToString(point) + ", ";
        }
        returnString += "]";
        return returnString;
    }
}
