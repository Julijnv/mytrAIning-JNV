from flask import Flask, json, request, jsonify
import pandas as pd
import joblib

app = Flask(__name__)

# Cargar el modelo y las columnas del modelo
print("Cargando modelo...")
model = joblib.load('model_RF.pkl')
print("Modelo cargado con éxito.")
print("Cargando columnas del modelo...")
model_columns = joblib.load('model_RF_columns.pkl')
print("Columnas del modelo cargadas con éxito.")

# Diccionario de Rutinas y Ejercicios
rutinas = {
    "0": {
        "nombre": "Rutina Full Body",
        "ejercicios": {
            "0.1": "Sentadilla",
            "0.2": "Press de banca",
            "0.3": "Peso muerto",
            "0.4": "Press militar",
            "0.5": "Curl de bíceps con barra",
            "0.6": "Press francés con barra"
        }
    },
    "1": {
        "nombre": "Rutina Espalda",
        "ejercicios": {
            "1.1": "Remo con barra",
            "1.2": "Peso muerto",
            "1.3": "Good mornings"
        }
    },
    "2": {
        "nombre": "Rutina Pecho",
        "ejercicios": {
            "2.1": "Press de banca inclinado con barra",
            "2.2": "Press de banca",
            "2.3": "Pullover con barra"
        }
    },
    "3": {
        "nombre": "Rutina Hombros",
        "ejercicios": {
            "3.1": "Press militar",
            "3.2": "Remo al cuello con barra",
            "3.3": "Press de banca con agarre cerrado"
        }
    },
    "4": {
        "nombre": "Rutina Brazos",
        "ejercicios": {
            "4.1": "Curl de bíceps con barra",
            "4.2": "Curl de bíceps con agarre inverso",
            "4.3": "Press francés con barra"
        }
    },
    "5": {
        "nombre": "Rutina Pierna",
        "ejercicios": {
            "5.1": "Sentadilla",
            "5.2": "Peso muerto",
            "5.3": "Prensa de piernas"
        }
    }
}

@app.route('/predict', methods=['POST'])
def predict():
    data = request.get_json(force=True)
    print("Datos recibidos:", data)

    rutina_id = data['rutinaID']
    ejercicios_rutina = rutinas.get(rutina_id, {}).get("ejercicios", {})
    print("Ejercicios de la rutina:", ejercicios_rutina)

    predictions = []
    for ejercicio_id, ejercicio_nombre in ejercicios_rutina.items():
        print("Prediciendo para el ejercicio:", ejercicio_nombre)
        input_data = pd.DataFrame({key: [value] for key, value in data.items() if key != 'Ejercicio_ID'})
        input_data['Ejercicio_ID'] = ejercicio_id 
        print("Datos de entrada para el ejercicio:", input_data)
        
        # Asegura que el DataFrame tenga las mismas columnas que el modelo
        for col in model_columns:
            if col not in input_data.columns:
                input_data[col] = 0
        input_data = input_data.reindex(columns=model_columns, fill_value=0)
        print("Datos de entrada después de reindexar:", input_data)
        
        # Predecir el peso para el ejercicio
        predicted_weight = model.predict(input_data)[0]
        print("Peso predicho para el ejercicio:", predicted_weight)
        predictions.append({
            'rutina_id': rutina_id,
            'exercise_id': ejercicio_id,
            'exercise_name': ejercicio_nombre,
            'predicted_weight': predicted_weight
        })

    return jsonify(predictions)

if __name__ == '__main__':
    app.run(debug=True, host='0.0.0.0')
 