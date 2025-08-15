package com.expense.tracker.servlet;

import com.expense.tracker.dao.ExpenseDAO;
import com.expense.tracker.model.Expense;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/expenses")
public class ExpenseCRUDServlet extends HttpServlet {
    private ExpenseDAO expenseDAO = new ExpenseDAO();

    private void setCorsHeaders(HttpServletResponse response) {
        response.setHeader("Access-Control-Allow-Origin", "*");
        response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        response.setHeader("Access-Control-Allow-Headers", "Content-Type");
    }

    @Override
    protected void doOptions(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        response.setStatus(HttpServletResponse.SC_OK);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        try {
            List<Expense> expenses = expenseDAO.getAllExpenses();
            response.setContentType("application/json");
            StringBuilder sb = new StringBuilder("[");
            for (int i = 0; i < expenses.size(); i++) {
                Expense e = expenses.get(i);
                sb.append(String.format("{\"id\":%d,\"userId\":%d,\"amount\":%.2f,\"type\":\"%s\",\"date\":\"%s\",\"name\":\"%s\"}",
                        e.getId(), e.getUserId(), e.getAmount(), e.getType(), e.getDate(), e.getName() == null ? "" : e.getName().replace("\"", "\\\"") ));
                if (i < expenses.size() - 1) sb.append(",");
            }
            sb.append("]");
            response.getWriter().write(sb.toString());
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        int userId = Integer.parseInt(request.getParameter("userId"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        String type = request.getParameter("type");
        Date date = Date.valueOf(request.getParameter("date"));
        String name = request.getParameter("name");
        Expense expense = new Expense(0, userId, amount, type, date, name);
        try {
            boolean success = expenseDAO.addExpense(expense);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": " + success + "}");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        double amount = Double.parseDouble(request.getParameter("amount"));
        String type = request.getParameter("type");
        Date date = Date.valueOf(request.getParameter("date"));
        String name = request.getParameter("name");
        Expense expense = new Expense(id, userId, amount, type, date, name);
        try {
            boolean success = expenseDAO.updateExpense(expense);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": " + success + "}");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setCorsHeaders(response);
        int id = Integer.parseInt(request.getParameter("id"));
        int userId = Integer.parseInt(request.getParameter("userId"));
        try {
            boolean success = expenseDAO.deleteExpense(id, userId);
            response.setContentType("application/json");
            response.getWriter().write("{\"success\": " + success + "}");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }
}
