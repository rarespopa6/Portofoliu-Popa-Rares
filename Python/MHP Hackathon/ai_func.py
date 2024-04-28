import pandas as pd
import numpy as np
from sklearn.model_selection import train_test_split
from sklearn.linear_model import LogisticRegression


def room_oc_prob(name: str,data_pred: str, time_period: str,attendance_per: str):
    data = pd.read_csv('meeting-rooms.csv',usecols=['room', 'date',time_period,attendance_per],
                       dtype={'room':str,'date':str,time_period:str,attendance_per:str })
    date_filtered=data[data['room'] == name]
    date_numerice = pd.to_datetime(date_filtered['date'], format='%d/%m/%Y').dt.dayofyear.values.reshape(-1, 1)
    etichete = date_filtered[time_period]

    X1_train, X1_test, y1_train, y1_test = train_test_split(date_numerice, etichete, test_size=0.2, random_state=42)

    model = LogisticRegression()
    model.fit(X1_train, y1_train)

    ziua_din_an = pd.to_datetime(data_pred, format='%d/%m/%Y').dayofyear
    data_pred = np.array([[ziua_din_an]])
    sansa_sala_liber = model.predict_proba(data_pred)[:, 0][0]

    return round((sansa_sala_liber*100), 2)


def table_oc_prob(name: str,data_pred: str, time_period: str):
    data = pd.read_csv('hackathon-schema.csv',usecols=['desk','date',time_period],
                       dtype={'desk':str,'date':str,time_period:str})
    date_filtered=data[data['desk'] == name]

    date_numerice = pd.to_datetime(date_filtered['date'], format='%d/%m/%Y').dt.dayofyear.values.reshape(-1, 1)
    etichete = date_filtered[time_period]
    X_train, X_test, y_train, y_test = train_test_split(date_numerice, etichete, test_size=0.2, random_state=42)

    model = LogisticRegression()
    model.fit(X_train, y_train)

    ziua_din_an = pd.to_datetime(data_pred, format='%d/%m/%Y').dayofyear
    data_pred = np.array([[ziua_din_an]])
    sansa_masa_libera = model.predict_proba(data_pred)[:, 0][0]

    return round((sansa_masa_libera*100),2)
