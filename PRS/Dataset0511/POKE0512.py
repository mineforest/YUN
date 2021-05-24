import pandas as pd
import numpy as np

og_recipe= pd.read_csv('C:/RecoSys/cate_recipe.csv', header=0, encoding='UTF-8')
tags=list(og_recipe['tag'])

tag_list=['recipe_id']

for i in tags:
    tok=i.split(', ')
    for j in tok:
        if j not in tag_list:
            tag_list.append(j)
df = pd.DataFrame(columns=tag_list)

"""
for rid, rtag in zip(og_recipe['id'], og_recipe['tag']):
    list=[0]*(len(tag_list)+1)
    list[0]=rid

    tok = rtag.split(', ')

    for i in tok:
        list[tag_list.index(i)]= 1
    df = df.append(list)
    print(list)
print(df.head())
"""
idx=0
for rid, rtag in zip(og_recipe['id'], og_recipe['tag']):
    list=np.zeros(len(tag_list), dtype=np.int32)
    list[0]=rid

    tok = rtag.split(', ')

    for i in tok:
        list[tag_list.index(i)]= 1
    dlist=pd.DataFrame([list],columns=tag_list)
    df.loc[idx]=list

    print(idx)
    idx=idx+1
#numpy로

df.to_csv('C:/RecoSys/recipe_tags.csv', encoding='utf-8')