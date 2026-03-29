package com.manager.cafe.controller;

import com.manager.cafe.entity.Expense;
import com.manager.cafe.security.ExpenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/expenses")
public class ExpenseController {

    @Autowired
    private ExpenseService service;

    @GetMapping
    public List<Expense> getAll(Authentication auth) {
        return service.getExpenses(auth);
    }

    @PostMapping
    public Expense add(@RequestBody Expense expense, Authentication auth) {
        return service.addExpense(expense, auth);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id, Authentication auth) {
        service.deleteExpense(id, auth);
    }
}
