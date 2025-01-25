package com.practica3;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet EditarLlibresServlet
 * 
 * Este servlet se encarga de procesar las solicitudes POST para actualizar
 * la información de un libro en la base de datos.
 * 
 */
@WebServlet(name = "EditarLlibresServlet", urlPatterns = {"/editarLlibre"})
public class EditarLlibresServlet extends HttpServlet {

    /**
     * Procesa las solicitudes POST enviadas al servlet.
     * Recoge los parámetros del formulario y actualiza el registro
     * correspondiente en la base de datos.
     *
     * @param request  el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que se utiliza para devolver la respuesta al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuración de la respuesta
        response.setContentType("text/html;charset=UTF-8");

        // Recoger parámetros del formulario
        String id = request.getParameter("id");
        String titol = request.getParameter("titol");
        String isbn = request.getParameter("isbn");
        String anyPublicacio = request.getParameter("any_publicacio");
        String idEditorial = request.getParameter("id_editorial");

        try {
            /**
             * Establece una conexión con la base de datos y ejecuta una consulta
             * SQL para actualizar los datos del libro.
             *
             * @throws SQLException           si ocurre un error al interactuar con la base de datos.
             * @throws ClassNotFoundException si no se encuentra el controlador JDBC.
             */
            Connection connection = Connexio.getConnection();

            // SQL para actualizar el libro
            String sql = "UPDATE llibres SET titol = ?, isbn = ?, any_publicacio = ?, id_editorial = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            // Configurar los valores de la consulta
            pstmt.setString(1, titol);
            pstmt.setString(2, isbn);
            pstmt.setInt(3, Integer.parseInt(anyPublicacio));
            pstmt.setInt(4, Integer.parseInt(idEditorial));
            pstmt.setInt(5, Integer.parseInt(id));

            // Ejecutar la consulta
            int filasActualizadas = pstmt.executeUpdate();

            if (filasActualizadas > 0) {
                // Redirigir a la página principal si la actualización es exitosa
                response.sendRedirect("llibreria.jsp");
            } else {
                // Enviar un mensaje de error si no se actualizó ningún registro
                response.getWriter().println("Error al actualizar el libro.");
            }

            // Cerrar recursos
            pstmt.close();
            connection.close();
        } catch (SQLException | NumberFormatException e) {
            /**
             * Maneja errores relacionados con la base de datos o con la conversión de datos.
             *
             * @param e la excepción lanzada, puede ser {@link SQLException} o {@link NumberFormatException}.
             */
            e.printStackTrace();
            response.getWriter().println("Error al actualizar el libro: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            /**
             * Maneja errores relacionados con la carga del controlador JDBC.
             *
             * @param ex la excepción {@link ClassNotFoundException} si no se encuentra el controlador.
             */
            Logger.getLogger(EditarLlibresServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
