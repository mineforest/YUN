import firebase_admin
from firebase_admin import credentials, db

cred = credentials.Certificate("tcnk-e50ef-firebase-adminsdk-ydhen-a568e6740f.json")
firebase_admin.initialize_app(cred,{
    'databaseURL' : "https://tcnk-e50ef-default-rtdb.firebaseio.com",    
    }    
)

class FirebaseConnect:
    def __init__(self, key):
        self.key=key
                    
    def getDataa(self):
        dict = db.reference('history').child(self.key).get()
        return [(i['rcp_id'],i['rate']) for i in dict.values()]
    
