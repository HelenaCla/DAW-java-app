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
        body {
            font-family: 'Roboto', sans-serif;
            background-color: #f4f6f9;
            margin: 0;
            padding: 0;
        }

        h1 {
            text-align: center;
            color: #333;
            padding: 20px 0;
        }

        .container {
            width: 90%;
            max-width: 1200px;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0px 4px 6px rgba(0,0,0,0.1);
        }

        .btn-add-book {
            display: inline-block;
            background-color: #4CAF50;
            color: #fff;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            font-weight: bold;
            text-decoration: none;
            margin-bottom: 20px;
            transition: all 0.3s ease;
            float: right;
        }

        .btn-add-book:hover {
            background-color: #45a049;
            cursor: pointer;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            border-radius: 10px;
            overflow: hidden;
        }

        th, td {
            padding: 12px;
            text-align: center;
        }

        th {
            background-color: #4A90E2;
            color: white;
            font-weight: bold;
        }

        tr:nth-child(even) {
            background-color: #f2f2f2;
        }

        tr:nth-child(odd) {
            background-color: #ffffff;
        }

        tr:hover {
            background-color: #e0e0e0;
        }

        .btn {
            padding: 8px 12px;
            font-size: 14px;
            border-radius: 5px;
            border: none;
            cursor: pointer;
            transition: all 0.3s ease;
        }

        .btn-edit {
            background-color: #4CAF50;
            color: white;
        }

        .btn-edit:hover {
            background-color: #45a049;
        }

        .btn-delete {
            background-color: #f44336;
            color: white;
        }

        .btn-delete:hover {
            background-color: #e53935;
        }

        .alert-error {
            padding: 15px;
            background-color: #f44336;
            color: white;
            text-align: center;
            border-radius: 5px;
            margin-bottom: 20px;
        }

        .alert-error span {
            font-weight: bold;
        }
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
                Connection conn = null;
                Statement stmt = null;
                ResultSet rs = null;

                try {
                    conn = Connexio.getConnection();
                    stmt = conn.createStatement();

                    String consulta = "SELECT l.id, l.titol, l.isbn, l.any_publicacio, " +
                                      "GROUP_CONCAT(a.nom SEPARATOR ', ') AS autor " +
                                      "FROM llibres l " +
                                      "LEFT JOIN llibre_autor la ON l.id = la.id_llibre " +
                                      "LEFT JOIN autors a ON la.id_autor = a.id " +
                                      "GROUP BY l.id, l.titol, l.isbn, l.any_publicacio";

                    rs = stmt.executeQuery(consulta);

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
                                <form action="editarLlibres.jsp" method="get" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= id %>">
                                    <button type="submit" class="btn btn-edit">✏️ Editar</button>
                                </form>

                                <form action="eliminarLlibre" method="post" style="display:inline;">
                                    <input type="hidden" name="isbn" value="<%= isbn %>">
                                    <button type="submit" class="btn btn-delete">🗑️ Eliminar</button>
                                </form>
                            </td>
                        </tr>
            <%
                    }

                } catch (SQLException e) {
                    out.println("<tr><td colspan='6' class='alert-error'><span>❌ Error:</span> " + e.getMessage() + "</td></tr>");
                } finally {
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
