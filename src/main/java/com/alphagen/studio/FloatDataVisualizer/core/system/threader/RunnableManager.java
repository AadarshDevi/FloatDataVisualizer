package com.alphagen.studio.FloatDataVisualizer.core.system.threader;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

// todo create a ConnectionManager that can do more things
public class RunnableManager {

    // logger
    private static final Logger logger = LogManager.getLogger(RunnableManager.class);

    // The actual thread used
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    // runnable process
    private final Runnable runnable;

    // thing to operate the thread
    private Future<?> future;

    public RunnableManager(Runnable runnable) {
        this.runnable = runnable;
    }

    // start running the reader
    public synchronized void start() {
        if (isRunning()) return;
        future = executorService.submit(runnable);
    }

    public synchronized boolean isRunning() {
        return future != null && !future.isDone();
    }

    // stop execution
    public synchronized void stop() {
        if (future == null) return;
        future.cancel(true);
    }

    // destroy thread and stop all execution
    public void shutdown() {
        stop();
        executorService.shutdownNow();
    }
}
