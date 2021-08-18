import pandas as pd

from surprise import KNNBasic
from surprise import Dataset
from surprise import Reader

from collections import defaultdict
from operator import itemgetter
import heapq

import os
import csv


def load_dataset():
    reader = Reader(line_format='user item rating timestamp', sep=',', skip_lines=1)
    ratings_dataset = Dataset.load_from_file('mapped_rating.csv', reader=reader)
    # r_cols=['itemID', 'userID', 'rating']
    # ratings = pd.read_csv('mapped_rating.csv',names=r_cols, header=None, encoding='UTF-8')
    # print(ratings)
    # Lookup a movie's name with it's Movielens ID as key
    movieID_to_name = {}
    with open('ml-latest-small/movies.csv', newline='', encoding='ISO-8859-1') as csvfile:
        movie_reader = csv.reader(csvfile)
        next(movie_reader)
        for row in movie_reader:
            movieID = int(row[0])
            movie_name = row[1]
            movieID_to_name[movieID] = movie_name
    # Return both the dataset and lookup dict in tuple
    return (ratings_dataset, movieID_to_name)


dataset, movieID_to_name = load_dataset()

# Build a full Surprise training set from dataset
trainset = dataset.build_full_trainset()

# Build a full Surprise training set from dataset
trainset = dataset.build_full_trainset()

similarity_matrix = KNNBasic(sim_options={
        'name': 'cosine',
        'user_based': False
        })\
        .fit(trainset)\
        .compute_similarities()

test_subject = '70'

# Get the top K items user rated
k = 20


# When using Surprise, there are RAW and INNER IDs.
# Raw IDs are the IDs, strings or numbers, you use when
# creating the trainset. The raw ID will be converted to
# an unique integer Surprise can more easily manipulate
# for computations.
#
# So in order to find an user inside the trainset, you
# need to convert their RAW ID to the INNER Id. Read
# here for more info https://surprise.readthedocs.io/en/stable/FAQ.html#what-are-raw-and-inner-ids
test_subject_iid = trainset.to_inner_uid(test_subject)

# Get the top K items we rated
test_subject_ratings = trainset.ur[test_subject_iid]
k_neighbors = heapq.nlargest(k, test_subject_ratings, key=lambda t: t[1])

# Default dict is basically a standard dictionary,
# the difference beeing that it doesn't throw an error
# when trying to access a key which does not exist,
# instead a new entry, with that key, is created.
candidates = defaultdict(float)

for itemID, rating in k_neighbors:
    try:
      similaritities = similarity_matrix[itemID]
      for innerID, score in enumerate(similaritities):
          candidates[innerID] += score * (rating / 5.0)
    except:
      continue

# Utility we'll use later.
def getMovieName(movieID):
  if int(movieID) in movieID_to_name:
    return movieID_to_name[int(movieID)]
  else:
      return ""

# Build a dictionary of movies the user has watched
watched = {}
for itemID, rating in trainset.ur[test_subject_iid]:
  watched[itemID] = 1

# Add items to list of user's recommendations
# If they are similar to their favorite movies,
# AND have not already been watched.
recommendations = []

position = 0
for itemID, rating_sum in sorted(candidates.items(), key=itemgetter(1), reverse=True):
  if not itemID in watched:
    recommendations.append(getMovieName(trainset.to_raw_iid(itemID)))
    position += 1
    if (position > 10): break # We only want top 10

for rec in recommendations:
  print("Movie: ", rec)