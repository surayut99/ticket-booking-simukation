package theatre;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

public class TestClass {
    public static void main(String[] args) {
        String s = "    position,A1,A2,A3,A4,A5";
//        System.out.println(s.contains("schedule"));
        List<String> positions = new ArrayList<>(Arrays.asList(s.split(",")));
        positions.remove(0);
        System.out.println(positions);
    }
}
