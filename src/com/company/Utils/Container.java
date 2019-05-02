package com.company.Utils;

import java.util.ArrayList;
import java.util.Iterator;

public class Container<K> {
    protected ArrayList<ArrayList<K>> list;  //Tabellenblat
    protected ArrayList<K> newElements;      //"ZwischenSpeicher"

    public Container() {
        list = new ArrayList<>();
        newElements = new ArrayList<>();
    }

    //Anzahl der Zeilen / Einträge / Tupeln
    public int size() {
        int result = 0;

        Iterator<ArrayList<K>> iterator = list.iterator();
        while(iterator.hasNext()) {
            iterator.next();
            result++;
        }

        return result;
    }

    //fügt ein neues Element zur kleinen Liste dazu.
    public boolean add(K newElement) {
        if(newElement == null) {
            return false;
        }

        if(smallContains(newElement)) {
            return false;
            //element already exists
        }

        newElements.add(newElement);

        return true;
    }


    private boolean smallContains(K check) {
        if(check == null)
            return false;

        return newElements.contains(check);
    }


    public void wrapTogether() {
        list.add(newElements);
        newElements = new ArrayList<>();
    }

    public boolean contains(Object check) {
        if(check == null)
            return false;

        return list.contains(check);
    }

    private String toStringHelper(ArrayList<K> AL) {
        if(AL == null)
            throw new NullPointerException("Invalid ArrayList!");

        String result = "";
        Iterator<K> iterator = AL.iterator();
        while(iterator.hasNext()) {
            K next = iterator.next();
            if(next instanceof ArrayList) {
                result += toStringHelper((ArrayList)next);
            }
            result += next.toString();
        }

        return result;
    }

    @Override
    public String toString() {
        if(this.list == null)
            return "";

        Iterator<ArrayList<K>> iterator = list.iterator();
        String result = "";

        /*
        while(iterator.hasNext()) {
            ArrayList<K> next = iterator.next();
            result += toStringHelper(next);
        }*/

        return list.toString();
    }

    /**
     * @param i
     * @return
     */
    public ArrayList<K> get(int i) {
        if(i > size()) {
            return null;
        }

        return(list.get(i));
    }




    /**
     * This method is only for Containers of Value!
     * @param bezeichnung - the criteria you are searching for.
     * @return
     */
    public ArrayList<Value> get(String bezeichnung) {
        Iterator<ArrayList<K>> iterator = list.iterator();
        ArrayList<Value> v = new ArrayList<>();

        while(iterator.hasNext()) {
            ArrayList<K> next = iterator.next();
            Iterator<K> kIterator = next.iterator();
            while(kIterator.hasNext()) {
                K elem = kIterator.next();
                if(elem instanceof Value) {
                    Value val = (Value) elem;
                    String bez = val.getBez();
                    if(bez.equals(bezeichnung)) {
                        v.add(val);
                    }
                }
            }
        }
        return v;
    }









}
