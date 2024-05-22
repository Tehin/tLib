package dev.tehin.tlib.utilities.task;

import dev.tehin.tlib.api.tLib;
import lombok.Getter;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

@Getter
public class TaskTimer {

    public static Consumer<TaskTimer> EMPTY_CONSUMER = (t) -> {};
    public static Runnable EMPTY_RUNNABLE = () -> {};

    private HashMap<String, String> data;
    private boolean ended = false; // Prevent double end calls

    private final Consumer<TaskTimer> task;
    private final double duration, increment;
    private final Runnable end;
    private final int delay;
    private double counter;
    private ScheduledThreadPoolExecutor executor;
    private BukkitTask bukkitReference;
    private boolean sync = false;

    private int ticks = 0;

    /**
     * A class that will repeat itself until a goal is reached
     * <br/>
     * The counter or ticks will NEVER be 0, the value will be INCREMENTED before the first call
     * <br/>
     * The final tick or counter WILL BE the same as the duration (Last tick on 20 ticks would be 20)
     *
     * @param task The consumer function that will be called each defined tick
     * @param end What function should be called when the timer ends
     * @param duration If ticks enabled, for how many ticks will this last, if not, defines the max of the counter
     * @param increment How much should the counter increment each time
     * @param delay What should the delay be in milliseconds (this is the defined tick)
     * @param ticks If the duration will be based on ticks, otherwise it will be based by the count max value
     */
    public TaskTimer(Consumer<TaskTimer> task, Runnable end, double duration, double increment, int delay, boolean ticks) {
        this.task = task == null ? EMPTY_CONSUMER : task;
        this.end = end == null ? EMPTY_RUNNABLE : end;
        this.duration = duration;
        this.increment = increment;
        this.delay = delay;

        if (!ticks) this.ticks = -1;
    }

    /**
     * Schedules the task to be async.
     * <br/>
     * Be aware of the thread-safety if working with Bukkit.
     */
    public void runAsync() {
        this.executor = new ScheduledThreadPoolExecutor(1);

        executor
                .scheduleAtFixedRate(this::play, 0, getDelay(), TimeUnit.MILLISECONDS);
    }

    /**
     * Schedules the task to be sync, meaning it will run in the server thread.
     * <br/>
     * Be aware of tick overloads, if that is the case, increase the ticks so
     * the job takes less tick time.
     */
    public void runSync(Plugin plugin) {
        sync = true;

        bukkitReference = plugin.getServer().getScheduler()
                .runTaskTimer(plugin, this::play, 0, delay / 50);
    }

    private void play() {
        incrementAndCheck();
        getTask().accept(this);
    }

    private void incrementAndCheck() {
        if (ticks != -1) this.ticks++;
        this.counter += getIncrement();

        if (!isFinalExecution()) return;
        stop();
    }

    /**
     * Instantly stop and end the execution of the timer.
     * <br/>
     * Calls the end {@link Runnable}
     */
    public void stop() {
        if (ended) return;

        ended = true;

        // If it's sync, this will be called inside the BukkitTask anyway. If is not sync, it will execute in the caller thread.
        getEnd().run();

        if (this.data != null) {
            this.data.clear();
        }

        if (sync) {
            this.bukkitReference.cancel();
            return;
        }

        // If the task is not sync, cancel the executor.
        this.executor.shutdownNow();
    }

    /**
     * Checks if this is the last time the execution will be held
     */
    public boolean isFinalExecution() {
        double threshold = (this.ticks == -1) ? this.counter : this.ticks;
        return threshold >= getDuration();
    }

    /**
     * Adds a metadata to the timer instance, this can be accessed
     * on future instance ticks until the end is reached
     *
     * @param key The key identifier
     * @param value The value related to the identifier
     */
    public void addData(String key, String value) {
        if (this.data == null) this.data = new HashMap<>();

        this.data.put(key, value);
    }

    /**
     * @param key The key identifier
     * @return {@link Optional}, empty if the key was not found, or containing
     * the data if found
     */
    public Optional<String> getData(String key) {
        if (this.data == null) return Optional.empty();

        return Optional.ofNullable(this.data.get(key));
    }

    /**
     * @param counter The counter to be set, beware this modifies the timer duration
     */
    public void setCounter(int counter) {
        this.counter = counter;
    }
}
