package com.github.imdmk.doublejump.scheduler;

public interface TaskScheduler {

    void runAsync(Runnable runnable);

    void runLater(Runnable runnable, long delay);

    void runLaterAsync(Runnable runnable, long delay);

    void runTimerAsync(Runnable runnable, long delay, long period);
}
