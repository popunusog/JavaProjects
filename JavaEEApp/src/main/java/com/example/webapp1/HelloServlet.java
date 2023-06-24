package com.example.webapp1;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;

@WebServlet(name = "helloServlet", value = "/hello-servlet")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Wprowadź dane!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h1>" + message + "</h1>");
        out.println("<script>");
        out.println("function validateForm() {");
        out.println("var minPercentage = parseInt(document.getElementById(\"minPercentage\").value);");
        out.println("var maxPercentage = parseInt(document.getElementById(\"maxPercentage\").value);");
        out.println("var minMl = parseInt(document.getElementById(\"minPojemnosc\").value);");
        out.println("var maxMl = parseInt(document.getElementById(\"maxPojemnosc\").value);");
        out.println("var minPrice = parseInt(document.getElementById(\"minCena\").value);");
        out.println("var maxPrice = parseInt(document.getElementById(\"maxCena\").value);");
        out.println("");
        out.println("if (minPercentage > maxPercentage) {");
        out.println("alert(\"Minimum Percentage cannot be greater than maximum Percentage!\");");
        out.println("return false;");
        out.println("}");
        out.println("if (minMl > maxMl) {");
        out.println("alert(\"Minimum ml cannot be greater than maximum ml!\");");
        out.println("return false;");
        out.println("}");
        out.println("if (minPrice > maxPrice) {");
        out.println("alert(\"Minimum price cannot be greater than maximum price!\");");
        out.println("return false;");
        out.println("}");
        out.println("}");
        out.println("</script>");

        out.println("<form method=\"GET\" onsubmit=\"return validateForm();\">");
        out.println("Minimum Percentage: <input type=\"number\" id=\"minPercentage\" name=\"minPercentage\" min=\"0\" max=\"100\"><br>");
        out.println("Maximum Percentage: <input type=\"number\" id=\"maxPercentage\" name=\"maxPercentage\" min=\"0\" max=\"100\"><br>");
        out.println("Minimum Pojemnosc: <input type=\"number\" id=\"minPojemnosc\" name=\"minPojemnosc\" min=\"0\" max=\"10000\"><br>");
        out.println("Maximum Pojemnosc: <input type=\"number\" id=\"maxPojemnosc\" name=\"maxPojemnosc\" min=\"0\" max=\"10000\"><br>");
        out.println("Minimum Cena: <input type=\"number\" id=\"minCena\" name=\"minCena\" min=\"0\" max=\"10000\"><br>");
        out.println("Maximum Cena: <input type=\"number\" id=\"maxCena\" name=\"maxCena\" min=\"0\" max=\"10000\"><br>");
        out.println("<input type=\"submit\" value=\"Submit\">");
        out.println("</form>");

        int minPercentage = 0;
        int maxPercentage = 100;
        int minPojemnosc = 0;
        int maxPojemnosc = 10000;
        int minCena = 0;
        int maxCena = 10000;
        String minPercentageStr = request.getParameter("minPercentage");
        String maxPercentageStr = request.getParameter("maxPercentage");
        String minPojemnoscStr = request.getParameter("minPojemnosc");
        String maxPojemnoscStr = request.getParameter("maxPojemnosc");
        String minCenaStr = request.getParameter("minCena");
        String maxCenaStr = request.getParameter("maxCena");
        if (minPercentageStr != null && !minPercentageStr.isEmpty()) {
            minPercentage = Integer.parseInt(minPercentageStr);
        }
        if (maxPercentageStr != null && !maxPercentageStr.isEmpty()) {
            maxPercentage = Integer.parseInt(maxPercentageStr);
        }
        if (minPojemnoscStr != null && !minPojemnoscStr.isEmpty()) {
            minPojemnosc = Integer.parseInt(minPojemnoscStr);
        }
        if (maxPojemnoscStr != null && !maxPojemnoscStr.isEmpty()) {
            maxPojemnosc = Integer.parseInt(maxPojemnoscStr);
        }
        if (minCenaStr != null && !minCenaStr.isEmpty()) {
            minCena = Integer.parseInt(minCenaStr);
        }
        if (maxCenaStr != null && !maxCenaStr.isEmpty()) {
            maxCena = Integer.parseInt(maxCenaStr);
        }


        try {
            Class.forName("org.apache.derby.jdbc.EmbeddedDriver");
            Connection connection = DriverManager.getConnection("jdbc:derby:ALKOHOLE2Db;create=true");
            Statement statement = connection.createStatement();
            String query =
                    "SELECT * FROM ALKOHOLE2 WHERE PROCENTY BETWEEN " + minPercentage + " AND " + maxPercentage +
                    " AND POJEMNOSC BETWEEN " + minPojemnosc + " AND " + maxPojemnosc +
                    " AND CENA BETWEEN " + minCena + " AND " + maxCena + " ORDER BY CENA";
            ResultSet resultSet = statement.executeQuery(query);

            out.println("<table>");
            out.println("<tr><th>ID</th><th>Nazwa</th><th>Pojemnosc</th><th>Procenty</th><th>Cena</th><th>Image</th></tr>");
            while (resultSet.next()) {
                int id = resultSet.getInt("ID");
                String nazwa = resultSet.getString("NAZWA");
                int pojemnosc = resultSet.getInt("POJEMNOSC");
                int procenty = resultSet.getInt("PROCENTY");
                int cena = resultSet.getInt("CENA");
                String imagePath = resultSet.getString("IMAGE_PATH");

                out.println("<tr><td>" + id + "</td><td>" + nazwa + "</td><td>" + pojemnosc + "</td><td>" + procenty + "</td><td>" + cena + "</td><td><img src=\"" + imagePath + "\" alt=\"Image\"></td></tr>");
            }
            out.println("</table>");

            resultSet.close();
            statement.close();
            connection.close();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

        out.println("</body></html>");
    }

    public void destroy() {
    }
}