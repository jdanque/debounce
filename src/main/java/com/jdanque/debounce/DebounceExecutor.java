package com.jdanque.debounce;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * The debouncer, executes tasks with a delay, and cancels
 * any previous unexecuted tasks.
 */
public class DebounceExecutor {
    private ScheduledExecutorService executor;
	private ScheduledFuture<?> future;

	public DebounceExecutor() {
		this.executor = Executors.newSingleThreadScheduledExecutor();
	}

	public ScheduledExecutorService debounce(Debounce clazz) {

	    if (future != null && !future.isDone()) {
			future.cancel(false);
		}

        if( clazz.getMode().equals(DebounceMode.DELAY) ||
            (clazz.getMode().equals(DebounceMode.CANCEL) && future == null )
            ){

            future = executor.schedule(clazz.getTask(),
                    clazz.getDelay(),
                    TimeUnit.MILLISECONDS);
        }

		return executor;
	}
}
