import numpy as np
from surprise import SVD, accuracy, Reader, Dataset
from scipy.sparse.linalg import svds
import pandas as pd
from surprise.model_selection import GridSearchCV
import time

s_time=time.time()

recipes=pd.read_csv('C:/RecoSys/new_recipe_tags.csv',header=0, encoding='utf-8')

r_cols=['recipe_id', 'user_id', 'rating']
ratings= pd.read_csv('C:/RecoSys/rating.csv',names=r_cols, header=None, encoding='UTF-8')
# ratings=ratings.set_index('user_id')
user_list=[0]*30000


# get params
traindata=Dataset.load_from_df(ratings, Reader())
param_grid = {'n_epochs': [10,30], 'n_factors': [20,40,80]}
gs = GridSearchCV(SVD, param_grid, measures=['rmse', 'mse'], cv=3)
gs.fit(traindata)
print(gs.best_score['rmse'])
pr=gs.best_params['rmse']
print('n_epochs : ', pr['n_epochs'],' / ','n_factors : ',pr['n_factors'])

idx=0
for i in ratings['user_id']:
        if i not in user_list:
            user_list[idx]=i
            idx=idx+1
print(idx)
user_name='mu0249'        # set target name

df_ratings=ratings.pivot_table(
    values='rating',
    index='user_id',
    columns='recipe_id'
).fillna(0)

mat=df_ratings.to_numpy()
user_mean=np.mean(mat, axis=1)

U, sigma, Vt = svds(df_ratings, k=pr['n_factors'],maxiter=pr['n_epochs'])  # k= 1~ 957

sigma=np.diag(sigma)

svd_user_pred_ratings= np.dot(np.dot(U,sigma),Vt)+user_mean.reshape(-1,1)
df_svd= pd.DataFrame(svd_user_pred_ratings,columns=df_ratings.columns)


def recommend_recipes(df_svd,user_id,recipe_df,ratings_df,rec_num=10):
    user_row_num= user_list.index(user_id)
    sorted_user_pred=df_svd.loc[user_row_num].sort_values(ascending=False)
    user_data=ratings_df[ratings_df.user_id==user_id]
    user_history=user_data.merge(recipe_df, on='recipe_id').sort_values(['rating'],ascending=False)
    recmd=recipe_df[~recipe_df['recipe_id'].isin(user_history['recipe_id'])]
    recmd=recmd.merge(pd.DataFrame(sorted_user_pred).reset_index(), on='recipe_id')
    recmd=recmd.rename(columns={user_row_num: 'Predictions'}).sort_values('Predictions',ascending=False).iloc[:rec_num, :]

    return user_history, recmd

already_rated,predictions= recommend_recipes(df_svd,user_name,recipes,ratings,7)

print(already_rated)
print(predictions)

print("time :", time.time() - s_time)