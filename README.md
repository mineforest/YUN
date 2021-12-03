POKE
====
>머신러닝을 이용한 레시피 추천 서비스   

<img src="https://user-images.githubusercontent.com/81149946/144440382-7700fbed-d652-493a-8996-3b55940bc64a.png" align="right"  width="170" height="120">   

 대한민국 1인 가구 수의 증가, 요리가 미숙한 MZ세대, 이와 같은 상황에서 본 프로젝트는 머신러닝을 적용하여 개인화된 레시피 추천 시스템을 개발한다. 추천 시스템은 Android App에 적용하여 서비스한다. 서비스 내 냉장고 기능을 통해 현재 보유한 재료로 가능한 레시피를 추천할 수 있으며, 재료의 유통기한을 표시해주어 냉장고 속 식재료의 순환이 적절하게 이루어질 수 있다.

 - 개발목표 : MZ세대를 겨냥해 식사메뉴 의사결정에 도움을 줄 수 있는 Android App 개발   
 - 개발인원 : 홍석준(팀장), 김동욱, 윤대현, 최재민, 황현성   
 - 소요기간 : 2021.03 ~ 2021.11 (약 9개월)   
 - 개발툴 : Java, Python, Flask, GCP, Firebase DB, Beautiful Soup, Apache, Zxing 등

## Reauirements:
 - Android sdk 24+   


## 서비스 구성도   
![서비스구성도](https://user-images.githubusercontent.com/81149946/144443559-3c762761-a86a-4070-b22c-cf5cbfe3dcd5.png)
전체적인 구성은 다음과 같다. 우선 Python 라이브러리인 Beatiful Soup을 이용해 웹 크롤링을 진행한다. 웹 크롤링을 통해 9,966개의 레시피 데이터를 수집 후 전처리 과정을 거쳐 FireStore에 레시피 
데이터베이스를 구축하였다. 수집한 레시피 데이터를 사용하여 컨텐츠 기반 추천 시스템 중 하나인 Word2vec 알고리즘으로 레시피 추천 시스템을 구현하고 Andorid App과의 통신을 위해 RESTful API로 추천 모델을 서빙한다. Android App에서는 Cold Start 문제를 해결하기위해 사용자의 초기 데이터를 받아 REST api를 호출하여 추천 레시피 목록을 제공한다. 이후 사용 시에는 사용자의 히스토리도 반영되어 개인화된 추천을 제공할 수 있다. App 내 냉장고 기능에서 재료 추가의 편의를 돕고자 Zxing 라이브러리를 사용하여 바코드 정보를 받는다. 받은 바코드 넘버를 바코드연계제품정보 공공데이터 API를 호출하여 재료 정보를 자동 입력한다.
## 앱 구성도   
![앱구성도](https://user-images.githubusercontent.com/81149946/144421077-35870099-2408-4f80-a65f-d3f76f02ded8.png)

## 핵심 기술   
> [Beautiful Soup를 이용한 Web Crawling](https://github.com/TechnoHong/POKE/tree/main/WC)   
> [Word2vec을 이용한 추천 모델과 Flask를 이용한 RESTful API](https://github.com/TechnoHong/POKE/tree/main/api)   
> Zxing 라이브러리를 이용한 Barcode Scanner   

## 애플리케이션 인터페이스

|![TeamTCNK_POKE시연 mp4_20211203_165041](https://user-images.githubusercontent.com/81149946/144565405-94fdaeb9-f009-44ad-914d-b5801c622472.gif)|![TeamTCNK_POKE시연 mp4_20211203_164932](https://user-images.githubusercontent.com/81149946/144565299-d05a47ac-ab16-4194-ad54-379b236bf5d1.gif)|![Screen_Recording_20211116-014724_POKE mp4_20211203_165253](https://user-images.githubusercontent.com/81149946/144565792-e664c437-f448-4cb9-ac3b-4c8b4ab3faba.gif)|
|---|---|---|
|AppStart|선호도사전조사|바코드스캔|
