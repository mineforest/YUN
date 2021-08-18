import numpy as np
import pandas as pd


def create_mapping(values, filename):
    with open(filename, 'w') as ofp:
        value_to_id = {value:idx for idx, value in enumerate(values.unique())}
        for value, idx in value_to_id.items():
            ofp.write("{},{}\n".format(value, idx))
    return value_to_id

r_cols=['itemID', 'userID', 'rating']
ratings = pd.read_csv('Dataset0729/non_str_rating.csv',names=r_cols, skiprows=1, header=None, encoding='UTF-8')
print(ratings.head())

user_mapping = create_mapping(ratings["userID"], "Dataset0729/unique_users.csv")
item_mapping = create_mapping(ratings["itemID"], "Dataset0729/unique_items.csv")


ratings["userID"] = ratings["userID"].map(user_mapping.get)
ratings["itemID"] = ratings["itemID"].map(item_mapping.get)

mapped_df = ratings[['itemID', 'userID', 'rating']]

mapped_df.to_csv('Dataset0729/mapped_rating.csv', header=False,index=False, encoding='utf-8')
mapped_df.head()