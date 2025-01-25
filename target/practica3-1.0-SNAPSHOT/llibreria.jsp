<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ page import="java.sql.*" %>
<%@ page import="com.practica3.Connexio" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Llibres Disponibles</title>

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css2?family=Roboto:wght@400;500;700&display=swap" rel="stylesheet">

    <style>
        /* Estilos de la página */
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        /* Resto de los estilos omitidos para brevedad */
    </style>
</head>
<body>

<div class="container">
    <h1>📚 Llibres Disponibles</h1>

    <!-- Botón para añadir un nuevo libro -->
    <a href="formAñadir.jsp" class="btn-add-book">➕ Añadir libro</a>

    <table>
        <thead>
            <tr>
                <th>ID</th>
                <th>Títol</th>
                <th>Autor</th>
                <th>ISBN</th>
                <th>Any</th>
                <th>Accions</th>
            </tr>
        </thead>
        <tbody>
            <%
                // Variables para la conexión a la base de datos
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;

                try {
                    /**
                     * Establece la conexión con la base de datos utilizando Connexio.
                     * Ejecuta una consulta SQL para recuperar la lista de libros disponibles
                     * junto con sus autores, utilizando un JOIN entre las tablas llibres, llibre_autor y autors.
                     *
                     * @throws SQLException si ocurre un error en la consulta o la conexión.
                     */
                    conn = Connexio.getConnection();
                    stmt = conn.createStatement();

                    String consulta = "SELECT l.id, l.titol, l.isbn, l.any_publicacio, " +
                                      "GROUP_CONCAT(a.nom SEPARATOR ', ') AS autor " +
                                      "FROM llibres l " +
                                      "LEFT JOIN llibre_autor la ON l.id = la.id_llibre " +
                                      "LEFT JOIN autors a ON la.id_autor = a.id " +
                                      "GROUP BY l.id, l.titol, l.isbn, l.any_publicacio";

                    rs = stmt.executeQuery(consulta);

                    // Itera sobre los resultados de la consulta
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String titol = rs.getString("titol");
                        String autor = rs.getString("autor") != null ? rs.getString("autor") : "Autor desconegut";
                        String isbn = rs.getString("isbn");
                        int any = rs.getInt("any_publicacio");
            %>
                        <tr>
                            <td><%= id %></td>
                            <td><%= titol %></td>
                            <td><%= autor %></td>
                            <td><%= isbn %></td>
                            <td><%= any %></td>
                            <td>
                                <!-- Botón para editar el libro -->
                                <form action="editarLlibres.jsp" method="get" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= id %>">
                                    <button type="submit" class="btn btn-edit">✏️ Editar</button>
                                </form>

                                <!-- Botón para eliminar el libro -->
                                <form action="eliminarLlibre" method="post" style="display:inline;">
                                    <input type="hidden" name="isbn" value="<%= isbn %>">
                                    <button type="submit" class="btn btn-delete">🗑️ Eliminar</button>
                                </form>
                            </td>
                        </tr>
            <%
                    }

                } catch (SQLException e) {
                    /**
                     * Maneja errores de la conexión o consulta SQL.
                     *
                     * @param e la excepción SQLException que contiene el detalle del error.
                     */
                    out.println("<tr><td colspan='6' class='alert-error'><span>❌ Error:</span> " + e.getMessage() + "</td></tr>");
                } finally {
                    /**
                     * Cierra los recursos utilizados (ResultSet, Statement, Connection).
                     * 
                     * @throws SQLException si ocurre un error al cerrar los recursos.
                     */
                    try {
                        if (rs != null) rs.close();
                        if (stmt != null) stmt.close();
                        if (conn != null) conn.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    }
                }
            %>
        </tbody>
    </table>
</div>

</body>
</html>
