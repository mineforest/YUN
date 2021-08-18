# -*- coding: utf-8 -*-
"""0728_rcp_EDA.ipynb

Automatically generated by Colaboratory.

Original file is located at
    https://colab.research.google.com/drive/1p28coqyaCE06-Wm7L7P4g7QFzj6cEaZc
"""

import pandas as pd
import numpy as np
from gensim.models import Word2Vec

import json

with open('0727_recipe(csv)2.json', 'r') as f:
  rcp_json = json.loads(f.read())

df2 = pd.json_normalize(rcp_json, record_path=['ingre_list'], meta=['id','name','recipe','tag'])

df2[:5]

df2 = df2.groupby('id').agg({'name':'first',
                               'recipe':'first',
                               'tag':'first',
                               'ingre_name':', '.join}).reset_index()

df2[:5]

def arrToStr(arr):
  return arr.str.replace("[^ㄱ-ㅎㅏ-ㅣ가-힣 ]"," ")

df2['cleand'] = arrToStr(df2['ingre_name']) + ' ' + arrToStr(df2['tag'])

df2['cleand'].head()

df2['cleand'].replace('', np.nan, inplace=True)
df2 = df2[df2['cleand'].notna()]
print(len(df2))

corpus = []
for w in df2['cleand']:
  corpus.append(w.split())

corpus[:10]

word2vec_model = Word2Vec(size=300, window=5, min_count=2, workers=-1)
word2vec_model.build_vocab(corpus)
word2vec_model.train(corpus, total_examples = word2vec_model.corpus_count, epochs=15)

word2vec_model.save('0728w2v.model')

df2.to_csv('0728rcp.csv', encoding='utf-8')