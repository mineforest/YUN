from gensim.models.keyedvectors import load_word2vec_format
import pandas as pd
from gensim.models import Word2Vec, word2vec
from gensim.models import KeyedVectors
from sklearn.metrics.pairwise import cosine_similarity

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

    def recommendations(self, id):
        rcp = self.df[['id','name']]
        res = ''

        indices = pd.Series(self.df.index, index=self.df['id']).drop_duplicates()
        idx = indices[int(id)]
        sim_scores = list(enumerate(self.cosine_sim[idx]))
        sim_scores = sorted(sim_scores, key= lambda x:x[1], reverse=True)
        sim_scores = sim_scores[1:21]

        rcp_indices = [i[0] for i in sim_scores]

        rec = rcp.iloc[rcp_indices].reset_index(drop=True)

        for idx, row in rec.iterrows():
            res += str(row['id'])+' '
            print(row['name'])
        return res

model=Word2v()
res=model.recommendations(1392396)