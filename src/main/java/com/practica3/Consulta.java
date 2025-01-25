/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/JSP_Servlet/Servlet.java to edit this template
 */
package com.practica3;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet Consulta
 * 
 * Este servlet se encarga de realizar una consulta a la base de datos
 * para obtener información de libros publicados en el año 1995. Genera una tabla
 * HTML con los resultados obtenidos.
 *
 */
@WebServlet("/Consulta")
public class Consulta extends HttpServlet {

    /**
     * Procesa las solicitudes GET enviadas al servlet.
     *
     * @param request  el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que se utiliza para devolver la respuesta al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        // StringBuilder para almacenar el HTML que vamos a devolver
        StringBuilder tablaHTML = new StringBuilder();

        try (Connection connection = Connexio.getConnection()) {
            /**
             * Consulta SQL para recuperar los libros publicados en 1995.
             * Utiliza la clase {@link Connexio} para establecer la conexión con la base de datos.
             */
            String sql = "SELECT id, titol, isbn, any_publicacio FROM llibres WHERE any_publicacio = '1995'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // Crear la tabla HTML
            tablaHTML.append("<table border='1'>");
            tablaHTML.append("<tr><th>ID</th><th>Título</th><th>ISBN</th><th>Año de Publicación</th></tr>");

            // Itera sobre los resultados de la consulta y genera las filas de la tabla
            while (resultSet.next()) {
                tablaHTML.append("<tr>");
                tablaHTML.append("<td>").append(resultSet.getInt("id")).append("</td>");
                tablaHTML.append("<td>").append(resultSet.getString("titol")).append("</td>");
                tablaHTML.append("<td>").append(resultSet.getString("isbn")).append("</td>");
                tablaHTML.append("<td>").append(resultSet.getInt("any_publicacio")).append("</td>");
                tablaHTML.append("</tr>");
            }

            tablaHTML.append("</table>");

        } catch (SQLException e) {
            /**
             * Maneja errores relacionados con la base de datos.
             *
             * @param e la excepción {@link SQLException} que contiene información sobre el error.
             */
            tablaHTML.append("<p style='color:red;'>❌ Error al consultar la base de datos: ").append(e.getMessage()).append("</p>");
            e.printStackTrace();
        } catch (ClassNotFoundException ex) {
            /**
             * Maneja errores relacionados con la carga del controlador JDBC.
             *
             * @param ex la excepción {@link ClassNotFoundException} si no se encuentra el controlador.
             */
            Logger.getLogger(Consulta.class.getName()).log(Level.SEVERE, null, ex);
        }

        // Enviar la respuesta al cliente (HTML)
        response.setContentType("text/html;charset=UTF-8");
        try (var out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<title>📚 Consulta de Llibres</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("<h1>📚 Consulta de Llibres</h1>");
            out.println(tablaHTML.toString());
            out.println("</body>");
            out.println("</html>");
        }
    }
}
