from konlpy.tag import Okt  
from gensim.models import Word2Vec
import gensim
import pandas as pd
#https://wikidocs.net/50739

#fread = open('wiki_data.txt', encoding="utf8")
okt=Okt()

df = pd.read_csv('1001rcp.csv', encoding='utf-8')

def arrToStr(arr):
  return arr.str.replace("[^ㄱ-ㅎㅏ-ㅣ가-힣 ]"," ")



result = []
for i,rcp in enumerate(df['recipe']):
    tokenlist = okt.pos(rcp, stem=True, norm=True)     
    temp=[]
    for word in tokenlist:
        if word[1] in ["Noun"]: # 명사일 때만
            temp.append((word[0])) # 해당 단어를 저장함        
    temp=temp+df.loc[i,'cleand'].split()    
    result.append(temp) # 결과에 저장
    df.loc[i,'cleand']=str(' '.join(temp))
    if i%500==0: 
        print("%d번째 While문."%i)
        print(temp)
    
df.to_csv('1018rcp.csv', encoding='utf-8')
print('총 샘플의 개수 : {}'.format(len(result)))

word2vec_model = Word2Vec(vector_size=300, window=5, min_count=2, workers=-1)
word2vec_model.build_vocab(result)
word2vec_model.train(result, total_examples = word2vec_model.corpus_count, epochs=15)
word2vec_model.save('1018w2v.model')



"""
df2['cleand'] = arrToStr(df2['ingre_name']) + ' ' + arrToStr(df2['tag'])

df2['cleand'].replace('', np.nan, inplace=True)
df2 = df2[df2['cleand'].notna()]
print(len(df2))

"""