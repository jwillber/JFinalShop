package shengxiangou;

import java.util.Set;
import java.util.TreeSet;

public class Test {

    public static void main(String[] args) {
        // TODO Auto-generated method stub
        Set<String> a = new TreeSet<>();    
        a.add("a[0][2]");
        a.add("a[0][1]");
        a.add("a[1][0");
        for(String key : a){
            System.out.println(key);
        }
    }

}
