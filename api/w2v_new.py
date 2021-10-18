import pandas as pd
import gensim
from gensim.models import KeyedVectors
from sklearn.metrics.pairwise import cosine_similarity

class Word2v:
    def __init__(self, hist,allergying,pref):
        self.hist=hist
        self.pref=pref
        self.allergying=allergying
        self.df = pd.read_csv('1018rcp.csv', encoding='utf-8')
        self.word2vec_model = KeyedVectors.load('1018w2v.model')
        self.document_embedding_list, self.obj = self.vectors()
        self.cosine_sim = cosine_similarity(self.document_embedding_list, self.document_embedding_list)
    
    def vectors(self):
        document_embedding_list = []
        obj=len(self.df)
        
        token = self.df[self.df['id'].isin(self.hist)]['cleand'].sum()
        if type(token)==int: token=''
        self.df=self.df.append({'id' : str(obj) , 'cleand' : token+' '+' '.join(self.pref)},ignore_index=True)                

        document_list=self.df['cleand']
        self.hist=self.df.index[self.df['id'].isin(self.hist)].tolist()
        self.hist.append(obj)        
        ddx=0
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
            if doc2vec is None:
                print(line)
                ddx=ddx+1        
        return document_embedding_list, obj

    def isExcepted(self,l1):
        for i in self.allergying:
            if i in l1:
                return False
        return True

    def recommendations(self):
        rcp = self.df[['id','name']]
        res = {'recipe':[]}
        
        sim_scores = list(enumerate(self.cosine_sim[self.obj]))   # 제일 마지막 애에 대한 값을 구하라.        
        
        sim_scores = sorted(sim_scores, key= lambda x:x[1], reverse=True)
        sim_scores = sim_scores[0:1000]
        
        sim_scores = [i for i in sim_scores if (not i[0] in self.hist) and self.isExcepted((self.df.iloc[i[0]]['cleand']))]
        
        sim_scores = sim_scores[0:500]
        
        rcp_indices = [i[0] for i in sim_scores]     
        rec = rcp.iloc[rcp_indices].reset_index(drop=True)
        
        for idx, row in rec.iterrows():            
            res['recipe'].append({'id':row['id'],'score':int(sim_scores[idx][1]*100)})

        print("치명적 오류 발생안함")
        return res


#w3v = Word2v(list(map(int, [6948903,1978049,2803587])),['닭가슴살샐러드','브라우니'],['김치'])
#닭가슴살샐러드, 두부메이플브라우니, 스터프트에그
#w3v = Word2v(list(map(int, [1564712])),[],['파스타','파스타','파스타','파스타','파스타','파스타','파스타','파스타','파스타','파스타'])
#초콜릿케이크
#res = w3v.recommendations()    
#print(w3v.recommendations())

# 기피재료 제거
# 1. set(l1).isdisjoint(set(l2))
# 2. substr compare <