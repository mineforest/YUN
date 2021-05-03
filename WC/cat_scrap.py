import urllib.request
from urllib import parse
from urllib.request import urlretrieve
from bs4 import BeautifulSoup as bs
import re
import pandas as pd
import os
import csv

def in_cat_url(url, lower_cat, fpage, lpage):
    page_range = range(fpage, lpage)
    print(lower_cat + " start")
    for num in page_range:
        code = urllib.request.urlopen(url + '&order=reco&page=' + str(num))
        soup = bs(code, "html.parser", from_encoding='utf-8')
        try:
            res = soup.find(class_= 'rcp_m_list2')
            res = res.find(class_= 'common_sp_list_ul')
            for i in res.find_all('a', {'href':re.compile('^/recipe/[0-9]*')}): # 이라인
                url_tmp = i.get('href')
                url_tmp = re.sub('/recipe/','',url_tmp)
                tocsv(lower_cat+'.csv', url_tmp, lower_cat)
        except(AttributeError):
            pass

def cat_scrap_id(url, infolist, fpage, lpage):
    lcat = infolist[0]
    scat = infolist[1]
    catstr = infolist[2]
    page_range = range(fpage, lpage)


    index = url.find(lcat) + 5 #cat?=
    cat_url = url[:index] + scat + url[index:]
    
    code = urllib.request.urlopen(cat_url)
    soup = bs(code, "html.parser", from_encoding='utf-8')
    res = soup.find(class_='tag_cont')
    
    for i in res.find_all('a'):
        lower_cat = i.get_text()
        lower_url = 'https://www.10000recipe.com/recipe/list.html?q=' + parse.quote(lower_cat)
        if not os.path.exists('./cate/' + lower_cat + '.csv'):
            in_cat_url(lower_url, lower_cat, fpage, lpage)
        else:
            print(lower_cat + " 지나갈게요")
    if '/' in catstr:
        catstr = catstr.replace("/","+")
    if not os.path.exists('./cate/' + catstr + '.csv'):
        print(catstr + " start")
        for num in page_range:
            code = urllib.request.urlopen(cat_url+str(num))
            soup = bs(code, "html.parser", from_encoding='utf-8')
            
            try:
                res = soup.find(class_='common_sp_list_ul')
                for i in res.find_all('a', {'href':re.compile('^/recipe/[0-9]*')}):
                    url_tmp = i.get('href')
                    url_tmp = re.sub('/recipe/','',url_tmp)
                    tocsv(catstr+'.csv', url_tmp, catstr)
    #               print(url_tmp + ", " + catstr + " DF 추가완료")
            except(AttributeError):
                pass
    else:
        print(catstr + " 지나갈게요")

def tocsv(title, str1, str2):
    if not os.path.exists('./cate/' + title):
        f = open('./cate/'+title,'w', newline='',encoding='utf-8')
        wr = csv.writer(f)
        wr.writerow([str1, str2])
    else:
        f = open('./cate/'+title,'a', newline='',encoding='utf-8')
        wr = csv.writer(f)
        wr.writerow([str1, str2])

info_list = []
fpage = 1
lpage = 35

url = "https://www.10000recipe.com/recipe/list.html?q=&query=&cat1=&cat2=&cat3=&cat4=&fct=&order=reco&lastcate=cat4&dsearch=&copyshot=&scrap=&degree=&portion=&time=&niresource=&page="
code = urllib.request.urlopen(url)
soup = bs(code, "html.parser", from_encoding='utf-8')


res = soup.find(class_='rcp_cate')
for i in res.find_all('a'):
    tmp = i.get('href')
    tmp2 = i.get_text()
    tmp = re.sub('javascript:goSearchRecipe\(\'', '', tmp)
    cat_tmp = re.sub('\',\'.*','',tmp)
    cat_num = re.sub('^.*\'\,\'','',tmp)
    cat_num = re.sub('\'\)','',cat_num)
    inf_list = [cat_tmp, cat_num, tmp2]
    info_list.append(inf_list)

for n in info_list:
    if n[2] == "전체" or n[2] == "기타":
        continue
    else:
        cat_scrap_id(url, n, fpage,lpage)

print("컴플리트")