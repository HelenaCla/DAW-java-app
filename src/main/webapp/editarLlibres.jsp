<%@page import="com.practica3.Connexio"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.sql.*" %>

<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Editar Llibre</title>
    <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css">
</head>
<body>

<div class="container mt-5">
    <h1 class="text-center mb-4">Editar Libro</h1>

    <% 
        /**
         * Recupera el parámetro ID del libro desde la URL.
         * Si no se proporciona un ID válido, muestra un mensaje de error.
         *
         * @param id el identificador del libro recibido como parámetro en la URL.
         * @return Si el ID no está presente o es inválido, se redirige al usuario
         *         a la página principal de la lista de libros.
         */
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
    %>
        <div class="alert alert-danger" role="alert">
            <h3>Error: No se ha recibido el ID del libro.</h3>
        </div>
        <a href="llibreria.jsp" class="btn btn-primary mt-3">🔙 Volver a la lista de libros</a>
    <%
        return;
        }

        // Declaración de objetos para la conexión a la base de datos
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            /**
             * Conecta con la base de datos utilizando la clase Connexio.
             * Recupera los detalles del libro correspondiente al ID proporcionado.
             *
             * @throws SQLException si ocurre algún problema con la conexión o la consulta SQL.
             */
            connection = Connexio.getConnection();
            String sql = "SELECT * FROM llibres WHERE id = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(id));
            rs = pstmt.executeQuery();

            if (rs.next()) {
    %>

    <form action="editarLlibre" method="post" class="bg-light p-4 rounded shadow-sm">
        <!-- Enviar el ID del libro como un campo oculto -->
        <input type="hidden" name="id" value="<%= rs.getInt("id") %>">

        <div class="mb-3">
            <label for="titol" class="form-label"><strong>Título:</strong></label>
            <input type="text" name="titol" id="titol" class="form-control" value="<%= rs.getString("titol") %>" required>
        </div>

        <div class="mb-3">
            <label for="isbn" class="form-label"><strong>ISBN:</strong></label>
            <input type="text" name="isbn" id="isbn" class="form-control" value="<%= rs.getString("isbn") %>" required>
        </div>

        <div class="mb-3">
            <label for="any_publicacio" class="form-label"><strong>Año de publicación:</strong></label>
            <input type="number" name="any_publicacio" id="any_publicacio" class="form-control" value="<%= rs.getInt("any_publicacio") %>" required>
        </div>

        <div class="mb-3">
            <label for="id_editorial" class="form-label"><strong>ID de la Editorial:</strong></label>
            <input type="text" name="id_editorial" id="id_editorial" class="form-control" value="<%= rs.getInt("id_editorial") %>">
        </div>

        <div class="d-flex justify-content-between">
            <button type="submit" class="btn btn-success">💾 Guardar Cambios</button>
            <a href="llibreria.jsp" class="btn btn-secondary">🔙 Volver</a>
        </div>
    </form>

    <%
            } else {
    %>
        <div class="alert alert-warning" role="alert">
            <h3>Error: No se encontró ningún libro con ese ID.</h3>
        </div>
        <a href="llibreria.jsp" class="btn btn-primary mt-3">🔙 Volver a la lista de libros</a>
    <%
            }
        } catch (SQLException e) {
    %>
        <div class="alert alert-danger" role="alert">
            <h3>Error al recuperar el libro: <%= e.getMessage() %></h3>
        </div>
    <%
            e.printStackTrace();
        } finally {
            /**
             * Libera los recursos asociados con la conexión, el PreparedStatement y el ResultSet.
             * 
             * @throws SQLException si ocurre un error al cerrar los recursos.
             */
            try {
                if (rs != null) rs.close();
                if (pstmt != null) pstmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    %>

</div>

<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
