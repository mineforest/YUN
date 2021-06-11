from collections import defaultdict

from surprise import SVD, Reader, Dataset
import pandas as pd
from surprise.model_selection import GridSearchCV, train_test_split

recipes=pd.read_csv('C:/RecoSys/recipe_tags.csv',header=0, encoding='utf-8')

r_cols=['recipe_id', 'user_id', 'rating']
ratings= pd.read_csv('C:/RecoSys/rating.csv',names=r_cols, header=None, encoding='UTF-8')
# ratings=ratings.set_index('user_id')

# get params
traindata=Dataset.load_from_df(ratings, Reader())
param_grid = {'n_epochs': [10,20,50], 'n_factors': [1,10]}
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3,refit=True)
print("hi")
gs.fit(traindata)
print(gs.best_score['rmse'])
pr=gs.best_params['rmse']
print('n_epochs : ', pr['n_epochs'],' / ','n_factors : ',pr['n_factors'])

#https://www.fun-coding.org/recommend_basic7.html
def get_top_n(preds,n=10):
    top_n=defaultdict(list)
    for uid, iid, true_r, est, _ in preds:
        top_n[iid].append((uid,est))

    for uid, user_ratings in top_n.items():
        user_ratings.sort(key=lambda x: x[1], reverse=True)
        top_n[uid]=user_ratings[:n]

    return top_n

trainset=traindata.build_full_trainset()
algo=SVD(n_factors=pr['n_factors'], n_epochs=pr['n_epochs'])
algo.fit(trainset)
testset=trainset.build_testset()

preds=algo.test(testset)
print(preds[100])

top_n=get_top_n(preds,n=10)

for uid, user_ratings in top_n.items():
    print(uid,[iid for (iid,_) in user_ratings])
""""""