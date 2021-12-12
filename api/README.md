W2V을 이용한 추천 모델과 FLASK를 이용한 RESTful API
--------------------------------------------------

```
개발툴 : Python, Flask, Firebase DB, Apache  

목적 : 사용자 선호도 기반 맞춤 레시피 목록(W2V)을 앱에 제공(API)
```
   
   
## 목차
1. W2V Model 생성
2. RESTful API 구현
3. Firebase에서 데이터 가져오기
4. 추천 model을 사용하기 위한 Word2v 클래스
 <br/>
 <br/>
 
  
### W2V Model 생성 - getModel.py
> * 사용 라이브러리
> 
>     - gensim
>     - konlpy  
>     <br/>  
>     
> * Installation  
>     - KoNLPy 설치법은 http://konlpy.org/ko/v0.4.3/install/ 를 참고해주세요.  
>     - gensim은 pip를 이용해 설치할 수 있습니다.  
>     <br/>  
>      
>     ```
>        $ pip install gensim  
>     ```  
>     <br/>  
> * 주요 내용  
> 
>     - 데이터에서 명사만 추출해 사용하고자 konlpy의 Okt 모델을 사용합니다.
>     - 반복문을 통해 각 품사를 태깅하고, 그 중 명사 집합만 골라 모델 생성에 사용합니다.
>  
>      
>     - W2V 모델은 다음 문장을 통해 생성합니다.  
>  
>        ```python
>           word2vec_model = Word2Vec(vector_size=300, window=5, min_count=2, workers=-1)
>        ```  
>     - 이렇게 생성한 모델에 단어를 빌드하고, 훈련하는 과정은 이렇게 간단하게 진행할 수 있습니다.  
>     
>        ```python
>           word2vec_model.build_vocab(result)
>           word2vec_model.train(result, total_examples = word2vec_model.corpus_count, epochs=15)
>        ```  
>     - 훈련시킨 모델은 .model 타입의 파일로 저장할 수 있습니다.  
>     
>        ```python
>           word2vec_model.save('Model명.model')
>        ```  
 <br/>


### RESTful API 구현 - app.py
> * 사용 라이브러리
> 
>     - flask
>     <br/>  
>     
> * Installation  
>     - pip를 이용해 flask와 flask-restx를 설치해 줍니다.  
>     <br/>  
>      
>     ```
>        $ pip install flask  
>        $ pip install flask-restx
>     ```  
>     <br/>  
> * 주요 내용  
> 
>     - 이 함수는 아무 파라미터가 없는 기본형입니다.  
>     <br/>  
>     
>     ```python
>        @app.route('/')
>        def hello_world():
>           return 'Hello, Everyone. Im TechnoHong, Nice to meet you. Good LUCK :D'
>     ```  
>     - 우리는 GET 방식으로 읽어올 것이기 때문에, 다음과 같이 정의해줍니다.  
>     <br/>  
>     
>     ```python
>        @app.route('/<uid>', methods = ['GET'])  
>        def user_hist_base(uid):  
>           print(uid)  
>           return res  
>     ```  
>     - 이후 이 코드를 실행하고, url 뒤에 정보를 입력해 주면, 코드에서 받을 수 있습니다.  
>   
>     - 예를 들어 http://localhost:5000/1234 로 접속하면, 파라미터 uid에 1234를 받을 수 있습니다.  
>       
>     - 우리는 이렇게 접속이 발생하면, getData에 그 uid에 해당하는 user의 정보를 던져주고, 그에 상응하는 결과값을 받아 처리합니다.  
>
>  
 <br/>
   
   
