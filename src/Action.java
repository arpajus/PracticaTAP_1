import java.util.Arrays;

public abstract class Action {
    // nose si aparte de id tiene que haber un codigo, no tengo claro si id y codigo
    // son diferentes
    private String id;
    private double memory; // memory -> MB
    protected int values[];
    private int result;

    public Action(String id, double memory, int[] values) {
        this.id = id;
        this.memory = memory;
        this.values = values;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getMemory() {
        return memory;
    }

    public void setMemory(double memory) {
        this.memory = memory;
    }

    public int[] getValues() {
        return values;
    }

    public void setValues(int[] values) {
        this.values = values;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return "Action: [id=" + id + ", memory=" + memory + ", values=" + Arrays.toString(values) + "]";
    }

    public abstract void operation();
}