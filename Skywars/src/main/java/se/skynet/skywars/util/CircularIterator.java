package se.skynet.skywars.util;

import java.util.List;

public class CircularIterator<T> {

    private final List<T> list;
    private int index;

    public CircularIterator(List<T> list) {
        this.list = list;
        index = 0;
    }

    public T next(){
        if(index >= list.size()){
            index = 0;
        }
        T element = list.get(index);
        index++;
        return element;
    }

    public void remove(int index) {
        this.list.remove(index);
    }

}
