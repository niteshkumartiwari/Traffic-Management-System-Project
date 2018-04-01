import pandas as pd
import numpy as np
import datetime

df=pd.read_csv('dataset.csv')
df.loc[len(df)]=[datetime.datetime.now(),23,20,20,20,20,20,20,20,20,20,20,20,20,20,20]
df.to_csv('dataset.csv')
