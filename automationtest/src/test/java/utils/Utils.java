package utils;

import java.util.concurrent.ThreadLocalRandom;

public class Utils {

    public static int randomEmail(int min, int max){
        int randomNum = ThreadLocalRandom.current().nextInt(min, max + 1);
        return randomNum;
    }
}
