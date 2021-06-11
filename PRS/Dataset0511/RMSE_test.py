import numpy as np
import pandas as pd
from surprise import Reader, Dataset, accuracy
from surprise.model_selection import train_test_split, GridSearchCV
from surprise.prediction_algorithms.matrix_factorization import SVD

r_cols=['recipe_id', 'user_id', 'rating']
ratings= pd.read_csv('C:/RecoSys/rating.csv',names=r_cols, header=None, encoding='UTF-8')

#https://nicola-ml.tistory.com/77
#https://studychfhd.tistory.com/227

traindata=Dataset.load_from_df(ratings, Reader())
print(type(traindata))
param_grid = {'n_epochs': [26,28], 'n_factors': [10,20,40,70,110]}
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3)
gs.fit(traindata)
print(gs.best_score['rmse'])
pr=gs.best_params['rmse']
print('n_epochs : ', pr['n_epochs'],' / ','n_factors : ',pr['n_factors'])

pred=gs.predict(['90050185'])
print(pred)