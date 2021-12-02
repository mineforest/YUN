from flask import Flask
from w2v_new import Word2v
from getData import FirebaseConnect
app = Flask(__name__)

@app.route('/')
def hello_world():
    return 'Hello, Everyone. Im TechnoHong, Nice to meet you. I want a piece of Salmon of course, raw fish. but im not bear, im tcnk... P.S. : test recipe id is 6905019 Good LUCK :D'

@app.route('/rcp/<rid>', methods = ['GET'])
def sim_rcp(rid):
    param=rid.split('+')
    w3v = Word2v(list(map(int, param)),[],[])
    res = w3v.recommendations()
    return res

@app.route('/user/<uid>', methods = ['GET'])
def user_hist_base(uid):    
    fbc = FirebaseConnect(uid)
    forRateRcp=fbc.getHistory()
    param=[]
    if forRateRcp:
        for i in forRateRcp:
            param=param+[i[0] for j in range(i[1]-2) if i[1]>2]    
    w3v = Word2v(list(map(int, param)),fbc.getAllergying(),fbc.getPrefer())
    res = w3v.recommendations()
    return res

if __name__ == '__main__':
    app.run(host="127.0.0.1", port="5000")