import numpy as np
from surprise import SVD
import scipy as sp
from scipy.sparse.linalg import svds
import pandas as pd

i_cols=['recipe_id','title','unknown','kor','west','chi','jap']
recipes=pd.read_csv('C:/RecoSys/Dataset0501/recipe.csv',names=i_cols,header=None,sep='|',encoding='utf-8')
recipes.set_index('recipe_id')
recipes.head()

r_cols=['user_id', 'recipe_id', 'rating']
ratings= pd.read_csv('C:/RecoSys/Dataset0501/ratings2.csv',names=r_cols, header=None, sep='|', encoding='UTF-8')
#ratings=ratings.set_index('user_id')
ratings.head()

#print(ratings.)
#print(ratings[ratings.user_id==1])
#print(ratings.values)



df_ratings=ratings.pivot_table(
    values='rating',
    index='user_id',
    columns='recipe_id'
).fillna(0)
df_ratings.head()

mat=df_ratings.to_numpy()
user_mean=np.mean(mat,axis=1)
mat_user_mean=mat-user_mean.reshape(-1,1)
pd.DataFrame(mat_user_mean,columns=df_ratings.columns).head()



U, sigma,Vt=svds(mat_user_mean, k=5)    # 차원 축소값 k=1~9


sigma=np.diag(sigma)
#print(sigma.shape)
#sigma.shape

svd_user_pred_ratings=np.dot(np.dot(U,sigma),Vt)+user_mean.reshape(-1,1)
df_svd=pd.DataFrame(svd_user_pred_ratings,columns=df_ratings.columns)
df_svd.head()


def recommend_recipes(df_svd,user_id,recipe_df,ratings_df,rec_num=5):
    user_row_num=user_id-1
    sorted_user_pred=df_svd.iloc[user_row_num].sort_values(ascending=False)
    user_data=ratings_df[ratings_df.user_id==user_id]
    user_history=user_data.merge(recipe_df,on='recipe_id').sort_values(['rating'],ascending=False)
    recmd=recipe_df[~recipe_df['recipe_id'].isin(user_history['recipe_id'])]
    recmd=recmd.merge(pd.DataFrame(sorted_user_pred).reset_index(), on='recipe_id')
    recmd=recmd.rename(columns={user_row_num: 'Predictions'}).sort_values('Predictions',ascending=False).iloc[:rec_num, :]

    return user_history, recmd

#수정하기

already_rated,predictions=recommend_recipes(df_svd,1,recipes,ratings,7)
already_rated.head(7)

print(predictions)

