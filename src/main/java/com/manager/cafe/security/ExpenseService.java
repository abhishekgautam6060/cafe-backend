package com.manager.cafe.security;

import com.manager.cafe.entity.Expense;
import com.manager.cafe.entity.User;
import com.manager.cafe.repository.ExpenseRepository;
import com.manager.cafe.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ExpenseService {

    @Autowired
    private ExpenseRepository repo;

    @Autowired
    private UserRepository userRepo;

    public List<Expense> getExpenses(Authentication auth) {
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();
        return repo.findByUser(user);
    }

    public Expense addExpense(Expense expense, Authentication auth) {
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();

        expense.setUser(user);
        return repo.save(expense);
    }

    public void deleteExpense(Long id, Authentication auth) {
        User user = userRepo.findByEmail(auth.getName()).orElseThrow();

        Expense expense = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Expense not found"));

        if (!expense.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Unauthorized");
        }

        repo.delete(expense);
    }
}
