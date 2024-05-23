FROM python:3.11.8-slim

WORKDIR /prediction-container


COPY . .
COPY model_RF.pkl /models/model_RF.pkl
COPY model_RF_columns.pkl /models/model_RF_columns.pkl
COPY requirements.txt .


RUN pip install --no-cache-dir -r requirements.txt

EXPOSE 5000

CMD ["python", "prediction_msv.py"]
