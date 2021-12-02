import requests
from bs4 import BeautifulSoup
from urllib.parse import urljoin
import re
import json
import urllib.request
import collections
import os
import csv

# 재료 정보가 담겨있는 json 파일 확인 => 각 재료에 고유의 id를 넣는 dict생성
with open('./js/ingre_test.json', 'r',encoding='UTF-8') as f:
    json_data = json.load(f)
    
new_dict = {}
for i in range(len(json_data)):
    new_list = []
    for k,v in json_data[i].items():
        new_list.append(v)
    new_dict[new_list[3]] = new_list[0]

with open('./js/0720tag.json', 'r',encoding='UTF-8') as f2:
    json_data2 = json.load(f2)
    
new_dict2 = {}
for i in range(len(json_data2)):
    new_list2 = []
    for k,v in json_data2[i].items():
        new_list2.append(v)
    new_dict2[new_list2[0]] = new_list2[1]

# dict형태를 json 파일로 로컬에 저장
def toJson(recipe_dict, title):
    file_path = './0720recipe/' + title + '.json'
    with open(file_path, 'w', encoding='utf-8') as file:
        json.dump(recipe_dict, file, ensure_ascii=False, indent='\t')

def toCsv(title,ingre_name_list,tag):
        if not os.path.exists('./0720title+ingre+tag/title+ingre+tag.csv'):
            with open("./0720title+ingre+tag/title+ingre+tag.csv",'w', newline='',encoding='utf-8') as f2:
                wr2 = csv.writer(f2)
                wr2.writerow([title, ingre_name_list, tag])
        else:
            with open("./0720title+ingre+tag/title+ingre+tag.csv",'a', newline='',encoding='utf-8') as f2:
                wr2 = csv.writer(f2)
                wr2.writerow([title, ingre_name_list, tag])

# 레시피 각각의 url 정보 파싱
def url_func(n,m):
    url = 'https://www.10000recipe.com/recipe/list.html?order=reco&page='
    url_list = []
    
    for num in range(n,m):
        n_nrl = url + str(num)
        print(n_nrl)
        response = requests.get(n_nrl)
        html = response.text
        soup = BeautifulSoup(html, 'html.parser')
        
        try:
            res = soup.find(class_='common_sp_list_ul ea4')
            a = 1
            for i in res.find_all('a','common_sp_link'):
                url_tmp = i.get('href')
                print(num,a)
                a += 1
                url_list.append(url_tmp)
        except(AttributeError):
            pass
    return url_list

# def passlist(pslist):
#     counter = collections.Counter(pslist)
#     counter = counter.most_common(len(counter))
#     file = open('ingre_without.txt', 'w',encoding='utf-8')
#     vstr =''
#     for a in counter:
#         vstr = vstr + str(a) + '\n'
#     file.writelines(vstr)
#     file.close()


