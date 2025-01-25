<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Insertar Llibre</title>
    <!-- Enlace a Bootstrap 5 -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
</head>
<body class="bg-light">
    <div class="container mt-5">
        <h1 class="text-center mb-4">Insertar Llibre</h1>
        
        <div class="card shadow-sm">
            <div class="card-body">
                <form action="insertarLlibre" method="post">
                    
                    <div class="mb-3">
                        <label for="titol" class="form-label">Títol</label>
                        <input type="text" id="titol" name="titol" class="form-control" placeholder="Introduce el título del libro" required>
                    </div>

                    <div class="mb-3">
                        <label for="isbn" class="form-label">ISBN</label>
                        <input type="text" id="isbn" name="isbn" class="form-control" placeholder="978-1234567890" required>
                    </div>

                    <div class="mb-3">
                        <label for="any_publicacio" class="form-label">Any de publicació</label>
                        <input type="number" id="any_publicacio" name="any_publicacio" class="form-control" placeholder="2024" min="0" required>
                    </div>

                    <div class="mb-3">
                        <label for="autors" class="form-label">Autors</label>
                        <input type="text" id="autors" name="autors" class="form-control" placeholder="Autor1, Autor2, Autor3" required>
                        <div class="form-text">Introduce los autores separados por comas.</div>
                    </div>

                    <div class="d-grid gap-2 d-md-flex justify-content-md-end">
                        <a href="llibreria.jsp" class="btn btn-secondary">Cancelar</a>
                        <button type="submit" class="btn btn-primary">Insertar Llibre</button>
                    </div>

                </form>
            </div>
        </div>
    </div>

    <!-- Enlace a Bootstrap 5 JS (opcional) -->
    <script src="https://cdn.jsdelivr.net/npm/@popperjs/core@2.11.8/dist/umd/popper.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.min.js"></script>
</body>
</html>
