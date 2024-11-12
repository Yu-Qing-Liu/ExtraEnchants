package com.github.yuqingliu.extraenchants.cooldown;

import java.time.Duration;
import java.time.Instant;

import com.github.yuqingliu.extraenchants.api.cooldown.Cooldown;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class CooldownImpl implements Cooldown {
    @EqualsAndHashCode.Include
    private final String id;

    private final Duration duration;
    private Instant end = null;

    @Override
    public void start() {
        if(getRemainingDuration().isZero()) {
            Instant now = Instant.now();
            this.end = now.plus(duration);   
        }
    }

    @Override
    public Duration getRemainingDuration() {
        if(end == null) {
            return Duration.ZERO;
        }
        Instant now = Instant.now();
        if(now.isBefore(end)) {
            return Duration.between(now, end);
        }
        return Duration.ZERO;
    }
    
    @Override
    public long getRemainingSeconds() {
        return getRemainingDuration().toSeconds();
    }
}
