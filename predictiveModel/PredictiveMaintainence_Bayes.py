#!/usr/bin/env python
# coding: utf-8

# In[13]:


import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import time
from sklearn.model_selection import train_test_split
from sklearn.naive_bayes import GaussianNB, BernoulliNB, MultinomialNB
import sys
if sys.version_info[0] < 3: 
    from StringIO import StringIO
else:
    from io import StringIO
    
from sklearn.metrics import mean_squared_error


# In[14]:


# data = pd.read_csv('../data/container_matrix_test.csv', delimiter=",")
# data.head()


DATA="""Timestamp, ID, Temperature(celsius), Target_Temperature(celsius), Amp, CumulativePowerConsumption, ContentType, Humidity, CO2, Door_Open, Maintainence_Required, Defrost_Cycle"""+"\n"+sys.argv[1]
    
print 'DATA: ', DATA
TESTDATA = StringIO(DATA)
#data = '2019-04-01 T16:29 Z,1813,4.291843460900875,4.4,2.870278314651876,10.273342381017777,3,4334.920958996634,4.9631508046318755,1,0,6'
#data = pd.read_csv('../data/container_matrix_test.csv', delimiter=",")
data = pd.read_csv(TESTDATA, sep=",")
data.columns = data.columns.to_series().apply(lambda x: x.strip())
data.head()


# In[15]:


# create a Python list of feature names
feature_cols = ['Temperature(celsius)', 'Door_Open','Target_Temperature(celsius)','Amp','ContentType','CumulativePowerConsumption'
                          ,'Humidity','CO2','Defrost_Cycle']

# use the list to select a subset of the original DataFrame
X = data[feature_cols]
y = data['Maintainence_Required']

# print the first 5 rows
X.head()


# In[16]:


import pickle

#loading a model from a file called model.pkl
model = pickle.load(open("model_bays.pkl","r"))


# In[17]:


# make predictions on the input set
y_pred = model.predict(X)
print "prediction: ", y_pred
#y_pred.shape
#print(np.sqrt(metrics.mean_squared_error(y, y_pred)))
print "rms error: ", (np.sqrt(mean_squared_error(y, y_pred)))


# In[ ]:




