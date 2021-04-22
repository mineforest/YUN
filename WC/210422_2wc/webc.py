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

num_id=0
skip_cnt =0
url_lists = url_func(1,25)
for url_str in url_lists:
    url = "https://www.10000recipe.com"
    url = url + url_str
    code = urllib.request.urlopen(url) #.read?
    soup = bs(code, "html.parser",from_encoding='utf-8')

    path = "./data/" + str(num_id)
    if not os.path.isdir(path):
        os.mkdir(path)

    ingre_list = []
    ingre_cat_list = []
    recipe_list = []

    res = soup.find('div', 'view2_summary') # 레시피 이름
    res = res.find('h3')
    menu_name = res.get_text()

    res = soup.find('div', 'centeredcrop')
    if res != None:
        res = res.find('img')
        menu_img = res.get('src')
        tmp_menu_img = path + "/main.jpg"
        urlretrieve(menu_img, tmp_menu_img)

    res = soup.find('span', 'view2_summary_info1') # 인분
    if res==None:
        menu_info_1 = "NaN"
    else:
        menu_info_1 = res.get_text()
   
    res = soup.find('span', 'view2_summary_info2') # 조리시간
    if res==None:
        menu_info_2 = "NaN"   
    else:
        menu_info_2 = res.get_text()
    
    res = soup.find('span', 'view2_summary_info3') # 난이도
    if res==None:
        menu_info_3 = "NaN"
    else:
        menu_info_3 = res.get_text()

    res = soup.find('div','ready_ingre3')           #재료
    if res == None:
        print("skip")
        continue
    try :
        for n in res.find_all('ul'):
            ingre_cat = n.find('b').get_text()
            for tmp in n.find_all('li'):
                try:
                    ingredient_name = tmp.get_text().replace('\n','').replace(' ','')
                    count = tmp.find('span')
                    ingredient_tmp = count.get_text()
                    ingredient_name = re.sub(ingredient_tmp, '', ingredient_name) # ingredient_name
                    ingredient_unit = ingredient_tmp.replace('/','').replace('+','')
                    ingredient_unit = ''.join([i for i in ingredient_unit if not i.isdigit()]) # ingredient_unit
                    ingredient_count = re.sub(ingredient_unit, '', ingredient_tmp) # ingredient_count

                    ingre_dict = {"ingre_name":ingredient_name,
                                "ingre_count":ingredient_count,
                                "ingre_unit":ingredient_unit}
                    ingre_list.append(ingre_dict)
                except:
                    pass
                ingre_cat_dict = {"ingre_cat":ingre_cat,
                                    "ingre_list":ingre_list}
                ingre_cat_list.append(ingre_cat_dict)
                ingre_list = []
    except(AttributeError):
        pass # 다른 태그 재료 크롤링, 단위 똑바로 구분

    res = soup.find('div','view_step')
    num=1
    for n in res.find_all('div','view_step_cont'):
        recipe_step_txt = n.get_text().replace('\n',' ')
        tmp = n.find('img')
        if tmp == None:
            num=num+1
            continue
        tmp = tmp.get('src')
        tmp2 = path + "/" + str(num) + ".jpg"
        urlretrieve(tmp, tmp2)
        num=num+1
        # recipe_list
        recipe_list.append(recipe_step_txt)
    with open(path+"/" + str(num_id) + ".csv",'w', newline='',encoding='utf-8') as f:
        wr = csv.writer(f)
        wr.writerow([menu_name, menu_info_1, menu_info_2, menu_info_3, ingre_cat_list, recipe_list])


    
    if not os.path.exists('recipe.csv'):
        with open("recipe.csv",'w', newline='',encoding='utf-8') as f2:
            wr2 = csv.writer(f2)
            wr2.writerow([menu_name, menu_info_1, menu_info_2, menu_info_3, ingre_cat_list, recipe_list])
    else:
        with open("recipe.csv",'a', newline='',encoding='utf-8') as f2:
            wr2 = csv.writer(f2)
            wr2.writerow([menu_name, menu_info_1, menu_info_2, menu_info_3, ingre_cat_list, recipe_list])
    
    if not os.path.exists('recipe_title.csv'):
        with open("recipe_title.csv",'w', newline='',encoding='utf-8') as f2:
            wr2 = csv.writer(f2)
            wr2.writerow([menu_name])
    else:
        with open("recipe_title.csv",'a', newline='',encoding='utf-8') as f2:
            wr2 = csv.writer(f2)
            wr2.writerow([menu_name])
    
    if not os.path.exists('recipe_recipe.csv'):
        with open("recipe_recipe.csv",'w', newline='',encoding='utf-8') as f2:
            wr2 = csv.writer(f2)
            wr2.writerow([recipe_list])
    else:
        with open("recipe_recipe.csv",'a', newline='',encoding='utf-8') as f2:
            wr2 = csv.writer(f2)
            wr2.writerow([recipe_list])
    
    num_id = num_id + 1

    print('{0:<5}% Success........{1}/{2}'.format((num_id*100/len(url_lists)),num_id,len(url_lists)))

print("Complete!")