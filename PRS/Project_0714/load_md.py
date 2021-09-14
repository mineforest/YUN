from tensorflow import keras
import tensorflow as tf
from gensim.models import Doc2Vec

doc2vec_model = Doc2Vec.load('w2v.model')

print(doc2vec_model)

