import java.io.Serializable;

public class Pair<F, S> implements Serializable {
    private F first;
    private S second;

    public Pair(F first, S second) {
        this.first = first;
        this.second = second;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public boolean equals(Object other) {
        if (other instanceof Pair) {
            Pair<F, S> that = (Pair<F, S>) other;
            return first.equals(that.first) && second.equals(that.second);
        }

        return false;
    }

    public int hashCode() {
        return first.hashCode() + second.hashCode() * second.hashCode();
    }

    public String toString() {
        return "[" + first + ", " + second + "]";
    }
}
