package com.company.Tasks;

import com.company.Utils.Utils;

import java.util.concurrent.CopyOnWriteArrayList;

public class TaskHandler extends Thread {
    private CopyOnWriteArrayList<Task> tasks = new CopyOnWriteArrayList();

    @Override
    public void run() {
        while(true) {
            checkTaskList();
            Utils.sleep(1000);
        }
    }

    private void checkTaskList(){
        for(Task t:tasks) {
            if(t.execute()) {
                //removeTask(t);
            }
        }
    }

    public boolean addTask(Task tsk){
        return (tasks.add(tsk));
    }

    public boolean removeTask(Task tsk){
        return (tasks.remove(tsk));
    }
}