url_lists= url_func(701,750)
pl = []
num_id=0
#각 레시피 상세페이지에서 데이터 크롤링
for url_str in url_lists:
    id_title = url_str.replace('/recipe/','')

    if os.path.isfile('./0720recipe/'+id_title+'.json'):
        num_id=num_id+1
        print(id_title+'/done')
        print('{0:<5}% Success........{1}/{2}'.format((num_id*100/len(url_lists)),num_id,len(url_lists)))
        continue

    url = 'https://www.10000recipe.com'
    url = url + url_str
    response = requests.get(url)
    html = response.text
    soup = BeautifulSoup(html, 'html.parser')

    tag=[]
    ingre_name_list = []



    if int(id_title) in new_dict2.keys():
        tag.append(new_dict2.get(int(id_title)))
    else:
        print("SKIP")    
        continue

    try:
        #menu name
        res = soup.find('div', 'view2_summary')

        if not(res):
            continue
        res = res.find('h3')
        menu_name = res.get_text().replace('\n',' ').replace('"','')
        menu_name = re.sub('[-=.#/?:$}♥♡*~※☆★]', '', menu_name)
        #menu image
        res = soup.find('div', 'centeredcrop')
        if not(res):
            continue
        res = res.find('img')
        menu_img = res.get('src')

        # ingredients
        res = soup.find('div','ready_ingre3')
        if not(res):
            continue
        ingre_dict = {}
        sauce_dict ={}
        ingre_list = []
        sauce_list =[]
        new_id = []
        

        try:
            for n in res.find_all('ul'):
                ingre_cat = n.find('b').get_text()
                if '재료' in ingre_cat:
                    for tmp in n.find_all('li'):
                        ingredient_name = tmp.get_text().replace('\n','').replace(' ','')
                        count = tmp.find('span')
                        if not(count):
                            continue
                        ingredient_tmp = count.get_text()
                        ingredient_name = re.sub(ingredient_tmp, '',ingredient_name)
                        ingredient_unit = ingredient_tmp.replace('/','').replace('+','')
                        ingredient_unit = ''.join([i for i in ingredient_unit if not i.isdigit()])
                        ingredient_count = re.sub(ingredient_unit, '', ingredient_tmp)
                        ingredient_unit = ingredient_unit.replace('숟갈','T').replace('수저','T').replace('스푼','T').replace('t','T').replace('큰술','T').replace('~','').replace('.','').replace('-','')

                        ingre_dict = {"ingre_name": ingredient_name,
                                     "ingre_count": ingredient_count,
                                     "ingre_unit": ingredient_unit}
                        if ingredient_name in new_dict.keys():
                            new_id.append(new_dict[str(ingredient_name)])
                            ingre_name_list.append(ingredient_name)

                        else:
                            pl.append(ingredient_name)
                        ingre_list.append(ingre_dict)
                elif '양념' in ingre_cat or'소스' in ingre_cat:
                    for tmp in n.find_all('li'):
                        ingredient_name = tmp.get_text().replace('\n','').replace(' ','')
                        count = tmp.find('span')
                        if not(count):
                            continue
                        ingredient_tmp = count.get_text()
                        ingredient_name = re.sub(ingredient_tmp, '',ingredient_name)
                        ingredient_unit = ingredient_tmp.replace('/','').replace('+','')
                        ingredient_unit = ''.join([i for i in ingredient_unit if not i.isdigit()])
                        ingredient_count = re.sub(ingredient_unit, '', ingredient_tmp)
                        ingredient_unit = ingredient_unit.replace('숟갈','T').replace('수저','T').replace('스푼','T').replace('t','T').replace('큰술','T').replace('~','').replace('.','').replace('-','')

                        sauce_dict = {"ingre_name": ingredient_name,
                                     "ingre_count": ingredient_count,
                                     "ingre_unit": ingredient_unit}
                        sauce_list.append(sauce_dict)
                        ingre_name_list.append(ingredient_name)
        except:
            pass

        # 요리시간
        res = soup.find('span', 'view2_summary_info2')
        if not(res):
            continue
        time = res.get_text().replace('이내','').replace('분','').replace(' ','').replace('시간','')

        # 조리 방법+이미지
        recipe_list = []
        recipe_dict = []
        recipe_imges = []
        res = soup.find('div','view_step')
        if not(res):
            continue
        ind = 1
        for n in res.find_all('div', 'view_step_cont', 'media-body'):
            recipe_step_txt = n.get_text().replace('\n',' ')
            recipe_dict = {"txt":recipe_step_txt}
            recipe_list.append(str(ind) + '. ' + recipe_step_txt)
            ind += 1

        for n in res.find_all('div', 'view_step_cont', 'media-right'):
            res = n.find('img')
            if not(res):
                continue
            recipe_imges.append(res.get('src'))


        food_dict = {"id":id_title,
                    "name":menu_name,
                    "thumbnail":menu_img,
                    "url":url,
                     "ingredient_ids":new_id,
                    "time":time,
                    "ingre_list":ingre_list,
                    "sauce_list":sauce_list,
                    "recipe":recipe_list,
                    "recipe_img":recipe_imges,
                    "tag":tag}

        if(len(ingre_list)==len(new_id)):
            num_id = num_id + 1
            toJson(food_dict, id_title)
            toCsv(menu_name,ingre_name_list,tag)
            print(id_title+'/done')
        else:
            print(id_title+'/pass')
        
        print('{0:<5}% Success........{1}/{2}'.format((num_id*100/len(url_lists)),num_id,len(url_lists)))
    except:
            pass
print('완료')
#passlist(pl)

