import firebase_admin
from firebase_admin import credentials, db

cred = credentials.Certificate("/경로/생성된 비공개키 파일명.json")
firebase_admin.initialize_app(cred,{
    'databaseURL' : "파이어베이스 Realtime Database 경로.com",    
    }    
)

class FirebaseConnect:
    def __init__(self, key):
        self.key=key
           
    def getHistory(self):
        dict = db.reference('history').child(self.key).get()
        if dict==None:
            return []
        return [(i['rcp_id'],i['rate']) for i in dict.values()]
    
    def getAllergying(self):
        allergy = db.reference('allergy').child(self.key).get()
        if allergy==None:
            return []
        return [i['allergy'] for i in allergy.values()]
    
    def getPrefer(self):
        prefer = db.reference('preference').child(self.key).get()
        if prefer==None:
            return []
        return [i['preference'] for i in prefer.values()]
