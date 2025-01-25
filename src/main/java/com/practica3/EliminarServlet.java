package com.practica3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// Definición del servlet con su URL de mapeo
@WebServlet(name = "EliminarServlet", urlPatterns = {"/eliminarLlibre"})
public class EliminarServlet extends HttpServlet {
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {

        // Configuración del tipo de contenido de la respuesta
        response.setContentType("text/html;charset=UTF-8");

        // Obtener el ISBN del libro que se quiere eliminar
        String isbn = request.getParameter("isbn");

        try (PrintWriter out = response.getWriter()) {
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Eliminar Llibre</title>");
            out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container mt-5'>");

            out.println("<h1 class='text-center mb-4'>Eliminar Libro</h1>");
            
            try (Connection connection = Connexio.getConnection()) {
                
                // Eliminar de la tabla `llibre_autor`
                String deleteLlibreAutorSQL = "DELETE FROM llibre_autor WHERE id_llibre = (SELECT id FROM llibres WHERE isbn = ?)";
                PreparedStatement stmtDeleteLlibreAutor = connection.prepareStatement(deleteLlibreAutorSQL);
                stmtDeleteLlibreAutor.setString(1, isbn);
                int filasEliminadasLlibreAutor = stmtDeleteLlibreAutor.executeUpdate();
                stmtDeleteLlibreAutor.close();

                // Eliminar de la tabla `llibres`
                String deleteLlibreSQL = "DELETE FROM llibres WHERE isbn = ?";
                PreparedStatement stmtDeleteLlibre = connection.prepareStatement(deleteLlibreSQL);
                stmtDeleteLlibre.setString(1, isbn);
                int filasEliminadasLlibre = stmtDeleteLlibre.executeUpdate();
                stmtDeleteLlibre.close();

                if (filasEliminadasLlibre > 0) {
                    out.println("<div class='alert alert-success text-center' role='alert'>");
                    out.println("<h2>✅ Libro eliminado correctamente</h2>");
                    out.println("</div>");
                } else {
                    out.println("<div class='alert alert-warning text-center' role='alert'>");
                    out.println("<h2>⚠️ No se ha podido eliminar el libro</h2>");
                    out.println("</div>");
                }

            } catch (SQLException e) {
                e.printStackTrace();
                out.println("<div class='alert alert-danger' role='alert'>");
                out.println("<p>❌ Error al eliminar el libro de la base de datos: " + e.getMessage() + "</p>");
                out.println("</div>");
            }

            out.println("<div class='text-center mt-4'>");
            out.println("<a href='llibreria.jsp' class='btn btn-primary'>🔙 Volver a la lista de libros</a>");
            out.println("</div>");

            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");
            
        } catch (Exception e) {
            e.printStackTrace();
            response.getWriter().println("<p>Error general: " + e.getMessage() + "</p>");
        }

    }
}
