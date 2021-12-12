from konlpy.tag import Okt  
from gensim.models import Word2Vec
import gensim
import pandas as pd

okt=Okt()

df = pd.read_csv('CSV파일명.csv', encoding='utf-8')

def arrToStr(arr):
  return arr.str.replace("[^ㄱ-ㅎㅏ-ㅣ가-힣 ]"," ")

result = []
for i,rcp in enumerate(df['recipe']):
    tokenlist = okt.pos(rcp, stem=True, norm=True)     
    temp=[]
    for word in tokenlist:
        if word[1] in ["Noun"]: # 명사일 때만
            temp.append((word[0])) 
    temp=temp+df.loc[i,'cleand'].split()    
    result.append(temp) # 결과에 저장
    df.loc[i,'cleand']=str(' '.join(temp))
    if i%500==0: 
        print("%d번째 While문."%i)
        print(temp)
    
df.to_csv('CSV파일명.csv', encoding='utf-8')
print('총 샘플의 개수 : {}'.format(len(result)))

word2vec_model = Word2Vec(vector_size=300, window=5, min_count=2, workers=-1)
word2vec_model.build_vocab(result)
word2vec_model.train(result, total_examples = word2vec_model.corpus_count, epochs=15)
word2vec_model.save('Model명.model')
