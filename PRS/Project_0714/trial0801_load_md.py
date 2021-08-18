# 0. 사용할 패키지 불러오기
import np as np
from keras.utils import np_utils
from keras.datasets import mnist
from keras.models import Sequential
from keras.layers import Dense, Activation
import numpy as np
from numpy import argmax
import pandas as pd

from keras.models import load_model

# 1. 실무에 사용할 데이터 준비하기
from sklearn.model_selection import train_test_split

"""
(x_train, y_train), (x_test, y_test) = mnist.load_data()
x_test = x_test.reshape(10000, 784).astype('float32') / 255.0
y_test = np_utils.to_categorical(y_test)
xhat_idx = np.random.choice(x_test.shape[0], 5)
xhat = x_test[xhat_idx]
"""
r_cols=['itemID', 'userID', 'rating']
ratings = pd.read_csv('Dataset0729/mapped_rating.csv',names=r_cols, skiprows=1, header=None, encoding='UTF-8')

ratings['rating'] = ratings['rating'].values.astype(np.float32)
X = ratings[['userID', 'itemID']].values
y = ratings['rating'].values
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.1)
xhat_idx = np.random.choice(X_test.shape[0], 5)
xhat = X_test[xhat_idx]

# 2. 모델 불러오기
model = load_model('model0801.h5')

print(model.summary())

np.set_printoptions(threshold=7842,linewidth=np.inf)

# 3. 모델 사용하기
item_num=max(ratings['itemID'])
#tmp_item_data = np.array(list(set(ratings.itemID)))[200:300]
tmp_item_data=np.array(list(ratings.itemID))[440:640:2]
tmp_user = np.array([1 for i in range(100)])
print(tmp_item_data)
print(tmp_user)

predictions = model.predict([tmp_user, tmp_item_data])
predictions = np.array([p[0] for p in predictions])
print(predictions)

recommended_item_ids = (-predictions).argsort()[:10]
print(recommended_item_ids)
recommended_item_ids = (-predictions).argsort()[-10:]
print(recommended_item_ids)
"""
청포묵 김무침, 초간단 반찬
대패삼겹살을 넣은 두부김치만들기
"""