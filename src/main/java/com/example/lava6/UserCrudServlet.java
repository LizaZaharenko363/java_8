package com.example.lava6;

import com.example.model.User;
import com.example.service.UserService;
import jakarta.inject.Inject;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@WebServlet("/user-crud")
public class UserCrudServlet extends HttpServlet {
    @Inject
    private UserService userService;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        String operation = request.getParameter("op");
        String message = "";
        boolean success = true;

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>User CRUD Operations</title>");
        out.println("    <style>");
        out.println("        body { font-family: Arial, sans-serif; max-width: 800px; margin: 0 auto; padding: 20px; }");
        out.println("        .success { color: green; }");
        out.println("        .error { color: red; }");
        out.println("        table { width: 100%; border-collapse: collapse; }");
        out.println("        th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("        .button { display: inline-block; margin-bottom:20px; padding: 5px 10px; background-color: #4CAF50; color: white; text-decoration: none; }");
        out.println("    </style>");
        out.println("</head>");
        out.println("<body>");
        out.println("<h1>User CRUD Operations</h1>");

        try {
            switch (operation != null ? operation : "list") {
                case "create":
                    out.println("<h2>Create User</h2>");
                    out.println("<form action='user-crud' method='POST'>");
                    out.println("Name: <input type='text' name='name' required><br>");
                    out.println("Email: <input type='email' name='email' required><br>");
                    out.println("<input type='submit' name='op' value='create'>");
                    out.println("</form>");
                    break;

                case "read":
                    out.println("<h2>Read User by Email</h2>");
                    out.println("<form action='user-crud' method='POST'>");
                    out.println("Email: <input type='email' name='email' required><br>");
                    out.println("<input type='submit' name='op' value='read'>");
                    out.println("</form>");
                    break;

                case "update":
                    out.println("<h2>Update User</h2>");
                    out.println("<form action='user-crud' method='POST'>");
                    out.println("User ID: <input type='number' name='id' required><br>");
                    out.println("New Name: <input type='text' name='name' required><br>");
                    out.println("<input type='submit' name='op' value='update'>");
                    out.println("</form>");
                    break;

                case "delete":
                    out.println("<h2>Delete User</h2>");
                    out.println("<form action='user-crud' method='POST'>");
                    out.println("User ID: <input type='number' name='id' required><br>");
                    out.println("<input type='submit' name='op' value='delete'>");
                    out.println("</form>");
                    break;

                case "date-range":
                    out.println("<h2>Users by Registration Date Range</h2>");
                    out.println("<form action='user-crud' method='POST'>");
                    out.println("Start Date (YYYY-MM-DD): <input type='date' name='startDate' required><br>");
                    out.println("End Date (YYYY-MM-DD): <input type='date' name='endDate' required><br>");
                    out.println("<input type='submit' name='op' value='date-range'>");
                    out.println("</form>");
                    break;

                case "name-search":
                    out.println("<h2>Search Users by Name</h2>");
                    out.println("<form action='user-crud' method='POST'>");
                    out.println("Name Contains: <input type='text' name='namePart' required><br>");
                    out.println("<input type='submit' name='op' value='name-search'>");
                    out.println("</form>");
                    break;

                case "active-count":
                    long activeUserCount = userService.countActiveUsers();
                    out.println("<h2>Active Users Count</h2>");
                    out.println("<p>Total Active Users: " + activeUserCount + "</p>");
                    break;

                case "oldest-user":
                    Optional<User> oldestUser = userService.getOldestUser();
                    out.println("<h2>Oldest User</h2>");
                    if (oldestUser.isPresent()) {
                        User user = oldestUser.get();
                        out.println("<p>ID: " + user.getId() + "</p>");
                        out.println("<p>Name: " + user.getName() + "</p>");
                        out.println("<p>Email: " + user.getEmail() + "</p>");
                        out.println("<p>Registration Date: " + user.getRegistrationDate() + "</p>");
                    } else {
                        out.println("<p>No users found.</p>");
                    }
                    break;

                case "list":
                default:
                    List<User> activeUsers = userService.getActiveUsers();
                    out.println("<h2>Active Users:</h2>");
                    out.println("<table>");
                    out.println("<tr><th>ID</th><th>Name</th><th>Email</th><th>Registration Date</th><th>Active</th></tr>");

                    for (User u : activeUsers) {
                        out.println("<tr>");
                        out.println("<td>" + u.getId() + "</td>");
                        out.println("<td>" + u.getName() + "</td>");
                        out.println("<td>" + u.getEmail() + "</td>");
                        out.println("<td>" + u.getRegistrationDate() + "</td>");
                        out.println("<td>" + u.getActive() + "</td>");
                        out.println("</tr>");
                    }
                    out.println("</table>");
                    break;
            }
        } catch (Exception e) {
            message = "Error: " + e.getMessage();
            success = false;
        }

        out.println("<div class='" + (success ? "success" : "error") + "'>");
        out.println("<p>" + message + "</p>");
        out.println("</div>");

        out.println("<div style='margin-top: 20px;'>");
        out.println("<a href='user-crud?op=create' class='button'>Create User</a> ");
        out.println("<a href='user-crud?op=read' class='button'>Read User</a> ");
        out.println("<a href='user-crud?op=update' class='button'>Update User</a> ");
        out.println("<a href='user-crud?op=delete' class='button'>Delete User</a> ");
        out.println("<a href='user-crud?op=date-range' class='button'>Users by Date Range</a> ");
        out.println("<a href='user-crud?op=name-search' class='button'>Search by Name</a> ");
        out.println("<a href='user-crud?op=active-count' class='button'>Active Users Count</a> ");
        out.println("<a href='user-crud?op=oldest-user' class='button'>Oldest User</a> ");
        out.println("<a href='user-crud' class='button'>List Users</a>");
        out.println("</div>");

        out.println("<br><a href='index.html'>Back to Home</a>");
        out.println("</body></html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        String operation = request.getParameter("op");
        String message = "";
        boolean success = true;

        try {
            switch (operation) {
                case "create":
                    String name = request.getParameter("name");
                    String email = request.getParameter("email");
                    User newUser = userService.createUser(name, email);
                    message = "Created User: " + newUser.getName();
                    break;

                case "read":
                    email = request.getParameter("email");
                    List<User> users = userService.getUsersByEmail(email);
                    if (!users.isEmpty()) {
                        message = "Found Users with email: " + email;
                        for (User u : users) {
                            message += "<br>Id: " + u.getId() + ", Name: " + u.getName();
                        }
                    } else {
                        message = "No users found with that email.";
                        success = false;
                    }
                    break;

                case "update":
                    Long id = Long.parseLong(request.getParameter("id"));
                    String newName = request.getParameter("name");
                    User updatedUser = userService.updateUser(id, newName);
                    if (updatedUser != null) {
                        message = "Updated User: " + updatedUser.getName();
                    } else {
                        message = "User update failed.";
                        success = false;
                    }
                    break;

                case "delete":
                    id = Long.parseLong(request.getParameter("id"));
                    userService.deleteUser(id);
                    message = "User Deleted";
                    break;

                case "date-range":
                    String startDateStr = request.getParameter("startDate");
                    String endDateStr = request.getParameter("endDate");
                    LocalDate startDate = LocalDate.parse(startDateStr);
                    LocalDate endDate = LocalDate.parse(endDateStr);

                    List<User> usersInRange = userService.getUsersByRegistrationDateRange(startDate, endDate);

                    message = "Users registered between " + startDate + " and " + endDate + ":<br>";
                    for (User u : usersInRange) {
                        message += u.getId() + " - " + u.getName() + " (" + u.getEmail() + ")<br>";
                    }
                    break;

                case "name-search":
                    String namePart = request.getParameter("namePart");
                    List<User> usersWithName = userService.getUsersWithNameContaining(namePart);

                    message = "Users with name containing '" + namePart + "':<br>";
                    for (User u : usersWithName) {
                        message += u.getId() + " - " + u.getName() + " (" + u.getEmail() + ")<br>";
                    }
                    break;

                default:
                    message = "Invalid operation.";
                    success = false;
                    break;
            }
        } catch (Exception e) {
            message = "Error: " + e.getMessage();
            success = false;
        }

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<!DOCTYPE html>");
        out.println("<html><body>");
        out.println("<h2>" + (success ? "Success" : "Error") + "</h2>");
        out.println("<p>" + message + "</p>");
        out.println("<a href='user-crud'>Back to CRUD operations</a>");
        out.println("</body></html>");
    }
}