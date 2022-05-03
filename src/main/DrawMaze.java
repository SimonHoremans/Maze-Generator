package main;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DrawMaze {

    public int tileSize;
    public int wallSize;
    public int screenWidth;
    public int screenHeight;
    public int directionWidth;
    public int directionHeight;
    public Image[] directionTiles;
    public Image[] directionWalls;
    public final Color tileColor = Color.white;
    public final Color directionColor = Color.decode("#306998");
    public final Color deadEndColor = Color.red;
    public final Color intersectionColor = Color.green;

    public int getTileSize() {
        return tileSize;
    }

    public int getWallSize() {
        return wallSize;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public static List<Integer> convertToDisplay(List<Integer> original) {
        List<Integer> newCords = new ArrayList<>();
        for(int i = 0; i < original.size(); i++) {
            int newCor = 2*(original.get(i)+1) - 1;
            newCords.add(newCor);
        }

        return newCords;
    }

    public DrawMaze(int tileSize, int wallSize, int columns, int rows, boolean directionTiles) {
        this.tileSize = tileSize;
        this.wallSize = wallSize;
        screenWidth = tileSize*columns + wallSize*(columns+1);
        screenHeight = tileSize*rows + wallSize*(rows+1);
        if(directionTiles) {
            if(tileSize%3 != 0) {
                throw new IndexOutOfBoundsException("Tile size must be divisible by 3");
            }
            directionWidth = tileSize/3;
            directionHeight = (tileSize-directionWidth)/2;
            makeDirections();
        }
    }

    public void AddDirection(int direction, Graphics2D g2) {
        int x;
        int y;
        int width;
        int height;
        Graphics2D newG2 = g2;

        if(direction == 0 || direction == 2) {
            width = directionWidth;
            height = directionHeight;
        } else if(direction == 1 || direction == 3) {
            width = directionHeight;
            height =directionWidth;
        } else {
            width = directionWidth;
            height = directionWidth;
        }


        if(direction == 0) {
            x = directionWidth;
            y = 0;
        } else if(direction == 1) {
            x = tileSize - directionHeight;
            y = directionHeight;
        } else if(direction == 2) {
            x = directionWidth;
            y = tileSize - directionHeight;
        } else if(direction == 3){
            x = 0;
            y = directionWidth;
        } else {
            x = directionHeight;
            y = directionHeight;
        }

        newG2.fillRect(x, y, width, height);
    }

    public void makeDirections() {
        makeDirectionTiles(true);
        makeDirectionWalls();
    }

    public void makeDirectionTiles(boolean special) {
        directionTiles = new Image[15];

        for(int directionIndex = 0; directionIndex < 15; directionIndex++) {
            Image directionTile = new BufferedImage(tileSize, tileSize, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D)directionTile.getGraphics();

            boolean[] directionBits = BitConverter.convertToBool(directionIndex);

            int trueNumber;
            if(special) {
                trueNumber = BitConverter.numberOfTrue(directionBits);
            } else {
                trueNumber = 2;
            }

            if(trueNumber == 2) {
                g2.setColor(tileColor);
                g2.fillRect(0, 0, tileSize, tileSize);
                g2.setColor(directionColor);
                boolean first = true;
                for(int bitIndex = 0; bitIndex < directionBits.length; bitIndex++) {
                    if(!directionBits[bitIndex]) {
                        if(first) {
                            AddDirection(4, g2);
                            first = false;
                        }
                        AddDirection(bitIndex, g2);
                    }
                }
            } else if(trueNumber == 3) {
                g2.setColor(deadEndColor);
                g2.fillRect(0, 0, tileSize, tileSize);
            } else {
                g2.setColor(intersectionColor);
                g2.fillRect(0, 0, tileSize, tileSize);
            }
            directionTiles[directionIndex] = directionTile;
        }
    }

    public void makeDirectionWalls() {
        directionWalls = new Image[2];

        for(int i = 0; i < 2; i++) {
            int width;
            int height;

            if(i == 0) {
                width = tileSize;
                height = wallSize;
            } else {
                width = wallSize;
                height = tileSize;
            }

            Image directionWall = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = (Graphics2D)directionWall.getGraphics();
            g2.setColor(tileColor);
            g2.fillRect(0, 0, width, height);
            g2.setColor(directionColor);

            if(i == 0) {
                g2.fillRect(directionWidth, 0, directionWidth, wallSize);
            } else {
                g2.fillRect(0, directionWidth, wallSize, directionWidth);
            }
            directionWalls[i] = directionWall;
        }
    }

    public Image getDirectionTile(int index) {
        return directionTiles[index];
    }

    public List<Integer> getCube(List<Integer> coordinates) {
        int x = coordinates.get(0);
        int y = coordinates.get(1);

        int xCube = x*(tileSize+wallSize) + wallSize;
        int yCube = y*(tileSize+wallSize) + wallSize;

//        System.out.println(tileSize);
//        System.out.println(wallSize);

        return Arrays.asList(xCube, yCube, tileSize, tileSize);
    }

    public List<Integer> getCube(int[] coordinates) {
        int x = coordinates[0];
        int y = coordinates[1];

        int xCube = x*(tileSize+wallSize) + wallSize;
        int yCube = y*(tileSize+wallSize) + wallSize;

//        System.out.println(tileSize);
//        System.out.println(wallSize);

        return Arrays.asList(xCube, yCube, tileSize, tileSize);
    }

    public void drawDirectionTile(int[] coordinates, Graphics g, int tileIndex) {
        int x = coordinates[0];
        int y = coordinates[1];

        int xCube = x*(tileSize+wallSize) + wallSize;
        int yCube = y*(tileSize+wallSize) + wallSize;

        g.drawImage(directionTiles[tileIndex], xCube, yCube, null);
    }

    public void drawDirectionWall(int[] coordinatesA, int[] coordinatesB, Graphics g) {
        int xA = coordinatesA[0];
        int yA = coordinatesA[1];
        int xB = coordinatesB[0];
        int yB = coordinatesB[1];


        int xDiff = xB - xA;
        int yDiff = yB - yA;

        int xWall;
        int yWall;
        int wallIndex;

        if(xDiff != 0) {
            xWall = (Math.max(xDiff, 0)+xA)*(tileSize+wallSize);
            yWall = yA*(tileSize+wallSize) + wallSize;
            wallIndex = 1;
        } else {
            xWall = xA*(tileSize+wallSize) + wallSize;
            yWall = (Math.max(yDiff, 0)+yA)*(tileSize+wallSize);
            wallIndex = 0;
        }

        g.drawImage(directionWalls[wallIndex], xWall, yWall, null);
    }

    public List<Integer> getWall(List<Integer> coordinatesA, List<Integer> coordinatesB) {
        int xA = coordinatesA.get(0);
        int yA = coordinatesA.get(1);
        int xB = coordinatesB.get(0);
        int yB = coordinatesB.get(1);


        int xDiff = xB - xA;
        int yDiff = yB - yA;

        int xWall;
        int yWall;
        int width;
        int height;

        if(xDiff != 0) {
            xWall = (Math.max(xDiff, 0)+xA)*(tileSize+wallSize);
            yWall = yA*(tileSize+wallSize) + wallSize;
            width = wallSize;
            height = tileSize;
        } else {
            xWall = xA*(tileSize+wallSize) + wallSize;
            yWall = (Math.max(yDiff, 0)+yA)*(tileSize+wallSize);
            width = tileSize;
            height = wallSize;
        }

        return Arrays.asList(xWall, yWall, width, height);
    }

    public List<Integer> getWall(int[] coordinatesA, int[] coordinatesB) {
        int xA = coordinatesA[0];
        int yA = coordinatesA[1];
        int xB = coordinatesB[0];
        int yB = coordinatesB[1];


        int xDiff = xB - xA;
        int yDiff = yB - yA;

        int xWall;
        int yWall;
        int width;
        int height;

        if(xDiff != 0) {
            xWall = (Math.max(xDiff, 0)+xA)*(tileSize+wallSize);
            yWall = yA*(tileSize+wallSize) + wallSize;
            width = wallSize;
            height = tileSize;
        } else {
            xWall = xA*(tileSize+wallSize) + wallSize;
            yWall = (Math.max(yDiff, 0)+yA)*(tileSize+wallSize);
            width = tileSize;
            height = wallSize;
        }

        return Arrays.asList(xWall, yWall, width, height);
    }

    public List<Integer> getWall(int[] wall) {
        int x;
        int y;
        int width;
        int height;


        if(wall[0] == 0) {
            y = wall[1]*(tileSize+wallSize) + wallSize;
            x = (wall[2]+1)*(tileSize+wallSize);
            width = wallSize;
            height = tileSize;
        } else {
            x = wall[1]*(tileSize+wallSize) + wallSize;
            y = (wall[2]+1)*(tileSize+wallSize);
            height = wallSize;
            width = tileSize;
        }

        return Arrays.asList(x, y, width, height);
    }
}
