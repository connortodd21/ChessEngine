import java.security.SecureRandom;

public class RandomNumber {

    public static long randomMilliseconds(){
        return System.nanoTime();
    }

    public static long secureRandom(){
        return new SecureRandom().nextLong();
    }

    public static long badRandom(){
        return (long)(Math.random()*1000000000000000000L);
    }

    public static void testDistribution() {
        int sampleSize = 2000;
        int sampleSeconds = 10;
        long startTime = System.currentTimeMillis();
        long endTime = startTime + (sampleSeconds * 1000);
        int[] distArray;
        distArray = new int[sampleSize];
        while (System.currentTimeMillis() < endTime)
        {
            for (int i = 0; i < 10000; i++)
            {
                distArray[(int)((secureRandom() % (sampleSize/2)) + sampleSize/2)]++;
//                distArray[(int)(randomMilliseconds() % sampleSize)]++;
            }
        }
        for (int i = 0; i < sampleSize; i++)
        {
            System.out.println(distArray[i]);
        }
    }

}
