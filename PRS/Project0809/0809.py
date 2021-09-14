import time
from surprise import KNNBasic
from surprise import Dataset
from surprise import Reader

from collections import defaultdict
from operator import itemgetter
import heapq
import csv

#https://colab.research.google.com/drive/1x8Hrnxau2ZuvOtkA9gNXblxp2GHe7l5S?usp=sharing#scrollTo=PDjwk2THR0Pz
#w2v와 결합해 성능 향상시키는 방법 찾아보기
from surprise.prediction_algorithms.matrix_factorization import NMF

s_time=time.time()
def load_dataset():
    #r_cols=['itemID', 'userID', 'rating']
    #ratings = pd.read_csv('doritos/new_rating.csv',names=r_cols, header=None, encoding='UTF-8')

    reader = Reader(line_format='user item rating', sep=',', skip_lines=1)
    ratings = Dataset.load_from_file('doritos/uir_rating.csv', reader=reader)

    # Lookup a movie's name with it's Movielens ID as key
    itemID_to_name = {}
    with open('doritos/new_recipe_tags.csv', newline='', encoding='UTF-8') as csvfile:
            rcp_reader = csv.reader(csvfile)
            next(rcp_reader)
            for row in rcp_reader:
                itemID = int(row[0])
                item_name = row[1]
                itemID_to_name[itemID] = item_name
    # Return both the dataset and lookup dict in tuple
    return (ratings, itemID_to_name)

dataset, itemID_to_name = load_dataset()
trainset = dataset.build_full_trainset()

similarity_matrix = KNNBasic(sim_options={
        'name': 'cosine',
        'user_based': False
        })\
        .fit(trainset)\
        .compute_similarities()
print(similarity_matrix)
#1018, 즉 유저수까지만 생성됨 왜? -> 레시피수는 1018까지라 정확한 추천 힘듬

k = 10000

#test_subject = '1486'
#test_subject_iid = trainset.to_inner_uid(test_subject)
#print(test_subject_iid)
# Get the top K items we rated
#test_subject_ratings = trainset.ur[test_subject_iid]

test_subject_ratings = [(3,5.0),(14,5.0),(22,5.0),(31,5.0),(6189,5.0),(6081,5.0),(5758,5.0),(5695,5.0),(5233,5.0)]
k_neighbors = heapq.nlargest(k, test_subject_ratings, key=lambda t: t[1])


#innerID == i
#score
candidates = defaultdict(float)
for itemID, rating in k_neighbors:
    try:
      similaritities = similarity_matrix[itemID]
      for innerID, score in enumerate(similaritities):
          candidates[innerID] += score * (rating / 5.0)
    except:
      continue


# Utility we'll use later.
def getMovieName(itemID):
  if int(itemID) in itemID_to_name:
    return itemID_to_name[int(itemID)]
  else:
      return ""

# Build a dictionary of movies the user has watched
watched = {}
for itemID, rating in test_subject_ratings:
  watched[itemID] = 1
print(watched)

recommendations = []
position = 0
for itemID, rating_sum in sorted(candidates.items(), key=itemgetter(1), reverse=True):
  if not itemID in watched:
    tmp=getMovieName(trainset.to_raw_iid(itemID))
    #if(tmp=="") : continue
    recommendations.append(tmp)
    position += 1
    if (position > 30): break # We only want top 10

for rec in recommendations:
  print("recipe: ", rec)

print("time :", time.time() - s_time)
""""""