import csv
import pandas as pd

df = pd.read_csv("recipe.csv",header=0,encoding='utf-8')

ingre_list = list(df['ingre_list'])
ingre_list2 = []

for i in ingre_list:
    tmp = i.replace("[","").replace("]","").replace("'","")
    tmp2 = tmp.split(", ")
    for j in tmp2:
        ingre_list2.append(j)

ingre_set = set(ingre_list2)

ingre_list3 = list(ingre_set)

print(len(ingre_list2))
print(len(ingre_list3))

ingre_df = pd.DataFrame(ingre_list3)
ingre_df.to_csv("ingre_list.csv", header=False, index=False, encoding='utf-8')