from flask import Flask
from w2v import Word2v

app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, Everyone. Im TechnoHong, Nice to meet you. I want a piece of Salmon of course, raw fish. but im not bear, im tcnk... P.S. : test recipe id is 6905019 Good LUCK :D'

@app.route('/<rid>', methods = ['GET'])
def sim_rcp(rid):
    param=rid.split('+')
    w3v = Word2v(list(map(int, param)))
    res = w3v.recommendations(0)
    return res


@app.route('/user')
def hello_user():
    return 'Hello user'

if __name__ == '__main__':
    app.run(host="0.0.0.0", port="5000")