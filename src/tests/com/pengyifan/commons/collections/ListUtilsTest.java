package tests.com.pengyifan.commons.collections;

import java.util.ArrayList;
import java.util.List;

import com.pengyifan.commons.collections.ListUtils;


public class ListUtilsTest {
  public static void main(String args[]) {
    List<Integer> list = new ArrayList<Integer>();
    int is[] = new int[] {90,50,20,80,70,30,10,60,40};
    for(int i: is) {
      list.add(i);
    }
    
    System.out.println(list);
    List<Integer> sublist = ListUtils.longestIncreasingSubsequence(list);
    System.out.println(sublist);
  }
}
