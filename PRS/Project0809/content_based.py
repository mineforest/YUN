from collections import defaultdict

from gensim.models.keyedvectors import load_word2vec_format
import numpy as np
import pandas as pd
from gensim.models import Word2Vec, word2vec
from gensim.models import KeyedVectors
from sklearn.metrics.pairwise import cosine_similarity
import time

s_time=time.time()

class Word2v:
    def __init__(self):
        self.df = pd.read_csv('doritos/0728rcp.csv', encoding='utf-8')
        self.word2vec_model = KeyedVectors.load('w2v/w2v.model')
        self.document_embedding_list = self.vectors(self.df['cleand'])
        self.cosine_sim = cosine_similarity(self.document_embedding_list, self.document_embedding_list)

    def vectors(self, document_list):
        document_embedding_list = []

        for line in document_list:
            doc2vec = None
            count = 0
            for word in line.split():
                if word in self.word2vec_model.wv.index_to_key:
                    count += 1
                    if doc2vec is None:
                        doc2vec = self.word2vec_model.wv[word]
                    else:
                        doc2vec = doc2vec + self.word2vec_model.wv[word]
            if doc2vec is not None:
                doc2vec = doc2vec / count
                document_embedding_list.append(doc2vec)
        return document_embedding_list

    def merge(self,dict,obj):
        for j, value in obj:
            dict[j]+=value
        return dict

    def recommendations(self, id):
        rcp = self.df[['id', 'name']]
        res = ''

        indices = pd.Series(self.df.index, index=self.df['id']).drop_duplicates()

        idx = indices.loc[id]
        #print(idx)
        candidates = defaultdict(float)
        for i in idx:
            sim_scores = list(enumerate(self.cosine_sim[i]))
            sim_scores = sorted(sim_scores, key=lambda x: x[1], reverse=True)
            sim_scores = sim_scores[1:51]                                       # 각 레시피별 뽑을 상위 추천레시피 수

            candidates=self.merge(candidates,sim_scores)

            #print(candidates)

        #print(candidates)
        candidates=sorted(candidates.items(), key=lambda x: x[1],reverse=True)
        candidates = candidates[1:21]                                              # 최종으로 뽑을 상위 추천레시피 수
        print(candidates)

        rcp_indices = [i[0] for i in candidates]
        rec = rcp.iloc[rcp_indices].reset_index(drop=True)

        for idx, row in rec.iterrows():
            res += str(row['id']) + ' '
            print(row['name'])

        return res

model=Word2v()
rated=[1392396,1579505,6472193,6872698,6953368,6945181,6935370,6877546,6881283]
# 오후의 간식 아몬드 버터링쿠키, 초코아몬드 쿠키, 녹차 초코칩 쿠키, 연유 버터링 쿠키 만들기, 초보 베이킹 _ 간단하면서도 맛있는 쿠키, 담백 고소한 쿠키만들기, 쫀득 꾸덕한 브라우니쿠키만들기,크랜베리피스타치오화이트초코쿠키 쫀득한 아메리칸쿠키만들기,머랭쿠키
#rated=[1392396]
res=model.recommendations(rated)

print("time :", time.time() - s_time)