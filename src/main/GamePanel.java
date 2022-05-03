package main;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.Buffer;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class GamePanel extends JPanel implements Runnable{

    public DrawMaze drawMaze;
    public MazeGenerator mazeGenerator;
    public int drawCounter;
    public int intersectionCounter;
    public int connectionCounter;
    public int drawCounterMax;
    public List<int[][]> maze;
    public boolean[][][] mazeWalls;
    public Image drawing;
    public boolean drawSwitch;



    public int screenWidth;
    public int screenHeight;
    final int columns = 75;
    final int rows = 50;
    public int fps = 150;
    boolean mazeDone = false;

    Thread gameThread;
    KeyHandler keyH = new KeyHandler();


    public GamePanel() {
        mazeGenerator = new MazeGenerator(columns, rows, new int[]{0, 0});
        mazeGenerator.generateMaze();
        mazeGenerator.generateWalls();

        System.out.println(Arrays.toString(mazeGenerator.getWallsPoint(new int[]{1, 1})));

        drawCounter = -1;
        intersectionCounter = 0;
        connectionCounter = 0;
        maze = mazeGenerator.getMaze();
        mazeWalls = mazeGenerator.getMazeWalls();
        drawCounterMax = maze.size();
        drawMaze = new DrawMaze(12, 4, columns, rows, true);
        screenWidth = drawMaze.getScreenWidth();
        screenHeight = drawMaze.getScreenHeight();

        drawSwitch = true;

        drawing = new BufferedImage(screenWidth, screenHeight, BufferedImage.TYPE_INT_RGB);
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.BLACK);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        long previousTime = System.currentTimeMillis();
        while(gameThread != null) {
//            System.out.println("The loop is running");
            long currentTime = System.currentTimeMillis();
            long timeDif = currentTime - previousTime;
            float fps;
            if(timeDif != 0) {
                fps = 1000/timeDif;
            }
            else {
                fps = -1;
            }
//            update();
//            repaint();
            if(fps <= this.fps && fps >= 0) {
//                System.out.println("fps: " + fps);
//                System.out.println("timediff: " + timeDif);
                update();
                repaint();
                previousTime = System.currentTimeMillis();
            }

//            Scanner scanner = new Scanner(System.in);
//            scanner.next();


        }
    }

    public void update() {
//        if(keyH.upPressed) {
//            playerY -= playerSpeed;
//        }
//        else if(keyH.downPressed) {
//            playerY += playerSpeed;
//        }
//        else if(keyH.leftPressed) {
//            playerX -= playerSpeed;
//        }
//        else if(keyH.rightPressed) {
//            playerX += playerSpeed;
//        }
    }

    public void paintComponent(Graphics g) {


        super.paintComponent(g);

//        drawMapperRoads(g);
//        drawMapperGradually(g);
//        drawWithDirection(g);
//        draw(g);
        if(!mazeDone) {
            if(drawGradually(g)) {
                fps = 15;
                mazeDone = true;
            }
        } else {
            drawMapperGradually(g);
        }
//        drawWalls(g);
//        drawDirectionTiles(g);
//        if(drawSwitch) {
//            draw(g);
//        } else {
//            drawWalls(g);
//        }
//
//        drawSwitch = !drawSwitch;
    }

    public void drawFromList(List<Integer> rectList, Graphics2D g2) {
        g2.fillRect(rectList.get(0), rectList.get(1), rectList.get(2), rectList.get(3));
    }

    private boolean drawGradually(Graphics g) {

        boolean done;

        Graphics2D g2 = (Graphics2D)this.drawing.getGraphics();

        g2.setColor(Color.white);
//        for(int i = 0; i<columns; i++) {
//            for(int k = 0; k<rows; k++) {
//                drawFromList(drawMaze.getCube(Arrays.asList(i, k)), g2);
//            }
//        }
//
//        for(int[][] wall : maze) {
//            drawFromList(drawMaze.getWall(wall[0], wall[1]), g2);
//        }
        if(drawCounter < this.maze.size()) {
            if (drawCounter == -1) {
                drawFromList(drawMaze.getCube(this.maze.get(0)[0]), g2);
            } else {
                int[][] wall = maze.get(drawCounter);
                drawFromList(drawMaze.getCube(wall[1]), g2);
                drawFromList(drawMaze.getWall(wall[0], wall[1]), g2);
            }
//        drawFromList(drawMaze.getCube(Arrays.asList(1, 1)), g2);
//        drawFromList(drawMaze.getCube(Arrays.asList(2, 1)), g2);
//        drawFromList(drawMaze.getWall(Arrays.asList(1, 1), Arrays.asList(2, 1)), g2);
            drawCounter += 1;
            done = false;
        } else {
            done = true;
        }
        g.drawImage(drawing, 0, 0, null);
        g2.dispose();

        return done;
    }

    private void draw(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);
        for(int i = 0; i<columns; i++) {
            for(int k = 0; k<rows; k++) {
                drawFromList(drawMaze.getCube(Arrays.asList(i, k)), g2);
            }
        }

        for(int[][] wall : maze) {
            drawFromList(drawMaze.getWall(wall[0], wall[1]), g2);
        }
