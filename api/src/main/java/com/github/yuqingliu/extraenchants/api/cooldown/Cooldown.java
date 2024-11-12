package com.github.yuqingliu.extraenchants.api.cooldown;

import java.time.Duration;

public interface Cooldown {
    String getId();
    void start();
    Duration getRemainingDuration();
    long getRemainingSeconds();
}