### Firebase에서 데이터 가져오기 - getData.py
> * 사용 라이브러리
> 
>     - firebase_admin
>     <br/>  
>     
> * Installation  
>     - pip를 이용해 firebase를 설치해 줍니다.  
>    <br/>  
>   
>     ```
>        $ pip install firebase_admin
>     ```  
>        
>     - 또한 Firebase admin SDK 스니펫에서 비공개 키를 받아와 해당 레포지토리에 저장합니다.  
>     키는 보안을 위해 제거했습니다.
>   
>     <br/>  
> * 주요 내용  
>     - POKE에선 사용자의 히스토리, 기피재료, 선호도를 반영하여 추천해줍니다.  
>     - 따라서, 이들을 각각 Firebase에 접근해 가져올 함수를 정의해 주었습니다.  
>   
>     - Ex ) user의 선호도를 가져오는 함수 getPrefer()  
>       Firebase 스토리지의 preference 항목에서 user key에 해당하는 값을 가져오는 과정입니다.  
>   
>     ```python
>        def getPrefer(self):
>        prefer = db.reference('preference').child(self.key).get()
>        if prefer==None:
>            return []
>        return [i['preference'] for i in prefer.values()]
>     ```  
>       
>     
>     
 <br/>  
   
   
### 추천 model을 사용하기 위한 Word2v 클래스 - w2v_poke.py
> * 사용 라이브러리
> 
>     - gensim
>     - sklearn  
>   
>     <br/>  
>     
> * Installation  
>     - pip를 이용해 gensim과 sklearn 모듈을 설치해 줍니다.  
>     - pip로 scikit-learn 설치 시 sklearn에 사용되는 numpy, scipy 모듈도 자동으로 설치됩니다.
>    <br/>  
>   
>     ```
>        $ pip install gensim
>        $ pip install scikit-learn  
>     ```  
>   
>     <br/>  
> * 주요 내용  
>     - W2V 모델을 사용해 추천 리스트를 출력해 주는 클래스입니다.
>     - 생성자(init)
>        - 인자값으로 app.py에서 던져주는 사용자의 히스토리, 기피재료, 선호도를 받습니다.
>        - CSV 타입의 레시피 파일을 dataframe으로 받아오고, model을 생성합니다.  
>
>        - 또한 vectors()를 통해 임베딩 리스트를 받아, 각 임베딩 단어에 대한 코사인 유사도를 구합니다. 
>   
>          ```python
>              self.cosine_sim = cosine_similarity(self.document_embedding_list, self.document_embedding_list)
>          ```  
>   
>  
>     - vectors() 
>        - 받아온 인자값으로 *유령 레시피*를 기존의 dataframe 끝에 하나 만들어 붙여 줍니다.
>        - 그렇게 추가된 dataframe의 'cleand'열을 리스트로 추출합니다. (document_list)  
>        - 이후 이 리스트를 돌며, 벡터를 임베딩하고, 이를 document_embedding_list에 누적 저장합니다.  
>  
>     - recommendations()  
>        - init을 통해 구한 모든 레시피에 대한 코사인 유사도는 cosine_sim에 저장되어 있습니다.
>        - cosine_sim에는 모든 레시피에 대한 각각의 유사도가 담겨 있는데, 그 중 vectors()에서 넣었던 *유령 레시피*의 코사인 유사도를 이용할 것입니다.  
>   
>          ```python
>              sim_scores = list(enumerate(self.cosine_sim[self.obj]))
>          ```  
>           
>   
>        - 각 레시피에 대한 코사인 유사도가 높을 수록 유사한 레시피이기 때문에, 내림차순으로 정렬해줍니다.  
>        - sim_scores는 (레시피 id,유사도)의 튜플 형태로 저장되어 있습니다. 유사도 기준으로 정렬하고자 간단한 람다식을 사용해 주었습니다.
>   
>          ```python
>              sim_scores = sorted(sim_scores, key= lambda x:x[1], reverse=True)
>          ```  
>          
>        - 그렇게 구해진 추천 목록을 원래 dataframe과 매칭해 준 후, dict 객체에 저장해줍니다.
>        - API는 json 형식으로 전달하기 때문에, json과 호환되는 타입인 dict로 저장합니다.
>        
   
   
   
   
