$(document).ready(function() {
  $('#routine-form').on('submit', function(e) {
    e.preventDefault(); // Evita el envío normal del formulario

    // Debugging: Imprimir el valor de cada campo antes de procesarlo
    console.log("Gender raw value:", $('#Gender').val());
    console.log("Age raw value:", $('input[name="Age"]').val());
    console.log("Weight raw value:", $('input[name="Weight"]').val());
    console.log("FAF raw value:", $('#FAF').val());
    console.log("Routine ID raw value:", $('input[name="rutinaID"]:checked').val());

    var genderVal = $('#Gender').val();
    var FAFVal = $('#FAF').val();

    // Recoge y verifica los datos del formulario
    var formData = {
      Gender: genderVal ? parseInt(genderVal, 10) : null,
      Age: $('input[name="Age"]').val() ? parseInt($('input[name="Age"]').val(), 10) : null,
      Weight: $('input[name="Weight"]').val() ? parseFloat($('input[name="Weight"]').val()) : null,
      FAF: FAFVal ? parseInt(FAFVal, 10) : null,
      rutinaID: $('input[name="rutinaID"]:checked').val(),
      Valoracion: 2 // Valoración fija como mencionaste
    };

    // Debugging: Imprimir el objeto formData completo
    console.log("formData:", formData);

    // Realiza una solicitud AJAX al endpoint de Java
    $.ajax({
      url: '/javaprediction', // Asegúrate de que esta es la URL correcta
      type: 'POST',
      contentType: 'application/json',
      data: JSON.stringify(formData),
      success: function(response) {
        console.log("Response from server: ", response);
    var data = typeof response === 'string' ? JSON.parse(response) : response;
    displayResults(data); // dentro del bloque success

      },
      error: function(xhr, status, error) {
        console.error("Error: ", error);
      }
    });
  });
 

function displayResults(data) {
  var resultsList = $('#results-list');
  resultsList.empty(); // Limpia los resultados anteriores

  if (Array.isArray(data)) {
      let delay = 0; // Inicializa el retraso para la animación de cada resultado
      data.forEach(function(item) {
          setTimeout(function() {
              var listItem = $(`<li class="content__container__list__item" style="display:none;">${item.exercise_name} <span>[${item.predicted_weight.toFixed(2)} kg]</span></li>`);
              resultsList.append(listItem);
              listItem.fadeIn(600); // Hace que cada ítem aparezca con un fade
          }, delay);
          delay += 1000; // Incrementa el retraso para que el próximo resultado se muestre 1 segundo después
      });

      // Solo inicia el scroll una vez que se haya empezado a mostrar el primer ítem
      $('html, body').animate({
          scrollTop: $('.content').offset().top
      }, 1000);
  } else {
      console.log("Received data is not an array, or an unexpected format:", data);
      $('.content').fadeOut(200);
  }
}





});






