package com.jdanque.debounce;

/**
 * Modes for a debounced method
 */
public enum DebounceMode {
    /**
     * Default mode for a debounced task,
     * will postpone task execution after specified
     * wait interval, but will still execute task
     */
    DELAY,

    /**
     * Cancels task execution if another call was
     * made on a debounced task. If task is called
     * only once it will just continue like {@link DebounceMode#DELAY}
     */
    CANCEL

}
