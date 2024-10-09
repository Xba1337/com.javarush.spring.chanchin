package com.javarush.controller;

import com.javarush.domain.Task;
import com.javarush.service.TaskService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


@Controller
@RequestMapping("/")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/")
    public String tasks(Model model,
                        @RequestParam(value = "page", required = false, defaultValue = "1") int page,
                        @RequestParam(value = "size", required = false, defaultValue = "10") int size) {
        List<Task> tasks = taskService.getAllTasks((page-1) * size, size);

        model.addAttribute("tasks", tasks);
        model.addAttribute("current_page", page);

        int totalPages = (int) Math.ceil(1.0 * taskService.getAllCount() / size);
        if (totalPages > 1) {
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages).boxed().collect(Collectors.toList());
            model.addAttribute("page_numbers", pageNumbers);
        }

        return "tasks";
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable("id") int id,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        taskService.deleteTask(id);

        return "redirect:/?page=" + page;
    }

    @PostMapping("/")
    public String create(@ModelAttribute("task") Task task,
                         @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        taskService.createTask(task.getDescription(), task.getStatus());

        return "redirect:/?page=" + page;
    }

    @PostMapping("/{id}")
    public String edit(@ModelAttribute Task task,
                       @RequestParam(value = "page", required = false, defaultValue = "1") int page) {
        taskService.updateTask(task.getId(), task.getDescription(), task.getStatus());

        return "redirect:/?page=" + page;
    }
}
