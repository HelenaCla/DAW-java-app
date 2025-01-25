package com.practica3;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Servlet InsertarServlet
 * 
 * Este servlet maneja las solicitudes POST para insertar un nuevo libro en la base de datos.
 * También inserta los autores si no existen y establece relaciones entre el libro y los autores
 * en la tabla `llibre_autor`.
 * 
 */
@WebServlet(name = "InsertarLlibreServlet", urlPatterns = {"/insertarLlibre"})
public class InsertarServlet extends HttpServlet {

    /**
     * Procesa las solicitudes POST para insertar un libro y sus autores en la base de datos.
     * <p>
     * Realiza los siguientes pasos:
     * <ol>
     *   <li>Inserta el libro en la tabla `llibres`.</li>
     *   <li>Verifica si los autores ya existen. Si no, los inserta en la tabla `autors`.</li>
     *   <li>Establece relaciones entre el libro y los autores en la tabla `llibre_autor`.</li>
     * </ol>
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

        // Obtener parámetros del formulario
        String titol = request.getParameter("titol");
        String isbn = request.getParameter("isbn");
        String anyPublicacio = request.getParameter("any_publicacio");
        String[] autors = request.getParameterValues("autors"); // Los autores vienen en un array

        try (PrintWriter out = response.getWriter()) {
            // Inicio del HTML con Bootstrap
            out.println("<!DOCTYPE html>");
            out.println("<html lang='es'>");
            out.println("<head>");
            out.println("<meta charset='UTF-8'>");
            out.println("<meta name='viewport' content='width=device-width, initial-scale=1.0'>");
            out.println("<title>Inserción de Libro</title>");
            out.println("<link rel='stylesheet' href='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css'>");
            out.println("</head>");
            out.println("<body>");
            out.println("<div class='container mt-5'>");

            try (Connection connection = Connexio.getConnection()) {
                /**
                 * Inserta el libro en la tabla `llibres`.
                 *
                 * @throws SQLException si ocurre un error al ejecutar la consulta.
                 */
                String sqlInsertLlibre = "INSERT INTO llibres (titol, isbn, any_publicacio) VALUES (?, ?, ?)";
                PreparedStatement stmtLlibre = connection.prepareStatement(sqlInsertLlibre, PreparedStatement.RETURN_GENERATED_KEYS);
                stmtLlibre.setString(1, titol);
                stmtLlibre.setString(2, isbn);
                stmtLlibre.setInt(3, Integer.parseInt(anyPublicacio));
                int rowsInserted = stmtLlibre.executeUpdate();

                // Obtener el ID del libro recién insertado
                int idLlibre = -1;
                ResultSet generatedKeys = stmtLlibre.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idLlibre = generatedKeys.getInt(1);
                }
                stmtLlibre.close();

                if (idLlibre > 0) {
                    out.println("<div class='alert alert-success' role='alert'>");
                    out.println("<h2>✅ Libro insertado correctamente</h2>");
                    out.println("<p><strong>ID del libro:</strong> " + idLlibre + "</p>");
                    out.println("</div>");

                    // Inserta los autores y establece relaciones con el libro
                    for (String nomAutor : autors) {
                        /**
                         * Verifica si el autor ya existe en la tabla `autors`.
                         * Si no existe, lo inserta.
                         *
                         * @throws SQLException si ocurre un error al ejecutar la consulta.
                         */
                        int idAutor = -1;
                        String sqlSelectAutor = "SELECT id FROM autors WHERE nom = ?";
                        PreparedStatement stmtSelectAutor = connection.prepareStatement(sqlSelectAutor);
                        stmtSelectAutor.setString(1, nomAutor);
                        ResultSet rsAutor = stmtSelectAutor.executeQuery();

                        if (rsAutor.next()) {
                            idAutor = rsAutor.getInt("id");
                        } else {
                            String sqlInsertAutor = "INSERT INTO autors (nom) VALUES (?)";
                            PreparedStatement stmtInsertAutor = connection.prepareStatement(sqlInsertAutor, PreparedStatement.RETURN_GENERATED_KEYS);
                            stmtInsertAutor.setString(1, nomAutor);
                            stmtInsertAutor.executeUpdate();
                            ResultSet rsInsertedAutor = stmtInsertAutor.getGeneratedKeys();
                            if (rsInsertedAutor.next()) {
                                idAutor = rsInsertedAutor.getInt(1);
                            }
                            stmtInsertAutor.close();
                        }
                        stmtSelectAutor.close();

                        /**
                         * Establece la relación entre el libro y el autor en la tabla `llibre_autor`.
                         *
                         * @throws SQLException si ocurre un error al ejecutar la consulta.
                         */
                        if (idAutor > 0) {
                            String sqlInsertLlibreAutor = "INSERT INTO llibre_autor (id_llibre, id_autor) VALUES (?, ?)";
                            PreparedStatement stmtLlibreAutor = connection.prepareStatement(sqlInsertLlibreAutor);
                            stmtLlibreAutor.setInt(1, idLlibre);
                            stmtLlibreAutor.setInt(2, idAutor);
                            stmtLlibreAutor.executeUpdate();
                            stmtLlibreAutor.close();
                        }
                    }
                } else {
                    out.println("<div class='alert alert-danger' role='alert'>");
                    out.println("<h2>❌ Error al insertar el libro</h2>");
                    out.println("</div>");
                }

            } catch (SQLException e) {
                /**
                 * Maneja errores relacionados con la base de datos.
                 *
                 * @param e la excepción {@link SQLException} que contiene detalles del error.
                 */
                e.printStackTrace();
                out.println("<div class='alert alert-danger' role='alert'>");
                out.println("<p>❌ Error en la inserción en la base de datos: " + e.getMessage() + "</p>");
                out.println("</div>");
            }

            // Botón para regresar a la lista de libros
            out.println("<a href='llibreria.jsp' class='btn btn-primary mt-3'>🔙 Volver a la lista de libros</a>");

            // Fin del contenedor y del cuerpo del HTML
            out.println("</div>");
            out.println("<script src='https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js'></script>");
            out.println("</body>");
            out.println("</html>");

        } catch (Exception e) {
            /**
             * Maneja errores generales del servlet.
             *
             * @param e la excepción {@link Exception} que contiene detalles del error.
             */
            e.printStackTrace();
            response.getWriter().println("<p>Error general: " + e.getMessage() + "</p>");
        }
    }
}
