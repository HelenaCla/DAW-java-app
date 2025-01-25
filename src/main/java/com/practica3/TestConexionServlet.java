package com.practica3;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet("/TestConexionServlet")
public class TestConexionServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        // Usa un try con recursos para asegurar que PrintWriter se cierra correctamente
        try (PrintWriter out = response.getWriter()) {
            try {
                // Llamada a la clase de conexión
                Connection connection = Connexio.getConnection();
                if (connection != null) {
                    out.println("<h1>✅ ¡Conexión a la base de datos exitosa desde el Servlet!</h1>");
                }
            } catch (SQLException e) {
                // Mostramos el error al usuario para que sepamos qué está pasando
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("<h1>❌ Error al conectar con la base de datos desde el Servlet</h1>");
                out.println("<p>Mensaje de error: " + e.getMessage() + "</p>");
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(TestConexionServlet.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
}
