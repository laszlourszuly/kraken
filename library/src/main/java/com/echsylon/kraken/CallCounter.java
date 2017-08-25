package com.echsylon.kraken;

/**
 * Created by laszlo on 2017-08-25.
 */
final class CallCounter {
    private final int decreaseInterval;
    private final int maxCount;

    private long lastCallTime;
    private int callCounter;

    CallCounter(int tier) {
        lastCallTime = 0L;
        callCounter = 0;

        switch (tier) {
            case 4:
                maxCount = 20;
                decreaseInterval = 1;
                break;
            case 3:
                maxCount = 20;
                decreaseInterval = 2;
                break;
            case 2: // Intentional fallthrough
            case 1:
            case 0:
            default:
                maxCount = 15;
                decreaseInterval = 3;
                break;
        }
    }

    synchronized void allocate(int cost) {
        // Early bail-out
        if (lastCallTime == 0L) {
            callCounter += cost;
            lastCallTime = System.currentTimeMillis() / 1000L;
            return;
        }

        // Calculate the number of seconds since last call.
        long currentTime = System.currentTimeMillis() / 1000L;
        long durationSinceLastCall = currentTime - lastCallTime;

        // Update the call counter accordingly
        int reduction = (int) (durationSinceLastCall / decreaseInterval);
        callCounter = Math.max(0, callCounter - reduction);
        callCounter += cost;

        // Wait for the call counter to chill down
        if (callCounter > maxCount)
            try {
                Thread.sleep((callCounter - maxCount) * decreaseInterval * 1000L);
                callCounter = maxCount;
            } catch (InterruptedException e) {
                // Oh dear!
            }

        // Update the last call timestamp
        lastCallTime = System.currentTimeMillis() / 1000L;
    }

}
