(function ($) {
    "use strict";



    /*==================================================================
    [ Focus input ]*/
    $('.input100').each(function(){
        $(this).on('blur', function(){
            if($(this).val().trim() != "") {
                $(this).addClass('has-val');
            }
            else {
                $(this).removeClass('has-val');
            }
        })    
    });
  
    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit', function(e){
        e.preventDefault();
        var check = true;

        for(var i=0; i<input.length; i++) {
            if(!validate(input[i])){
                showValidate(input[i]);
                check = false;
            }
        }

        if(check){
            var email = $('input[name="email"]').val().trim();
            var password = $('input[name="password"]').val().trim();

            $.ajax({
                url: '/login',
                type: 'POST',
                contentType: 'application/json',
                data: JSON.stringify({ email: email, password: password }),
                success: function() {
                    // Si el inicio de sesión es exitoso, puedes redirigir al usuario a la página de inicio
                    window.location.href = '/home';
                },
                error: function(xhr) {
                    // Manejo de errores aquí
                    alert('Error logging in: ' + xhr.responseJSON.mensaje);
                }
            });
        }
        return false;
    });

    $('.validate-form .input100').each(function(){
        $(this).focus(function(){
           hideValidate(this);
        });
    });

    function validate (input) {
        // Validación específica para el correo electrónico
        if($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            return $(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{2,})$/) != null;
        }
        // Validación para el nombre de usuario, que puede ser un texto normal
        else if ($(input).attr('type') == 'text' || $(input).attr('name') == 'username') {
            return $(input).val().trim() !== '';
        }
        // Añade aquí otras validaciones si fuera necesario
        else {
            // Si el input no es ni email ni username, valida que no esté vacío
            return $(input).val().trim() !== '';
        }
    }
    

    function showValidate(input) {
        var thisAlert = $(input).parent();
        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();
        $(thisAlert).removeClass('alert-validate');
    }
    
    /*==================================================================
    [ Show pass ]*/
    var showPass = 0;
    $('.btn-show-pass').on('click', function(){
        if(showPass == 0) {
            $(this).next('input').attr('type','text');
            $(this).addClass('active');
            showPass = 1;
        }
        else {
            $(this).next('input').attr('type','password');
            $(this).removeClass('active');
            showPass = 0;
        }
        
    });
})(jQuery);
