package util;

public class Tuple<A, B> {
    private A first;
    private B second;

    public Tuple() {
    	first = null;
    	second = null;
    }
    
    public Tuple(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A getFirst() {
        return first;
    }

    public B getSecond() {
        return second;
    }
    
    public void setFirst(A f) {
    	first = f;
    }
    
    public void setSecond(B s) {
    	second = s;
    }
    
}

