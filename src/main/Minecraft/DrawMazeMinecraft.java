package main.Minecraft;

public class DrawMazeMinecraft {
    public static int[] getTile(int[] coordinate) {
        return new int[]{((coordinate[0] + 1)*2 - 1), ((coordinate[1] + 1)*2 - 1)};
    }

    public static TileWall getTileWall(int[] coordinateA, int[] coordinateB) {
        int[] tileA = getTile(coordinateA);
        int[] tileB = getTile(coordinateB);

        int xA = tileA[0];
        int yA = tileA[1];

        int xB = tileB[0];
        int yB = tileB[1];

        TileWall tileWall = new TileWall();
        tileWall.tileA = tileA;
        tileWall.tileB = tileB;
        tileWall.wall = new int[]{(xA + xB)/2, (yA + yB)/2};

        return tileWall;
    }
}
