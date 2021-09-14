import pandas as pd

r_cols=['itemID', 'userID', 'rating']
ratings = pd.read_csv('doritos/mapped_rating.csv',names=r_cols, skiprows=1, header=None, encoding='UTF-8')

print(ratings)
ratings=ratings[['userID','itemID', 'rating']]
print(ratings)

ratings.to_csv('doritos/uir_rating.csv', header=False,index=False, encoding='utf-8')