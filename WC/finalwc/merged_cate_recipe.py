import pandas as pd
import os
import csv

merge_path = './cate_recipe.csv'

df1 = pd.read_csv('recipe.csv', sep=",", names=['id','title','info1','info2','info3','ingre_count','ingre_list','ingre_count_list','ingre_unit_list','recipe_step'])
df2 = pd.read_csv('cate.csv', sep=",")

df=pd.merge(df1,df2,on='id',how='inner')
df.to_csv(merge_path,mode='w',index=False,encoding='utf-8')
