package com.jdanque.debounce;

import java.util.concurrent.ExecutorService;

/**
 * Enables debouncing a runnable task before invocation.<br/>
 *
 * Debouncing is a technique that postpones execution of a method until after
 * wait milliseconds since the last time it was invoked.
 * Useful for implementing behavior that should only happen after the input has stopped arriving.
 * <br/>
 * The idea is that a delay D is defined such that:
 * <ul>
 * <li>Each method call M is delayed by D.</li>
 * <li>When a new method call M occurs, if a previous call M was not yet
 * executed, it is canceled. And M is put to waiting instead.</li>
 * <li>When no new method call occurs during the delay D, then the currently
 * waiting call is effectively executed.</li>
 * </ul>
 *
 * Debounced methods will always return null (or void for void methods).
 *
 */
public class Debounce {

    /**
     * Private db of debouncers contructed
     */
    private static DebouncerStore<String> store = new DebouncerStore<>();

    /**
     * uniqueID of debounced task
     * if no id is supplied upon construction,
     * a uniqueID will be generated
     */
    private static long uniqueID;

    /**
     * ID of debounced task
     * see {@link Debounce#uniqueID}
     */
    private String id;

    /**
     * Delay in milleseconds before invocation of the debounced method
     */
    private long delay;

    /**
     * Defaults to DebounceMode.DELAY
     * will postpone method execution after specified

     * wait interval, but will still execute method
     */
    private DebounceMode mode;

    /**
     * A runnable task to be executed with debouncing
     */
    private Runnable task;

    public Debounce(){
        this(null, 0, ()->{});
    }

    public Debounce(long delay, Runnable task){
        this(null, delay, task);
    }

    public Debounce(String id, long delay, Runnable task){
        this(id, delay, DebounceMode.DELAY, task);
    }

    public Debounce(String id, long delay, DebounceMode mode, Runnable task){
        this.id = id;
        this.delay = delay;
        this.task = task;
        this.mode = mode;
    }

    private static String generateUniqueID(){
        while(store.hasDebouncer(String.valueOf(uniqueID))){
            uniqueID++;
        }
        return String.valueOf(uniqueID);
    }

    /**
     * Executes the saved runnable task
     * @return
     *  the executorservice holder
     */
    public ExecutorService execute() {

        if(this.getId() == null){
            this.id = generateUniqueID();
        }

        return store.registerOrGetDebouncer(this.getId())
                .debounce(this);
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public DebounceMode getMode() {
        return mode;
    }

    public void setMode(DebounceMode mode) {
        this.mode = mode;
    }

    public Runnable getTask() {
        return task;
    }

    public void setTask(Runnable task) {
        this.task = task;
    }

    public long getDelay() {
        return delay;
    }

    public void setDelay(long delay) {
        this.delay = delay;
    }
}
