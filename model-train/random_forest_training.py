import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.ensemble import RandomForestRegressor
from sklearn.metrics import mean_squared_error
from math import sqrt
import joblib

# Carga del dataset
df = pd.read_csv('Path al dataset')

df['Ejercicio_ID'] = df['Ejercicio_ID'].astype(float)


features = ['Gender', 'Age', 'Weight', 'FAF', 'Ejercicio_ID', 'Valoracion']
X = df[features]
y = df['Peso Ejercicio']

X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
rf = RandomForestRegressor(n_estimators=100, random_state=42)
rf.fit(X_train, y_train)


joblib.dump(X_train.columns, 'model_columns.pkl')
joblib.dump(rf, 'modelo_entrenado.pkl')

 
