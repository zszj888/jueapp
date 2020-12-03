package com.somecom;

import java.util.concurrent.TimeUnit;

public class ThreadId {
    // Atomic integer containing the next thread ID to be assigned
    // Thread local variable containing each thread's ID
    private static final ThreadLocal<Integer> threadId = new ThreadLocal<>();

    // Returns the current thread's unique ID, assigning it if necessary
    public static void main(String[] args) {
        for (int i = 0; i < 20; i++) {
            int finalI = i;
            new Thread(() -> {
                threadId.set(finalI);
                try {
                    TimeUnit.MILLISECONDS.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(threadId.get());
            }).start();
        }
    }
}