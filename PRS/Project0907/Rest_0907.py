from flask import Flask
app = Flask (__name__)

@app.route('/')
def hello_world():
    return 'Hello, flasks!'

@app.route('/user')
def hello_user():
    return 'Hello, User!' 

if __name__ == "__main__":
    app.run()