package me.texels.mySqlOrm.utils;

public class Pair<F, S> {
    public F first;
    public S second;

    public Pair(F first, S second ){
        this.first=first;
        this.second=second;
    }

    @Override
    public String toString() {
        return "Pair("+first.toString()+","+second.toString()+")";
    }

    @Override
    public int hashCode() {
        return (first == null? 0 : first.hashCode()) ^ (second == null? 0 : second.hashCode());
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Pair)) return false;

        return ((Pair<?, ?>) obj).second == this.second && ((Pair<?, ?>) obj).first == this.first;
    }

    public static <F,S> Pair<F,S> of(F first, S second){
        return new Pair<>(first,second);
    }
}