//        drawFromList(drawMaze.getCube(Arrays.asList(1, 1)), g2);
//        drawFromList(drawMaze.getCube(Arrays.asList(2, 1)), g2);
//        drawFromList(drawMaze.getWall(Arrays.asList(1, 1), Arrays.asList(2, 1)), g2);
        g2.dispose();
    }

    private void drawWalls(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);
        for(int i = 0; i<columns; i++) {
            for(int k = 0; k<rows; k++) {
                drawFromList(drawMaze.getCube(Arrays.asList(i, k)), g2);
            }
        }

        for(int orientIndex = 0; orientIndex < 2; orientIndex++) {
            boolean[][] orientation = mazeWalls[orientIndex];

            for(int lineIndex = 0; lineIndex < orientation.length; lineIndex++) {
                boolean[] line = orientation[lineIndex];

                for(int wallIndex = 0; wallIndex < line.length; wallIndex++) {
                    boolean wall = line[wallIndex];

                    if(wall) {
                        drawFromList(drawMaze.getWall(new int[]{orientIndex, lineIndex, wallIndex}), g2);
                    }
                }
            }
        }
//        drawFromList(drawMaze.getCube(Arrays.asList(1, 1)), g2);
//        drawFromList(drawMaze.getCube(Arrays.asList(2, 1)), g2);
//        drawFromList(drawMaze.getWall(Arrays.asList(1, 1), Arrays.asList(2, 1)), g2);
        g2.dispose();
    }

    private void drawDirectionTiles(Graphics g) {
        for(int i = 0; i < 15; i++) {
            g.drawImage(drawMaze.getDirectionTile(i),
                    i*(drawMaze.tileSize + drawMaze.wallSize) + drawMaze.wallSize, drawMaze.wallSize, null);
        }
    }

    private void drawWithDirection(Graphics g) {

        Graphics2D g2 = (Graphics2D)g;

        g2.setColor(Color.white);

        for(int i = 0; i<columns; i++) {
            for(int k = 0; k<rows; k++) {
                int[] coordinates = new int[]{i, k};
                boolean[] walls = mazeGenerator.getWallsPoint(coordinates);
                int tileIndex = BitConverter.convertToTileInt(walls);
                drawMaze.drawDirectionTile(coordinates, g, tileIndex);
            }
        }

        for(int[][] wall : maze) {
            drawMaze.drawDirectionWall(wall[0], wall[1], g);
        }
//        drawFromList(drawMaze.getCube(Arrays.asList(1, 1)), g2);
//        drawFromList(drawMaze.getCube(Arrays.asList(2, 1)), g2);
//        drawFromList(drawMaze.getWall(Arrays.asList(1, 1), Arrays.asList(2, 1)), g2);
        g2.dispose();
    }

    private void drawMapperRoads(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        MazeMapper mazeMapper = new MazeMapper(mazeGenerator);
        mazeMapper.mapMaze(new int[]{0, 0});

        g2.setColor(Color.white);

        for(List<int[]> road : mazeMapper.roads) {
            int roadSize = road.size();
            for(int i = 0; i < roadSize; i++) {
                int[] pointA = road.get(i);
                boolean[] walls = mazeGenerator.getWallsPoint(pointA);
                int tileIndex = BitConverter.convertToTileInt(walls);
                drawMaze.drawDirectionTile(pointA, g, tileIndex);
                if(i < (roadSize - 1)) {
                    int[] pointB = road.get(i+1);
                    drawMaze.drawDirectionWall(pointA, pointB, g);
                }
            }
        }

//        drawFromList(drawMaze.getCube(Arrays.asList(1, 1)), g2);
//        drawFromList(drawMaze.getCube(Arrays.asList(2, 1)), g2);
//        drawFromList(drawMaze.getWall(Arrays.asList(1, 1), Arrays.asList(2, 1)), g2);
        g2.dispose();
    }

    private void drawMapperIntersections(Graphics g) {
        Graphics2D g2 = (Graphics2D)g;

        MazeMapper mazeMapper = new MazeMapper(mazeGenerator);
        mazeMapper.mapMaze(new int[]{0, 0});

        g2.setColor(Color.white);

        for(int[] intersection : mazeMapper.intersections) {

            boolean[] walls = mazeGenerator.getWallsPoint(intersection);
            int tileIndex = BitConverter.convertToTileInt(walls);
            drawMaze.drawDirectionTile(intersection, g, tileIndex);
        }

//        drawFromList(drawMaze.getCube(Arrays.asList(1, 1)), g2);
//        drawFromList(drawMaze.getCube(Arrays.asList(2, 1)), g2);
//        drawFromList(drawMaze.getWall(Arrays.asList(1, 1), Arrays.asList(2, 1)), g2);
        g2.dispose();
    }

    private void drawMapperGradually(Graphics g) {

        Graphics g2 = drawing.getGraphics();

        MazeMapper mazeMapper = new MazeMapper(mazeGenerator);
        mazeMapper.mapMaze(new int[]{0, 0});


        if(intersectionCounter < mazeMapper.intersections.size()) {
            int[] startPoint = mazeMapper.intersections.get(intersectionCounter);


            if(connectionCounter < mazeMapper.connections.get(intersectionCounter).size()){
                boolean[] walls;
                int tileIndex;


                int[] connection = mazeMapper.connections.get(intersectionCounter).get(connectionCounter);
                int[] endPoint = mazeMapper.intersections.get(connection[1]);
                List<int[]> road = mazeMapper.roads.get(connection[2]);
                int roadSize = road.size();

                if(intersectionCounter == 0 && connectionCounter == 0) {
                    walls = mazeGenerator.getWallsPoint(startPoint);
                    tileIndex = BitConverter.convertToTileInt(walls);
                    drawMaze.drawDirectionTile(startPoint, g2, tileIndex);
                }

                walls = mazeGenerator.getWallsPoint(endPoint);
                tileIndex = BitConverter.convertToTileInt(walls);
                drawMaze.drawDirectionTile(endPoint, g2, tileIndex);


                if(roadSize == 0) {
                    drawMaze.drawDirectionWall(endPoint, startPoint, g2);
                }


                for(int i = 0; i < roadSize; i++) {
                    int[] roadTile = road.get(i);
                    if(i == 0) {
                        drawMaze.drawDirectionWall(startPoint, roadTile, g2);
                    }
                    if(i == (roadSize - 1)) {
                        drawMaze.drawDirectionWall(endPoint, roadTile, g2);
                    }
                    if(i != (roadSize - 1)) {
                        drawMaze.drawDirectionWall(road.get(i+1), roadTile, g2);
                    }
                    walls = mazeGenerator.getWallsPoint(roadTile);
                    tileIndex = BitConverter.convertToTileInt(walls);
                    drawMaze.drawDirectionTile(roadTile, g2, tileIndex);
                }

                connectionCounter += 1;
            } else {
                intersectionCounter += 1;
                connectionCounter = 1;
            }
        }

        g.drawImage(drawing, 0, 0, null);
        g2.dispose();
    }
}
