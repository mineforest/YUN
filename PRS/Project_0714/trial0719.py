import pandas as pd
import numpy as np
import matplotlib.pyplot as plt
import os
import warnings

from tensorflow.keras.layers import Input, Embedding, Flatten, Dot, Dense, Concatenate
from tensorflow.keras.models import Model, load_model
from tensorflow.keras.utils import plot_model
from sklearn.model_selection import train_test_split
import seaborn as sns
import matplotlib.pyplot as plt

from tensorflow.python.keras.optimizer_v1 import Adam

r_cols=['book_id', 'user_id', 'rating']
data = pd.read_csv('Dataset0626/non_str_rating.csv',names=r_cols, skiprows=1, header=None, encoding='UTF-8')
#books = pd.read_csv('../data/book_kaggle/books.csv')  - 레시피정보

train, test = train_test_split(data, test_size = 0.2)


number_of_unique_user = len(data.user_id.unique())
number_of_unique_book_id = len(data.book_id.unique())
#print(number_of_unique_user, number_of_unique_book_id) #28459, 1018


#Dense layer

book_input = Input(shape=(1, ), name='book_input_layer')
user_input = Input(shape=(1, ), name='user_input_layer')

book_embedding_layer = Embedding(number_of_unique_book_id + 1, 16, name='book_embedding_layer')
user_embedding_layer = Embedding(number_of_unique_user + 1, 16, name='user_embedding_layer')

#book_embedding_layer = Embedding(number_of_unique_book_id + 1, 8, name='book_embedding_layer')
#user_embedding_layer = Embedding(number_of_unique_user + 1, 8, name='user_embedding_layer')

book_vector_layer = Flatten(name='book_vector_layer')
user_vector_layer = Flatten(name='user_vector_layer')

concate_layer = Concatenate()

dense_layer1 = Dense(128, activation='relu')
dense_layer2 = Dense(32, activation='relu')

result_layer = Dense(1)


# 쌓기
book_embedding = book_embedding_layer(book_input)
user_embedding = user_embedding_layer(user_input)

book_vector = book_vector_layer(book_embedding)
user_vector = user_vector_layer(user_embedding)

concat = concate_layer([book_vector, user_vector])
dense1 = dense_layer1(concat)
dense2 = dense_layer2(dense1)

result = result_layer(dense2)

model = Model(inputs=[user_input, book_input], outputs=result)

#model.summary()
plot_model(model, to_file='./dense_predict_model_16ver.png', show_shapes=True, show_layer_names=True)


model.compile(loss = 'mse', optimizer='Adam', metrics=['mse'])
history = model.fit([train.user_id, train.book_id], train.rating, epochs=8, verbose=1)





"""
plt.plot(history.history['loss'])
plt.xlabel('epochs')
plt.ylabel('training error')

model.evaluate([test.user_id, test.book_id], test.rating)
predictions = model.predict([test.user_id.head(10), test.book_id.head(10)])

for p, t in zip(predictions, test.rating.values[:10]):
    print(p, t)
    
print(tmp_book_data[:10])
print(tmp_user[:10])

predictions = model.predict([tmp_user, tmp_book_data])
predictions = np.array([p[0] for p in predictions])
print(predictions[:5])

# id는 1부터 시작인데 argsort를 하면 0부터 되므로 1을 더한다.
recommended_book_ids = (-predictions).argsort()[:5] + 1
print(recommended_book_ids) 

books[books['id'].isin(recommended_book_ids)]

"""


