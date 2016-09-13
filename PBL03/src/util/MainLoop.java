package util;

import java.util.logging.Level;
import java.util.logging.Logger;
import view.CarroFrame;

public class MainLoop implements Runnable {

    private CarroFrame game;
    private long desiredUpdateTime;
    private boolean running;

    private long afterTime;
    private long beforeTime = System.currentTimeMillis();

    private long overSleepTime = 0;

    /**
     * Create a new MainLoop object.
     *
     * @param loopSteps The LoopSteps that will be controlled by this loop.
     * @param ups Number of desired updates per second.
     * @param maxFrameSkips Maximum number of frame that can be skipped if the
     * graphical hardware is not fast enough to follow the desired ups.
     * @param noDelaysPerYield If the hardware is not fast enough to allow a
     * dellay between two frames, the delay will be enforced in this counter, so
     * other threads can process their actions.
     */
    public MainLoop(CarroFrame loopSteps, int ups) {
        super();

        if (ups < 1) {
            throw new IllegalArgumentException("You must display at least one frame per second!");
        }

        if (ups > 1000) {
            ups = 1000;
        }

        this.game = loopSteps;
        this.desiredUpdateTime = 1000000000L / ups;

    }

    /**
     * Sleep the given amount of time. Since the sleep() method of the thread
     * class is not precise, the overSleepTime will be calculated.
     *
     * @param nanos Number of nanoseconds to sleep.
     * @throws InterruptedException If the thread was interrupted
     */
    private void sleep(long nanos) throws InterruptedException {
        long beforeSleep = System.nanoTime();
        Thread.sleep(nanos / 1000000L, (int) (nanos % 1000000L));
        overSleepTime = System.nanoTime() - beforeSleep - nanos;
    }


    /**
     * Calculates the sleep time based in the calculation the previous loop. To
     * achieve this time, the frame display time will be subtracted by the time
     * elapsed in the last computation (afterTime - beforeTime). Then, if in the
     * previous loop there was an oversleep time, this will also be subtracted,
     * so the system can compensate this overtime.
     */
    private long calculateSleepTime() {
        return desiredUpdateTime - (afterTime - beforeTime);
    }

    /**
     * Runs the main loop. This method is not thread safe and should not be
     * called more than once.
     */
    @Override
    public void run() {
        game.setup();
        while (true) {
            beforeTime = System.nanoTime();

            // Updates, renders and paint the screen
            game.processLogics();
            game.paintScreen();

            afterTime = System.nanoTime();

            long sleepTime = calculateSleepTime();

            if (sleepTime >= 0) {
                try {
                    sleep(sleepTime);
                } catch (InterruptedException ex) {
                    Logger.getLogger(MainLoop.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }
}
