from keras.models import Sequential
from keras.layers import Dense
import numpy as np
from keras.optimizers import SGD

# Build dataset
input_data = np.array([[40, 20], [60, 40], [80, 60]])
output_data = np.array([[0, 0, 1], [0, 1, 0], [1, 0, 0]])  # cloth  파카 재킷 비옷

seed = 7
np.random.seed(seed)

model = Sequential()
model.add(Dense(8, input_dim=2, activation='sigmoid'))
model.add(Dense(256, input_dim=2, activation='sigmoid'))
model.add(Dense(3, activation='softmax'))

model.compile(loss='categorical_crossentropy', optimizer='adam', metrics=['accuracy'])

# Fit the model
model.fit(input_data, output_data, epochs=1000, batch_size=32, verbose=0)

# evaluate the model
loss, accuracy = model.evaluate(input_data, output_data)

# calculate predictions
predictions = model.predict(input_data)
print(np.round(predictions))