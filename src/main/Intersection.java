package main;

import java.util.List;

public class Intersection {
    public int[] point;
    public int direction;
    public List<int[]> road;

    public Intersection(int[] point, int direction, List<int[]> road) {
        this.point = point;
        this.direction = direction;
        this.road = road;
    }
}
