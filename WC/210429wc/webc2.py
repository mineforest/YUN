import urllib.request
from urllib.request import urlretrieve
from bs4 import BeautifulSoup as bs
import re
import pandas as pd
import os
import csv

def url_func(n, m):
    num_range = range(n,m)
    url = "https://www.10000recipe.com/recipe/list.html?order=reco&page="
    url_list = []

    for num in num_range:
        code = urllib.request.urlopen(url+str(num))
        soup = bs(code, "html.parser",from_encoding='utf-8')

        try:
            res = soup.find(class_='common_sp_list_ul')
            for i in res.find_all('a', {'href':re.compile('^/recipe/[0-9]*')}):
                url_tmp = i.get('href')
                url_list.append(url_tmp)
        except(AttributeError):
            pass

    return url_list
cnt=0
num_id=0
skip_cnt =0
url_lists = url_func(1,25)
for url_str in url_lists:
    url = "https://www.10000recipe.com"
    url = url + url_str
    code = urllib.request.urlopen(url) #.read?
    soup = bs(code, "html.parser",from_encoding='utf-8')
    
    #path = "./user/" + str(num_id)
    #if not os.path.isdir(path):
    #    os.mkdir(path)

    recipe_id=url_str.replace('/recipe/','')

    for n in soup.find_all(class_='media reply_list'):
        res = n.find(class_='media-left')
        res = res.find('a')['href']
        res = res.replace('/profile/review.html?uid=','') 
            
        res2 = n.find(class_='reply_list_star')
        cnt1 = 0
        for i in res2.find_all('img',{'src':'https://recipe1.ezmember.co.kr/img/mobile/icon_star2_on.png'}):
            cnt1 = cnt1 + 1
        if not os.path.exists('rating.csv'):
            with open("rating.csv",'w', newline='',encoding='utf-8') as f2:
                wr2 = csv.writer(f2)
                wr2.writerow([recipe_id, res, cnt1])
        else:
            with open("rating.csv",'a', newline='',encoding='utf-8') as f2:
                wr2 = csv.writer(f2)
                wr2.writerow([recipe_id, res, cnt1])