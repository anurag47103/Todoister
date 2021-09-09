package com.bawp.todoister.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bawp.todoister.data.TodoisterRepository;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    public static TodoisterRepository repository;
    public final LiveData<List<Task>> allTask;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository = new TodoisterRepository(application);
        allTask = repository.getAllTasks();
    }

    public LiveData<List<Task>> getAllTask() {return allTask; }

    public LiveData<Task> get(long id) {return repository.get(id);}

    public static void insert(Task task) {repository.insert(task);}

    public static void update(Task task) {repository.update(task);}

    public static void delelte(Task task) {repository.delete(task);}

}