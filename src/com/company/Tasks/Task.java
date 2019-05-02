package com.company.Tasks;

import java.util.function.Function;

import static com.company.Tasks.TaskState.*;

public class Task {
    private long execTime = 0;
    private TaskState zustand = OFFEN;
    private Function<Void,Boolean> fn = null;

    public Task(long execTime, Function<Void,Boolean> fn){
        this.execTime = execTime;
        this.fn = fn;
    }

    public boolean execute() {
        return (execute(false));
    }

    public boolean execute(boolean force) {
        if(verfiyReady() || force) {
            boolean x = fn.apply(null);
            if (x)
                zustand = ERLEDIGT;
            else
                zustand = FEHLGESCHLAGEN;
        }
        return (zustand == ERLEDIGT);
    }

    public boolean verfiyReady(){
        return ((execTime < System.currentTimeMillis()) && (zustand == OFFEN));
    }

}
