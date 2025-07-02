<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%-- 
    Aquí es donde, en el siguiente paso, pondremos la lógica Java 
    que actualmente tienes en tu servlet "capturarDatos".
    Por ahora, lo dejamos vacío para enfocarnos en la migración del HTML.
--%>

<!DOCTYPE html>
<html lang="es">

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="estilosLogin.css">
    <title>Inicio de sesión</title>
</head>

<body>
    <%-- El action del formulario ahora apunta a este mismo archivo JSP --%>
    <form action="login.jsp" method="post">
        <label for="" align="center" style="font-size: 20px;">Inicio de Sesión</label><br>
        <label for="nombre">Nombre de usuario:</label>
        <input type="text" id="nombre" placeholder="NombreUsuario.12" name="nombre" required>
        <label for="contraseña">Contraseña:</label>
        <input type="password" placeholder="Usuario123" id="clave" name="clave" required>
        <input type="submit" value="Iniciar sesión" name="iniciar">
        <input type="button" value="Limpiar" onclick="limpiarCampos()">
    </form>
    
    <%-- El código JavaScript no necesita ningún cambio. Funciona igual. --%>
    <script>
        function limpiarCampos() {
            document.getElementById("nombre").value = "";
            // Corrección: El id del campo de contraseña es "clave", no "contraseña".
            document.getElementById("clave").value = ""; 
        }
    </script>
</body>

</html>