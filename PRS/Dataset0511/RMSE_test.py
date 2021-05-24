import numpy as np
import pandas as pd
from surprise import Reader, Dataset, accuracy
from surprise.model_selection import train_test_split, GridSearchCV
from surprise.prediction_algorithms.matrix_factorization import SVD

r_cols=['recipe_id', 'user_id', 'rating']
ratings= pd.read_csv('C:/RecoSys/rating.csv',names=r_cols, header=None, encoding='UTF-8')

data=Dataset.load_from_df(ratings, Reader())

#reader = Reader(line_format='user item rating', sep = ',')
#Data = Dataset.load_from_file('C:/RecoSys/rating.csv', reader=reader)

"""
# 최적의 K값 찾기 (책 82p)
# https://nicola-ml.tistory.com/77
results = []
index = []
for K in range(50, 261, 10):
    print('K =', K)
    trainset, testset = train_test_split(data, test_size=0.25, random_state=0)
    algo = SVD(n_epochs=20,n_factors=K, random_state=13)
    algo.fit(trainset)
    predictions = algo.test(testset)
    accuracy.rmse(predictions)
    index.append(K)
    #results.append()
    
print("hi")
"""
#https://nicola-ml.tistory.com/77
#https://studychfhd.tistory.com/227 10,20,50,80
param_grid = {'n_epochs': [26,28], 'n_factors': [10,20,40,70,110]} #3:21
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3)

gs.fit(data)
print(gs.best_score['rmse'])
print(gs.best_params['rmse'])
pr=gs.best_params['rmse']
print(type(pr))
epochs=pr['n_epochs']

"""
# 최적의 iterations 값 찾기
summary = []
for i in range(len(results)):
    RMSE = []
    for result in results[i]:
        RMSE.append(result[2])
    min = np.min(RMSE)
    j = RMSE.index(min)
    summary.append([index[i], j+1, RMSE[j]])

# 그래프 그리기
import matplotlib.pyplot as plt
plt.plot(index, [x[2] for x in summary])
plt.ylim(0.89, 0.94)
plt.xlabel('K')
plt.ylabel('RMSE')
plt.show()

"""



"""


param_grid = { 'n_factors': [50,100,200]}
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3)
gs.fit(data)
print(gs.best_score['rmse'])
print(gs.best_params['rmse'])
"""