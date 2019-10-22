package com.daveace.salesdiary;

import java.util.Objects;

public class SummaryItem<T> {
    private T t;

    private SummaryItem(T t) {
        this.t = t;
    }
    
    public static<T> SummaryItem getInstance(T t){
        return new SummaryItem(t);
    }
    
    private void set(T t){
        this.t = t;
    }
    
    public T get(){
        return this.t;
    }

    @Override
    public String toString() {
        return "SummaryItem{" +
                "t=" + t +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SummaryItem)) return false;
        SummaryItem<?> that = (SummaryItem<?>) o;
        return Objects.equals(t, that.t);
    }

    @Override
    public int hashCode() {
        return Objects.hash(t);
    }

}
