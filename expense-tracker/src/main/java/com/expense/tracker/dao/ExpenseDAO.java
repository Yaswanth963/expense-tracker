package com.expense.tracker.dao;

import com.expense.tracker.util.DBUtil;
import com.expense.tracker.model.Expense;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDAO {
    public boolean addExpense(Expense expense) throws SQLException {
        String sql = "INSERT INTO expenses (user_id, amount, type, date, name) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, expense.getUserId());
            stmt.setDouble(2, expense.getAmount());
            stmt.setString(3, expense.getType());
            stmt.setDate(4, expense.getDate());
            stmt.setString(5, expense.getName());
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Expense> getExpensesByUser(int userId) throws SQLException {
        String sql = "SELECT * FROM expenses WHERE user_id = ?";
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getString("type"),
                        rs.getDate("date"),
                        rs.getString("name")
                ));
            }
        }
        return expenses;
    }

    public boolean updateExpense(Expense expense) throws SQLException {
        String sql = "UPDATE expenses SET amount = ?, type = ?, date = ?, name = ? WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setDouble(1, expense.getAmount());
            stmt.setString(2, expense.getType());
            stmt.setDate(3, expense.getDate());
            stmt.setString(4, expense.getName());
            stmt.setInt(5, expense.getId());
            stmt.setInt(6, expense.getUserId());
            return stmt.executeUpdate() > 0;
        }
    }

    public boolean deleteExpense(int expenseId, int userId) throws SQLException {
        String sql = "DELETE FROM expenses WHERE id = ? AND user_id = ?";
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, expenseId);
            stmt.setInt(2, userId);
            return stmt.executeUpdate() > 0;
        }
    }

    public List<Expense> getAllExpenses() throws SQLException {
        String sql = "SELECT * FROM expenses";
        List<Expense> expenses = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                expenses.add(new Expense(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getDouble("amount"),
                        rs.getString("type"),
                        rs.getDate("date"),
                        rs.getString("name")
                ));
            }
        }
        return expenses;
    }
}