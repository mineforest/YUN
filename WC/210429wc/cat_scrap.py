import urllib.request
from urllib.request import urlretrieve
from bs4 import BeautifulSoup as bs
import re
import pandas as pd
import os
import csv


def cat_scrap_id(url, infolist, fpage, lpage):
    lcat = infolist[0]
    scat = infolist[1]
    catstr = infolist[2]
    page_range = range(fpage, lpage)


    index = url.find(lcat) + 5 #cat?=
    cat_url = url[:index] + scat + url[index:]
    
    for num in page_range:
        code = urllib.request.urlopen(url+str(num))
        soup = bs(code, "html.parser", from_encoding='utf-8')
        
        try:
            res = soup.find(class_='common_sp_list_ul')
            for i in res.find_all('a', {'href':re.compile('^/recipe/[0-9]*')}):
                url_tmp = i.get('href')
                url_tmp = re.sub('/recipe/','',url_tmp)
                tocsv('cate.csv', url_tmp, catstr)
                print(url_tmp + ", " + catstr + " DF 추가완료")
        except(AttributeError):
            pass

def tocsv(title, str1, str2):
    if not os.path.exists(title):
        f = open(title,'w', newline='',encoding='utf-8')
        wr = csv.writer(f)
        wr.writerow([str1, str2])
    else:
        f = open(title,'a', newline='',encoding='utf-8')
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