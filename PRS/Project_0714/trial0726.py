import numpy as np
import pandas as pd
from sklearn.model_selection import train_test_split
from sklearn.preprocessing import LabelEncoder

from keras.models import Model
from keras.layers import Input, Reshape
from keras.layers.embeddings import Embedding
from keras.regularizers import l2

from keras.layers import Add, Activation, Lambda
from keras.layers import Concatenate, Dense, Dropout
from tensorflow import keras
from keras.models import load_model


r_cols=['itemID', 'userID', 'rating']
ratings = pd.read_csv('Dataset0729/mapped_rating.csv',names=r_cols, skiprows=1, header=None, encoding='UTF-8')
print(ratings.head())

g = ratings.groupby('userID')['rating'].count()
top_users = g.sort_values(ascending=False)[:15]
g = ratings.groupby('itemID')['rating'].count()
top_movies = g.sort_values(ascending=False)[:15]
top_r = ratings.join(top_users, rsuffix='_r', how='inner', on='userID')
top_r = top_r.join(top_movies, rsuffix='_r', how='inner', on='itemID')
pd.crosstab(top_r.userID, top_r.itemID, top_r.rating, aggfunc=np.sum)

user_enc = LabelEncoder()
ratings['user'] = user_enc.fit_transform(ratings['userID'].values)
n_users = ratings['user'].nunique()
item_enc = LabelEncoder()
ratings['item'] = item_enc.fit_transform(ratings['itemID'].values)
n_items = ratings['item'].nunique()
ratings['rating'] = ratings['rating'].values.astype(np.float32)
#min_rating = min(ratings['rating'])
#max_rating = max(ratings['rating'])
min_rating = 0
max_rating = 5

#print(n_users, n_items, min_rating, max_rating)

X = ratings[['user', 'item']].values
y = ratings['rating'].values
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.1)
#print(X_train.shape, X_test.shape, y_train.shape, y_test.shape)

n_factors = 50
X_train_array = [X_train[:, 0], X_train[:, 1]]
X_test_array = [X_test[:, 0], X_test[:, 1]]


class EmbeddingLayer:
    def __init__(self, n_items, n_factors):
        self.n_items = n_items
        self.n_factors = n_factors

    def __call__(self, x):
        x = Embedding(self.n_items, self.n_factors, embeddings_initializer='he_normal',
                      embeddings_regularizer=l2(1e-6))(x)
        x = Reshape((self.n_factors,))(x)
        return x


def RecommenderNet(n_users, n_movies, n_factors, min_rating, max_rating):
    user = Input(shape=(1,))
    u = EmbeddingLayer(n_users, n_factors)(user)

    movie = Input(shape=(1,))
    m = EmbeddingLayer(n_movies, n_factors)(movie)

    x = Concatenate()([u, m])
    x = Dropout(0.05)(x)

    x = Dense(10, kernel_initializer='he_normal')(x)
    x = Activation('relu')(x)
    x = Dropout(0.5)(x)

    x = Dense(1, kernel_initializer='he_normal')(x)
    x = Activation('sigmoid')(x)
    x = Lambda(lambda x: x * (max_rating - min_rating) + min_rating)(x)
    model = Model(inputs=[user, movie], outputs=x)
    model.compile(optimizer="adam", loss='mean_squared_error')
    return model,x

model,x = RecommenderNet(n_users, n_items, n_factors, min_rating, max_rating)
history = model.fit(x=X_train_array, y=y_train, batch_size=64, epochs=5,
                    verbose=1, validation_data=(X_test_array, y_test))

model.save('model0801.h5')
"""
model.save("my_model")
print("Original model:", model)

del EmbeddingLayer

loaded = keras.models.load_model("my_model")

print("Loaded model:", loaded)
"""

tmp_item_data = np.array(list(set(ratings.itemID)))[500:600]
tmp_user = np.array([1 for i in range(100)])

predictions = model.predict([tmp_user, tmp_item_data])
predictions = np.array([p[0] for p in predictions])
recommended_item_ids = (-predictions).argsort()[:10]
print(recommended_item_ids)


"""
def RecommenderV1(n_users, n_items, n_factors):
    user = Input(shape=(1,))
    u = Embedding(n_users, n_factors, embeddings_initializer='he_normal',
                  embeddings_regularizer=l2(1e-6))(user)
    u = Reshape((n_factors,))(u)

    item = Input(shape=(1,))
    m = Embedding(n_items, n_factors, embeddings_initializer='he_normal',
                  embeddings_regularizer=l2(1e-6))(item)
    m = Reshape((n_factors,))(m)

    x = Dot(axes=1)([u, m])
    model = Model(inputs=[user, item], outputs=x)

    model.compile(optimizer= "adam",loss='mean_squared_error')
    return model

model = RecommenderV1(n_users, n_items, n_factors)
history = model.fit(x=X_train_array, y=y_train, batch_size=64, epochs=5,
                    verbose=1, validation_data=(X_test_array, y_test))



def RecommenderV2(n_users, n_movies, n_factors, min_rating, max_rating):
    user = Input(shape=(1,))
    u = EmbeddingLayer(n_users, n_factors)(user)
    ub = EmbeddingLayer(n_users, 1)(user)

    movie = Input(shape=(1,))
    m = EmbeddingLayer(n_movies, n_factors)(movie)
    mb = EmbeddingLayer(n_movies, 1)(movie)
    x = Dot(axes=1)([u, m])
    x = Add()([x, ub, mb])
    x = Activation('sigmoid')(x)
    x = Lambda(lambda x: x * (max_rating - min_rating) + min_rating)(x)
    model = Model(inputs=[user, movie], outputs=x)
    model.compile(optimizer="adam", loss='mean_squared_error')
    return model

model = RecommenderV2(n_users, n_items, n_factors, min_rating, max_rating)
history = model.fit(x=X_train_array, y=y_train, batch_size=64, epochs=5,
                    verbose=1, validation_data=(X_test_array, y_test))
"""

