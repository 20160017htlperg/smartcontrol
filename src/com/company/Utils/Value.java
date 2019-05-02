package com.company.Utils;

import java.util.Date;

public class Value<K> {
    private K val;
    private String bez;

    public Value(K value, String bezeichnung) {
        this.val = value;
        this.bez = bezeichnung;
    }

    public K getValue() {
        return(val);
    }

    public String getBez() { return bez; }

    @Override
    public boolean equals(Object obj) {
        if(obj == null) {
            return false;
        }
        if(!(obj instanceof Value)) {
            return false;
        }
        //to handle the object easier we cast it to a value.
        Value temp = (Value) obj;

        if(temp.val.equals(this.val) && temp.bez.equals(this.bez)) {
            return(true);
        }

        return false;
    }

    @Override
    public String toString() {
        String result = "";
        result += "\"" + this.bez + "\" : ";

        if(val instanceof Number) {
            result += val.toString() +"";
        }

        if(val instanceof Boolean) {
            result += val.toString() +"";
        }

        if(val instanceof String) {
            result += "\"" + val + "\"";
        }

        if(val instanceof Date) {
            result += "\"" + val.toString() + "\"";
        }

        return(result);
    }
}