package pk.game.count;

public class Counter {
    private int count;

    public Counter() {
        this.count = 0;
    }

    /**
     *
     * @return The {@link Counter#count} so far
     */
    public int getCount() {
        return this.count;
    }

    /**
     *
     * @param count The new count value
     */
    public void setCount(int count) {
        this.count = count;
    }

    /**
     *
     * @param count The number to add to the current count
     */
    public void add(int count) {
        this.setCount(this.getCount()+count);
    }

    /**
     *
     * @param count The number to subtract from the current count
     */
    public void subtract(int count) {
        this.add(-count);
    }

    /**
     * Reset the counter back to 0
     */
    public void reset() {
        this.setCount(0);
    }
}
