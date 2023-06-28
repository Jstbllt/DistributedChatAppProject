import java.lang.reflect.Array;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

public class MatrixClock {
    private int[][] clock;

    public MatrixClock(int n) {
        this.clock = new int[n][n];
        for(int i = 0 ; i < n ; i++)
            this.clock[i][i] = 0;
    }

    public void increment(int i, int j) {
        this.clock[i-1][j-1]++;
    }

    public int[][] getClock() {
        return clock;
    }

    public void setClock(int i, int j, int value){
        clock[i][j] = value;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("\n=> MatrixClock\n");

        for(int i = 0 ; i < clock.length ; i++)
        {
            for(int j = 0 ; j < clock.length ; j++){
                result.append(clock[i][j] + " ");
            }
            result.append("\n");
        }
        return result.toString();
    }
}
