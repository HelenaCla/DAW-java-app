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
        // Obtener el parámetro ID de la URL
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

        // Conexión a la base de datos para recuperar la información actual del libro
        Connection connection = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            connection = Connexio.getConnection();
            String sql = "SELECT * FROM llibres WHERE id = ?";
            pstmt = connection.prepareStatement(sql);
            pstmt.setInt(1, Integer.parseInt(id));
            rs = pstmt.executeQuery();

            if (rs.next()) {
    %>

    <form action="editarLlibre" method="post" class="bg-light p-4 rounded shadow-sm">
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
