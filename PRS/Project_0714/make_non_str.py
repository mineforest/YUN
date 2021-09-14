import pandas as pd
from pandas import DataFrame as df

r_cols=['book_id', 'user_id', 'rating']
data = pd.read_csv('Dataset0626/new_rating.csv',names=r_cols, header=None, encoding='UTF-8')

print(data.info())
"""
new_data = df( columns=r_cols)
print(new_data.info())
print(new_data)
"""
j=0
for i, t in enumerate(data['user_id']):
    if not t.isdigit() :
        data=data.drop(i)
        #new_data.append(data.loc[i])
       # print(data.loc[i])

print(data.info())



data.to_csv('Dataset0626/non_str_rating.csv', header=False,index=False, encoding='utf-8')

mydata = pd.read_csv('Dataset0626/non_str_rating.csv',names=r_cols, header=None, encoding='UTF-8')

print(mydata.info())
"""

print(data.info())
df_mask=data['user_id'].isdigit()
filtered_df = data[df_mask]

values=[10000000,100000000]
filtered_df = data[~data.user_id.isin(values)]

for i in data['user_id']:
    if not i.isdigit() :
        #print(data.loc(i))
        data.drop(data.iloc(i),axis=0)

print(data.shape)
"""
#rating 데이터에서 user에 string잇는 행 제거할라햇음