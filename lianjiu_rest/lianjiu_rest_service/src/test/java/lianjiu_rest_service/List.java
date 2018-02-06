package lianjiu_rest_service;

import java.util.ArrayList;

public class List {
    public static void main(String[] args) {
        java.util.List<String> list = new ArrayList<String>();
        
        list.add("a");
        list.add("b");
        list.add("c");
        String a = "" ;
        for (int i = 0; i < list.size(); i++) {
            a = list.get(i) + a;
        }
        System.out.println(new StringBuffer(a).reverse().toString());
    }
}