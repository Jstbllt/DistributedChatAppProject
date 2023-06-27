import java.lang.reflect.Array;
import java.time.Clock;
import java.util.HashMap;
import java.util.Map;

public class VectorClock {
    private int[] clock;

    public VectorClock() {
        this.clock = new int[10];
        for(int i = 0 ; i <= clock.length - 1 ; i++)
            this.clock[i] = 0;
    }

    public void increment(int nodeId) {
        this.clock[nodeId-1]++;
    }

    public void update(VectorClock other) {
        for(int i = 0 ; i <= clock.length - 1 ; i++)
            //If other is bigger than me, I take his value
            if(other.clock[i] > this.clock[i])
                this.clock[i] = other.clock[i];
    }

    public VectorClock copy() {
        VectorClock copy = new VectorClock();
        copy.clock = clock;
        return copy;
    }

    public int[] getClock() {
        return clock;
    }

    public void setClock(int index, int value){
        clock[index] = value;
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
