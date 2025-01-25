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

/**
 * Servlet TestConexionServlet
 * 
 * Este servlet prueba la conexión a la base de datos utilizando la clase {@link Connexio}.
 *
 */
@WebServlet("/TestConexionServlet")
public class TestConexionServlet extends HttpServlet {

    /**
     * Procesa las solicitudes GET para probar la conexión a la base de datos.
     * <p>
     * Muestra un mensaje HTML indicando si la conexión se estableció correctamente
     * o si ocurrió un error.
     * </p>
     *
     * @param request  el objeto {@link HttpServletRequest} que contiene la solicitud del cliente.
     * @param response el objeto {@link HttpServletResponse} que se utiliza para devolver la respuesta al cliente.
     * @throws ServletException si ocurre un error específico del servlet.
     * @throws IOException      si ocurre un error de entrada/salida.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Configuración del tipo de contenido de la respuesta
        response.setContentType("text/html;charset=UTF-8");

        // Usa un try con recursos para asegurar que PrintWriter se cierra correctamente
        try (PrintWriter out = response.getWriter()) {
            try {
                /**
                 * Intenta establecer una conexión a la base de datos.
                 *
                 * @throws SQLException           si ocurre un error al conectar con la base de datos.
                 * @throws ClassNotFoundException si no se encuentra el controlador JDBC.
                 */
                Connection connection = Connexio.getConnection();
                if (connection != null) {
                    out.println("<h1>✅ ¡Conexión a la base de datos exitosa desde el Servlet!</h1>");
                }
            } catch (SQLException e) {
                /**
                 * Maneja errores relacionados con la conexión a la base de datos.
                 *
                 * @param e la excepción {@link SQLException} que contiene detalles del error.
                 */
                response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                out.println("<h1>❌ Error al conectar con la base de datos desde el Servlet</h1>");
                out.println("<p>Mensaje de error: " + e.getMessage() + "</p>");
                e.printStackTrace();
            } catch (ClassNotFoundException ex) {
                /**
                 * Maneja errores relacionados con la carga del controlador JDBC.
                 *
                 * @param ex la excepción {@link ClassNotFoundException} si no se encuentra el controlador.
                 */
                Logger.getLogger(TestConexionServlet.class.getName()).log(Level.SEVERE, null, ex);
                out.println("<h1>❌ Error: No se encontró el controlador JDBC</h1>");
            }
        }
    }
}
