package dev.tehin.tlib.utilities.task;

import java.util.function.Consumer;

public class RepeatingTask extends TaskTimer {

    public RepeatingTask(Consumer<TaskTimer> task, Runnable end, double durationInTicks, int delayInTicks) {
        super(task, end, durationInTicks / (delayInTicks * 50), 0, delayInTicks * 50, true);

        if (delayInTicks > durationInTicks) throw new IllegalArgumentException("Delay cannot be greater than duration");
    }
}
