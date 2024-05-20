package dev.tehin.tlib.api.configuration.holders;

import lombok.Getter;

@Getter
public class TasksConfig {

    private int corePoolSize = 1, maximumPoolSize = 3;
    private long threadKeepAliveInMs = 60000;

    /**
     * The core pool size for the async operations.
     * <br>
     * Default Value: 1
     */
    public TasksConfig corePoolSize(int size) {
        this.corePoolSize = size;
        return this;
    }

    /**
     * The maximum size the pool can grow to
     * <br>
     * Default Value: 3
     */
    public TasksConfig maximumPoolSize(int size) {
        this.maximumPoolSize = size;
        return this;
    }

    /**
     * The created threads keep alive in milliseconds
     * <br>
     * Default Value: 60000
     */
    public TasksConfig threadKeepAliveInMs(long keepAlive) {
        this.threadKeepAliveInMs = keepAlive;
        return this;
    }
}
