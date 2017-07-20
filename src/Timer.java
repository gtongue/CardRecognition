/**
 * Created by Garrett Tongue on 7/17/2017.
 */
public class Timer {
    public static long lastTime = 0;
    public static void startTimer()
    {
        lastTime = System.nanoTime();
    }

    public static void endTimer()
    {
        long currentTime = System.nanoTime();
        System.out.println((currentTime - lastTime)/(Math.pow(10,9)));
    }
}
