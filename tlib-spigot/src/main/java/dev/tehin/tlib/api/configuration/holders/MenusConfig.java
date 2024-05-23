package dev.tehin.tlib.api.configuration.holders;

import lombok.Getter;

@Getter
public class MenusConfig {

    private long clickDelayInMs = 100;

    /**
     * How much should the delay between inventory clicks be
     * <br>
     * Default Value: 100 (ms)
     */
    public MenusConfig clickDelayInMs(long delay) {
        this.clickDelayInMs = delay;
        return this;
    }

}
