import pandas as pd
import numpy as np

og_recipe= pd.read_json('C:/RecoSys/merged_recipe.json', encoding='UTF-8')
tag_list=['recipe_id']

for i in og_recipe['tag']:
    tok=i[0].split(', ')
    for j in tok:
        if j not in tag_list:
            tag_list.append(j)

df = pd.DataFrame(columns=tag_list)

idx=0
for rid, rtag in zip(og_recipe['id'], og_recipe['tag']):
    list=np.zeros(len(tag_list), dtype=np.int32)
    list[0]=rid

    tok = rtag[0].split(', ')

    for i in tok:
        list[tag_list.index(i)]= 1
    #dlist=pd.DataFrame([list],columns=tag_list)
    df.loc[idx]=list

    print(idx)
    idx=idx+1

df.to_csv('C:/RecoSys/new_recipe_tags.csv', encoding='utf-8')
