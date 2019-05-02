package com.company.Utils;

import java.util.ArrayList;
import java.util.Iterator;

public class JsonDataContainer<K> extends Container {
    private String toStringHelper(ArrayList<K> AL) {
        if(AL == null)
            throw new NullPointerException("Invalid ArrayList!");

        StringBuilder result = new StringBuilder();
        result.append('{');
        Iterator<K> iterator = AL.iterator();
        while(iterator.hasNext()) {
            K next = iterator.next();
            if(next instanceof ArrayList) {
                result.append(toStringHelper((ArrayList)next));
            }
            result.append(next.toString());
            if(iterator.hasNext()) {
                result.append(',');
            }else{
                result.append('}');
            }
        }

        return result.toString();
    }

    @Override
    public String toString() {
        if(this.list == null)
            return "";

        Iterator<ArrayList<K>> iterator = list.iterator();
        StringBuilder result = new StringBuilder();
        result.append('[');
        while(iterator.hasNext()) {
            ArrayList<K> next = iterator.next();
            result.append(toStringHelper(next));
            if(iterator.hasNext()) {
                result.append(',');
            }
        }
        result.append(']');
        return result.toString();
    }
}
