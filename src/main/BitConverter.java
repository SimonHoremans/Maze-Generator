package main;

public class BitConverter {
    public static boolean[] convertToBool(int number) {
        boolean[] bits = new boolean[4];

        for(int i = 0; i < 4; i++) {
            bits[i] = (number & (1 << i)) != 0;
        }
        return bits;
    }

    public static int convertToInt(boolean[] bits) {
        int number = 0;
        for(int i = 0; i < bits.length; i++) {
            if(bits[i]) {
                number += Math.pow(2, i);
            }
        }

        return number;
    }

    public static int convertToTileInt(boolean[] bits) {
        bits = invertBool(bits);
        return convertToInt(bits);
    }

    public static boolean[] invertBool(boolean[] bits) {
        boolean[] newBits = new boolean[bits.length];
        for(int i = 0; i < bits.length; i++) {
            newBits[i] = !bits[i];
        }
        return newBits;
    }

    public static int numberOfTrue (boolean[] bits) {
        int number = 0;
        for(boolean bit : bits) {
            if(bit) {
                number += 1;
            }
        }
        return number;
    }

    public static int getDirection(boolean[] bits) {
        int direction = 0;
        for(int i = 0; i < 4; i++) {
            if(bits[i]){
                direction = i;
                break;
            }
        }

        return direction;
    }

    public static int insertNumber(int number, int[] array) {
        boolean done = false;
        int startIndex = 0;
        int endIndex = array.length;
        int insertIndex = 0;

        if(array.length%2 != 0) {
            if(number <= array[0]) {
                done = true;
            } else {
                startIndex = 1;
            }
        }

        if(!done) {
            boolean small = false;
            boolean great = false;
            int currentIndex = 0;

            int counter = 0;

            while(!small || !great) {
//                if (counter > 10) {
//                    break;
//                }
                if(!small & !great) {
                    currentIndex = startIndex + ((array.length - startIndex)/2);
                } else if(!small & great) {
                    startIndex = currentIndex;
                    currentIndex = currentIndex + ((endIndex - currentIndex)/2);
                } else if(small & !great) {
                    endIndex = currentIndex;
                    currentIndex = startIndex + ((currentIndex - startIndex)/2);
                }

                small = number <= array[currentIndex];
                great = number >= array[currentIndex - 1];
                System.out.println(currentIndex);
                System.out.println(array[currentIndex]);
                System.out.println(array[currentIndex - 1]);
                System.out.println();
                counter += 1;
            }
            insertIndex = currentIndex;
        }

        return insertIndex;

    }
}
