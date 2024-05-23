$(document).ready(function () {
    $('#registro-form').on('submit', function (e) {
        e.preventDefault();

        var datosForm = {
            email: $('input[name="email"]').val(),
            nombre: $('input[name="nombre"]').val(),
            password: $('input[name="password"]').val(),
            rol: 'user' // Este valor lo debes ajustar según tu lógica de roles
        };

        $.ajax({
            type: 'POST',
            url: '/register',
            contentType: 'application/json',
            data: JSON.stringify(datosForm),
            success: function (data) {
                console.log('Registro exitoso:', data);
                window.location.href = '/login'; // Redirigir al usuario o mostrar un mensaje de éxito
            },
            error: function (error) {
                console.log('Error en el registro:', error);
                // Mostrar mensaje de error al usuario
            }
        });
    });
});
