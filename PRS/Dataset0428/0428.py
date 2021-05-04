import numpy as np
import pandas as pd

from surprise import BaselineOnly 
from surprise import KNNWithMeans
from surprise import SVD
from surprise import SVDpp
from surprise import NMF
from surprise import Reader


u_cols=['user_id','age']
users=pd.read_csv('C:/RecoSys/Dataset0501/user.csv', names=u_cols, sep='|',encoding='utf-8')
users.set_index('user_id')
users.head()

i_cols=['recipe_id','title','unknown','kor','west','chi','jap']
recipes=pd.read_csv('C:/RecoSys/Dataset0501/recipe.csv',names=i_cols,sep='|',encoding='utf-8')
recipes.set_index('recipe_id')
recipes.head()

r_cols=['user_id','recipe_id','rating']
ratings= pd.read_csv('C:/RecoSys/Dataset0501/ratings.csv',names=r_cols,sep='|',encoding='utf-8')
ratings=ratings.set_index('user_id')
ratings.head()

def recom_recipe(n_items):
    recipe_sort = recipe_mean.sort_values(ascending=False)[:n_items]
    recom_recipes=recipes.loc[recipe_sort.index-1]
    rec=recom_recipes['title']
    
    return rec


recipe_mean=ratings.groupby(['recipe_id'])['rating'].mean()
tmp=recom_recipe(5)


for i in enumerate(tmp):
    for j in i:
        print(j, end=' ')
    print()