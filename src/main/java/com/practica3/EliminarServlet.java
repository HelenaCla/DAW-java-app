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

/**
 * Servlet EliminarServlet
 *
 * Este servlet maneja las solicitudes POST para eliminar un libro y sus asociaciones de la base de datos
 * basado en el ISBN proporcionado.
 * 
 */
@WebServlet(name = "EliminarServlet", urlPatterns = {"/eliminarLlibre"})
public class EliminarServlet extends HttpServlet {

    /**
     * Procesa las solicitudes POST para eliminar un libro.
     * <p>
     * Elimina los datos relacionados en las tablas `llibre_autor` y `llibres` utilizando
     * el ISBN proporcionado por el cliente.
     * </p>
     *
     * @param request  el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que se utiliza para devolver la respuesta al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
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
                /**
                 * Elimina las asociaciones del libro en la tabla `llibre_autor`.
                 * 
                 * @throws SQLException si ocurre un error al ejecutar la consulta.
                 */
                String deleteLlibreAutorSQL = "DELETE FROM llibre_autor WHERE id_llibre = (SELECT id FROM llibres WHERE isbn = ?)";
                PreparedStatement stmtDeleteLlibreAutor = connection.prepareStatement(deleteLlibreAutorSQL);
                stmtDeleteLlibreAutor.setString(1, isbn);
                int filasEliminadasLlibreAutor = stmtDeleteLlibreAutor.executeUpdate();
                stmtDeleteLlibreAutor.close();

                /**
                 * Elimina el libro de la tabla `llibres`.
                 * 
                 * @throws SQLException si ocurre un error al ejecutar la consulta.
                 */
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
                /**
                 * Maneja errores relacionados con la base de datos.
                 *
                 * @param e la excepción {@link SQLException} que contiene información sobre el error.
                 */
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
            /**
             * Maneja errores generales del servlet.
             *
             * @param e la excepción {@link Exception} que contiene información sobre el error.
             */
            e.printStackTrace();
            response.getWriter().println("<p>Error general: " + e.getMessage() + "</p>");
        }

    }
}
