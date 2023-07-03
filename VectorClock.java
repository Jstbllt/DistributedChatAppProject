import java.lang.reflect.Array;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

public class VectorClock {
    private int[] clock;

    public VectorClock(int n) {
        this.clock = new int[n];
        for(int i = 0 ; i <= clock.length - 1 ; i++)
            this.clock[i] = 0;
    }

    // Getter and setter
    public int[] getClock() {
        return clock;
    }
    public void setClock(int index, int value){
        clock[index] = value;
    }

    // Permits to increment a clock
    public void increment(int nodeId) {
        this.clock[nodeId-1]++;
    }

    public VectorClock copy() {
        VectorClock copy = new VectorClock(clock.length);
        for(int i = 0 ; i < copy.clock.length ; i++){
            copy.clock[i] = clock[i];
        }
        return copy;
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        result.append("VectorClock [ ");

        for(int nodeClock : clock)
        {
            result.append(nodeClock + " ");
        }

        result.append("]");
        return result.toString();
    }
}
