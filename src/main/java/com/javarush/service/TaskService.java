package com.javarush.service;


import com.javarush.dao.TaskDAO;
import com.javarush.domain.Status;
import com.javarush.domain.Task;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TaskService {

    private final TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public List<Task> getAllTasks(int offset, int limit) {
        return taskDAO.getAllTasks(offset, limit);
    }

    public Task getTask(int id) {
        Task task = taskDAO.getTask(id);

        if (task == null) {
            throw new RuntimeException("Not found task with id " + id);
        }

        return task;
    }

    public int getAllCount() {
        return taskDAO.getCount();
    }

    @Transactional
    public Task updateTask(int id, String description, Status status) {
        Task updatedTask = taskDAO.getTask(id);

        if (updatedTask == null) {
            throw new RuntimeException("Not found task with id " + id);
        }

        updatedTask.setDescription(description);
        updatedTask.setStatus(status);
        taskDAO.saveOrUpdate(updatedTask);

        return updatedTask;
    }

    @Transactional
    public Task createTask(String description, Status status) {
        Task task = new Task();
        task.setDescription(description);
        task.setStatus(status);
        taskDAO.saveOrUpdate(task);

        return task;
    }

    @Transactional
    public void deleteTask(int id) {
        Task task = taskDAO.getTask(id);
        if (task == null) {
            throw new RuntimeException("Not found task with id " + id);
        }

        taskDAO.deleteTask(task);
    }
}
