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
4. RESTful API로 model 호출
--------------------------------------------------  
  <br/>
  <br/>
  
### W2V Model 생성 - getModel.py
> * 사용 라이브러리
> 
>     - gensim
>     - konlpy  
>     <br/>  
>     
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
>     pip를 이용해 flask와 flask-restx를 설치해 줍니다.  
>     
>     ```
>        $ pip install flask  
>        $ pip install flask-restx
>     ```  
>     <br/>  
> * 주요 내용  
> 
>     - 이 함수는 아무 파라미터가 없는 기본형입니다.  
>     
>     ```python
>        @app.route('/')
>        def hello_world():
>           return 'Hello, Everyone. Im TechnoHong, Nice to meet you. Good LUCK :D'
>     ```  
>     - 우리는 GET 방식으로 읽어올 것이기 때문에, 다음과 같이 정의해줍니다.  
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
>
   


   
### Firebase에서 데이터 가져오기 - getData.py
> 
   
   
   
   
### RESTful API로 model 호출 - w2v_poke.py
> 

   
