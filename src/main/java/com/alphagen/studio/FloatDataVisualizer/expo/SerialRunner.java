package com.alphagen.studio.FloatDataVisualizer.expo;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.function.Consumer;

public class SerialRunner {

    private static final Logger logger = LogManager.getLogger(SerialRunner.class);
    private static SerialRunner instance;
    // The actual thread used
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();

    // what to do with the data collected
    private final Consumer<TestData> consumer;

    // turning on and off, the switch
    public volatile boolean running = false;

    // thing to operate the thread
    private Future<?> future;

    public SerialRunner(Consumer<TestData> consumer) {
        this.consumer = consumer;
    }

    // start running the reader
    public synchronized void start() {
        if (running) return;
        running = true;
        future = executorService.submit(this::loop);
    }

    // stop execution
    public synchronized void stop() {
        running = false;
        if (future != null) {
            future.cancel(true);
        }
    }

    // destroy thread and stop all execution
    public void shutdown() {
        stop();
        executorService.shutdownNow();
    }

    private void loop() {

        while (running) {
            TestData td = new TestData(Math.random() * 100.0, Math.random() * 100.0);
            consumer.accept(td);
            logger.info("TestData: " + td);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                return; // stop() activates
            }
        }

    }
}
