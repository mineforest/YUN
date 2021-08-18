import pandas as pd
import numpy as np

from surprise import KNNBasic, SVD, SVDpp, NMF, Reader
from surprise import Dataset
from surprise.model_selection import cross_validate,train_test_split
import os

r_cols=['itemID', 'userID', 'rating']
og_data = pd.read_csv('Dataset0626/non_str_rating.csv',names=r_cols, header=None, encoding='UTF-8')

reader = Reader(rating_scale=(1, 5))
data = Dataset.load_from_df(og_data[['userID', 'itemID', 'rating']], reader)

model=SVD()
#model=SVDpp()

cross_validate(model,data,measures=['rmse','mse'],cv=3,n_jobs=4,verbose=True)


#train, test = train_test_split(data, test_size = 0.2)
trainset = data.build_full_trainset()

#model.compile(loss = 'mse', optimizer='Adam', metrics=['mse'])
history = model.fit(trainset)
model.predict()









"""

ratings_dict = {'itemID': [1, 1, 1, 2, 2],
                'userID': [9, 32, 2, 45, 'user_foo'],
                'rating': [3, 2, 4, 3, 1]}
df = pd.DataFrame(ratings_dict)

# A reader is still needed but only the rating_scale param is requiered.
reader = Reader(rating_scale=(1, 5))

# The columns must correspond to user id, item id and ratings (in that order).
data = Dataset.load_from_df(df[['userID', 'itemID', 'rating']], reader)

# We can now use this dataset as we please, e.g. calling cross_validate
cross_validate(NormalPredictor(), data, cv=2)

"""