package main;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MazeMapper {

    public MazeGenerator mazeGenerator;
    public List<int[]> intersections;
    public List<List<int[]>> connections;
    public List<List<int[]>> roads;
    public int queque;
    public int interProgress;

    public MazeMapper(MazeGenerator mazeGenerator) {
        this.mazeGenerator = mazeGenerator;
    }

    public void mapMaze(int[] startPoint) {
        intersections = new ArrayList<>();
        connections = new ArrayList<>();
        roads = new ArrayList<>();

        connections.add(new ArrayList<int[]>());
        intersections.add(startPoint);
        interProgress = 0;
        queque = 0;

        while(queque < intersections.size()) {
            int currentIndex = queque;
            int progress = interProgress;
            int[] currentPoint = intersections.get(currentIndex);
            boolean[] possibleDirections = mazeGenerator.getWallsPoint(currentPoint);
            List<int[]> currentConnections = connections.get(currentIndex);
            addToConnections(currentConnections, possibleDirections);

            for(int k = progress; k < currentConnections.size(); k++) {
                int direction = currentConnections.get(k)[0];
                Intersection newIntersection = findNextIntersection(currentPoint, direction);
                intersections.add(newIntersection.point);
                roads.add(newIntersection.road);
                currentConnections.get(k)[1] = intersections.size() - 1;
                currentConnections.get(k)[2] = roads.size() - 1;
                connections.add(new ArrayList<int[]>(Arrays.asList(
                        new int[]
                                {newIntersection.direction,
                                        currentIndex,
                                        roads.size() - 1}
                        )));
                interProgress = 1;

            }

            queque += 1;
        }

    }

    public void addToConnections(List<int[]> pointConnections, boolean[] possibleDirections) {
        int[] directionsPresent = new int[pointConnections.size()];
        for(int i = 0; i < pointConnections.size(); i++) {
            directionsPresent[i] = pointConnections.get(i)[0];
        }

        for(int i = 0; i < possibleDirections.length; i++) {
            if(!inIntArray(directionsPresent, i) & possibleDirections[i]) {
                pointConnections.add(new int[]{i, -1, -1});
            }
        }
    }

    public Intersection findNextIntersection(int[] startPoint, int startDirection) {

        List<int[]> road = new ArrayList<>();
        int direction = startDirection;
        int numberOfDirections = 1;
        int[] nextPoint = startPoint;

        while(numberOfDirections == 1) {
            nextPoint = getNextPoint(nextPoint, direction);
            boolean[] possibleDirections = mazeGenerator.getWallsPoint(nextPoint);
            possibleDirections[invertDirection(direction)] = false;
            numberOfDirections = BitConverter.numberOfTrue(possibleDirections);
            if(numberOfDirections == 1) {
                road.add(nextPoint);
                direction = BitConverter.getDirection(possibleDirections);
            }
        }

        Intersection returnIntersection = new Intersection(nextPoint,
                invertDirection(direction),
                road);

        return returnIntersection;

    }

    public int[] getNextPoint(int[] point, int direction) {
        int x;
        int y;

        if(direction == 0) {
            x = point[0];
            y = point[1] - 1;
        } else if(direction == 2) {
            x = point[0];
            y = point[1] + 1;
        } else if(direction == 1) {
            x = point[0] + 1;
            y = point[1];
        } else {
            x = point[0] - 1;
            y = point[1];
        }

        return new int[]{x, y};
    };

    public int invertDirection(int direction) {
        return (direction + 2)%4;
    }

    public boolean inIntArray(int[] array, int number) {
        for(int i : array) {
            if(i == number) {
                return true;
            }
        }
        return false;
    }

    public int getSize() {
        int size = 0;
        size += intersections.size();

        for(List<int[]> road : roads) {
            size += road.size();
        }

        return size;
    }
}
