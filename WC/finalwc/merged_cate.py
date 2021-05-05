import pandas as pd
import os
import csv
import glob

datapath = './cate/'
merge_path = './cate.csv'

file_list = glob.glob(datapath+'*')

li = []

for filename in file_list:
    df = pd.read_csv(filename, sep=",", names=['id','tag'])
    li.append(df)

frame = pd.concat(li)
print(frame)
frame = frame.groupby(['id'])['tag'].apply(', '.join).reset_index()
frame.to_csv(merge_path,mode='w',index=False,encoding='utf-8')
