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
import java.util.logging.Level;
import java.util.logging.Logger;

// Definimos el servlet con la URL /editarLlibre
@WebServlet(name = "EditarLlibresServlet", urlPatterns = {"/editarLlibre"})
public class EditarLlibresServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuración de la respuesta
        response.setContentType("text/html;charset=UTF-8");

        // Obtener los parámetros del formulario
      // Recoger parámetros del formulario
        String id = request.getParameter("id");
        String titol = request.getParameter("titol");
        String isbn = request.getParameter("isbn");
        String anyPublicacio = request.getParameter("any_publicacio");
        String idEditorial = request.getParameter("id_editorial");

        try {
            // Conexión a la base de datos
            Connection connection = Connexio.getConnection();
            
            // SQL para actualizar el libro
            String sql = "UPDATE llibres SET titol = ?, isbn = ?, any_publicacio = ?, id_editorial = ? WHERE id = ?";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            pstmt.setString(1, titol);
            pstmt.setString(2, isbn);
            pstmt.setInt(3, Integer.parseInt(anyPublicacio));
            pstmt.setInt(4, Integer.parseInt(idEditorial));
            pstmt.setInt(5, Integer.parseInt(id));

            int filasActualizadas = pstmt.executeUpdate();
            
            if (filasActualizadas > 0) {
                response.sendRedirect("llibreria.jsp");
            } else {
                response.getWriter().println("Error al actualizar el libro.");
            }
            
            pstmt.close();
            connection.close();
        } catch (SQLException | NumberFormatException e) {
            e.printStackTrace();
            response.getWriter().println("Error al actualizar el libro: " + e.getMessage());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(EditarLlibresServlet.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
