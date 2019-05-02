package com.company.Utils;

import java.util.*;

public class Utils {
    public static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static String callJSON(ArrayList<HashMap<String, String>> hm) {
        String result = "[";
        /**/
        Iterator<HashMap<String, String>> iterator = hm.iterator();

        while(iterator.hasNext()) {
            result += "{" + makeJSON(iterator.next()) + "}";
            if(iterator.hasNext()) {
                result += ",";
            }
        }
        result+="]";
        return(result);
    }

    private static String makeJSON(HashMap<String, String> hm) {
        String data = "";
        Set<String> keys = hm.keySet();             //keynames
        Collection<String> values = hm.values();    //values
        Iterator<String> iter1 = keys.iterator();
        Iterator<String> iter2 = values.iterator();

        while(iter1.hasNext() && iter2.hasNext()) {
            data = String.format("\"%s\" : \"%s\"",iter1.next().toString(), iter2.next().toString());
        }

        return(data);
    }
}
