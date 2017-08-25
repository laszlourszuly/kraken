package com.echsylon.kraken;

/**
 * This class know how to calculate the call rate limit state for API requests
 * and will temporary block the thread if the limit is exceeded.
 */
final class CallCounter {
    private final int decreaseInterval;
    private final int maxCount;

    private long lastCallTime;
    private int callCounter;


    /**
     * Creates a new call rate state machine and initializes it with rules
     * according to the supplied tier.
     *
     * @param tier The tier that decides which call rate limit rules to apply.
     */
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


    /**
     * Calculates the next state of the call rate state machine according to the
     * supplied request cost. If the call rate limit is exceeded, the calling
     * thread is blocked to allow the call counter to chill down and allow the
     * request to proceed.
     *
     * @param cost The cost of the request being handled right now.
     */
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
