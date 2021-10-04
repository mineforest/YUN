import pandas as pd
from gensim.models import KeyedVectors
from sklearn.metrics.pairwise import cosine_similarity

class Word2v:
    def __init__(self, hist,allergying):
        self.hist=hist
        self.allergying=allergying
        self.df = pd.read_csv('1001rcp.csv', encoding='utf-8')
        self.word2vec_model = KeyedVectors.load('1001w2v.model')
        self.document_embedding_list, self.obj = self.vectors(self.df['cleand'])
        self.cosine_sim = cosine_similarity(self.document_embedding_list, self.document_embedding_list)

    def vectors(self, document_list):
        document_embedding_list = []
        obj = len(document_list)
        document_list.loc[obj] = self.df[self.df['id'].isin(self.hist)]['cleand'].sum()
        self.hist=self.df.index[self.df['id'].isin(self.hist)].tolist()
        self.hist.append(obj)

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
        return document_embedding_list, obj

    def isContained(self,l1):
        for i in self.allergying:
            if i in l1:
                return False
        return True

    def recommendations(self):
        rcp = self.df[['id','name']]
        res = {}

        idx=self.obj
        sim_scores = list(enumerate(self.cosine_sim[idx]))
        sim_scores = sorted(sim_scores, key= lambda x:x[1], reverse=True)
        sim_scores = sim_scores[0:1000]
        sim_scores = [i for i in sim_scores if (not i[0] in self.hist) and self.isContained((self.df.iloc[i[0]]['cleand']))]
        
        sim_scores = sim_scores[0:500]
        
        rcp_indices = [i[0] for i in sim_scores]     
        rec = rcp.iloc[rcp_indices].reset_index(drop=True)
        jdx=0
        for idx, row in rec.iterrows():
            #same = row['cleand']
            res[jdx]={'id':row['id'],'score':int(sim_scores[idx][1]*100)}
            jdx=jdx+1
        print("치명적 오류 발생안함")
        return res


#w3v = Word2v(list(map(int, [6948903,1894779,1978049,2803587])),['닭가슴살샐러드','브라우니'])
#res = w3v.recommendations()    
#print(w3v.recommendations())

# 기피재료 제거
# 1. set(l1).isdisjoint(set(l2))
# 2. substr compare <