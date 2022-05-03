package main;


import com.google.gson.Gson;
import main.Minecraft.DrawMazeMinecraft;
import main.Minecraft.TileWall;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
//        testConversion();
//        testModulus();
        gameLoop();
//        insertTest();
//        serializeTest();
//        mazeTest();
//        scannerTest();
//        bitTest();
//        listTest();
//        intTest();
//        minecraftMazeTest();
    }

    public static void gameLoop() {
        JFrame window = new JFrame();
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.setResizable(false);
        window.setTitle("2DTest");

        GamePanel gamePanel = new GamePanel();
        window.add(gamePanel);
        window.pack(); //resize window to panelsize

        window.setLocationRelativeTo(null);
        window.setVisible(true);

        gamePanel.startGameThread();
    }
    public static void testConversion() {
        List<Integer> newCords = DrawMaze.convertToDisplay(Arrays.asList(1, 1));
        System.out.println(newCords);

    }
    public static void testModulus() {
        System.out.println();
    }
    public static void serializeTest() {
        List<Integer> list = Arrays.asList(0, 0, 0);
        String listString = list.toString();
        System.out.println(list.toString());
    }
    public static void mazeTest() {
        MazeGenerator mazeGenerator = new MazeGenerator(125, 80, new int[]{14, 0});
//        System.out.println(Arrays.deepToString(mazeGenerator.getMazeArray()));
//        System.out.println(mazeGenerator.getMazeArray().length);
        mazeGenerator.generateMaze();
//        System.out.println(MazeGenerator.nobspls(mazeGenerator.getMaze()));
        mazeGenerator.generateWalls();
        MazeMapper mazeMapper = new MazeMapper(mazeGenerator);
        mazeMapper.mapMaze(new int[]{0, 0});
        System.out.println(mazeMapper.getSize());
//        System.out.println(Arrays.deepToString(mazeGenerator.getMazeWalls()));
    }
    public static void minecraftMazeTest() {
        MazeGenerator mazeGenerator = new MazeGenerator(15, 15, new int[]{0, 0});
        mazeGenerator.generateMaze();
        for(int[][] step : mazeGenerator.maze) {
            TileWall tileWall = DrawMazeMinecraft.getTileWall(step[0], step[1]);
            Gson gson = new Gson();
            String json = gson.toJson(tileWall);
            System.out.println(json);
        }
    }
    public static void scannerTest() {
        Scanner scanner = new Scanner(System.in);
        String line = scanner.next();
    }
    public static void bitTest() {
        boolean[] bits = new boolean[] {
                true,
                true,
                false,
                false
        };

        int tileIndex = BitConverter.convertToTileInt(bits);

        System.out.println(tileIndex);

    }
    public static void listTest() {
        List<Integer> listThingy = new ArrayList<>(Arrays.asList(1, 2, 3, 4));
        ArrayListTest.addToList(listThingy);
        System.out.println(listThingy);
    }
    public static void insertTest() {
        int[] numbers = new int[1000000001];
        for(int i = 0; i < numbers.length; i++) {
            numbers[i] = i*2;
        }

        int insertNumber = BitConverter.insertNumber(74383987, numbers);

        System.out.println(insertNumber);
    }
    public static void intTest() {
        int number = (int)1.7;
        System.out.println(number);
    }
}
