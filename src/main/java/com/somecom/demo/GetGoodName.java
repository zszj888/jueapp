package com.somecom.demo;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

public class GetGoodName {
    private static final String ZH = "张中颢";
    private static final String YQ = "张云奇";
    static final ArrayList<String> containerOne = new ArrayList<>();
    static final ArrayList<String> containerTwo = new ArrayList<>();

    public static void main(String[] args) throws Exception {
        IntStream.range(1, 100).forEach(value -> {
            String s = String.valueOf((int) (Math.random() * (int) (Math.random() * 1000000)) ^
                    (int) (Math.random() * (int) (Math.random() * 1000000)));
            int i = (int) (Math.random() * s.length());
            boolean b = (Integer.valueOf(s.charAt(i)) & 1) == 0;
            System.out.println(s + ":" + i + "->" + s.charAt(i) + ", " + (b ? "偶数" : "奇数"));
            if (b) {
                containerOne.add(YQ);
            } else {
                containerTwo.add(ZH);
            }
            try {
                TimeUnit.MILLISECONDS.sleep((int)(Math.random()*2));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        System.out.println(YQ + "得票：" + containerOne.size());
        System.out.println(ZH + "得票：" + containerTwo.size());
    }
}
