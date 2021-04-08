import urllib.request
from bs4 import BeautifulSoup as bs
import re
import json, csv

def toJson(recipe_dict):
    with open('recipe.json', 'w', encoding='utf-8') as file :
        json.dump(recipe_dict, file, ensure_ascii=False, indent='\t')


def toCSV(recipe_list):
    with open('ingredients.csv', 'w', encoding='utf-8', newline='') as file :
        csvfile = csv.writer(file)
        for row in recipe_list:
            csvfile.writerow(row)

def url_func(n, m):
    num_range = range(n,m)
    url = "https://www.10000recipe.com/recipe/list.html?order=reco&page="
    url_list = []

    for num in num_range:
        req = urllib.request.Request(url + str(num)) # page 수 붙이기
        code = urllib.request.urlopen(url).read()
        soup = bs(code, "html.parser")

        try:
            res = soup.find(class_='common_sp_list_ul')
            for i in res.find_all('a', {'href':re.compile('^/recipe/[0-9]*')}):
                url_tmp = i.get('href')
                url_list.append(url_tmp)
        except(AttributeError):
            pass
    
    return url_list

# 사이트마다 메뉴 이름, 재료, 레시피 등등 크롤링 후
# json 형식으로 저장
num_id=0
skip_cnt =0
food_dicts = []
ingre_set = set() # 재료 목록들을 담기 위한 set
url_lists = url_func(0,1)
for url_str in url_lists:
    url = "https://www.10000recipe.com"
    url = url + url_str
    req = urllib.request.Request(url)
    code = urllib.request.urlopen(url).read()
    soup = bs(code, "html.parser")

    # 변수목록
    # menu_name : 메뉴 이름
    # menu_img : 메뉴 이미지
    # menu_summary : 메뉴 설명
    # menu_info_1 : n인분
    # menu_info_2 : 요리 시간
    # menu_info_3 : 난이도
    # ingredient_name : 재료 이름
    # ingredient_count : 계랑 숫자
    # ingredient_unit : 계량 단위
    # ingredient_main : 조미료 판단
    # recipe_step_txt : 레시피 순서 txt
    # recipe_step_img : 레시피 순서 img

    info_dict = {}
    ingre_list = []
    ingre_dict = {}
    recipe_list = []
    recipe_dict = {}
    food_dict = {}

    # menu_name
    res = soup.find('div', 'view2_summary')
    res = res.find('h3')
    menu_name = res.get_text()

    # menu_img
    res = soup.find('div', 'centeredcrop')
    res = res.find('img')
    menu_img = res.get('src')

    # menu_summary
    res = soup.find('div', 'view2_summary_in')
    if res!=None:
        menu_summary = res.get_text().replace('\n','').strip()
    menu_summary="?"

    # menu_info
    res = soup.find('span', 'view2_summary_info1') # menu_info_1
    if res!=None:
        menu_info_1 = res.get_text()
    menu_info_1="?"
   
    res = soup.find('span', 'view2_summary_info2') # menu_info_2
    if res!=None:
        menu_info_2 = res.get_text()
    menu_info_2="?"   
    
    res = soup.find('span', 'view2_summary_info3') # menu_info_3
    if res!=None:
        menu_info_3 = res.get_text()
    menu_info_3="?"
    
    # info dict
    info_dict = {"info1":menu_info_1,
                "info2":menu_info_2,
                "info3":menu_info_3}

    # ingredient
    res = soup.find('div','ready_ingre3')
    try :
        for n in res.find_all('ul'):
            for tmp in n.find_all('li'):
                ingredient_name = tmp.get_text().replace('\n','').replace(' ','')
                count = tmp.find('span')
                ingredient_tmp = count.get_text()
                ingredient_name = re.sub(ingredient_tmp, '', ingredient_name) # ingredient_name
                ingredient_unit = ingredient_tmp.replace('/','').replace('+','')
                ingredient_unit = ''.join([i for i in ingredient_unit if not i.isdigit()]) # ingredient_unit
                ingredient_count = re.sub(ingredient_unit, '', ingredient_tmp) # ingredient_count
                # ingre_list
                ingre_dict = {"ingre_name":ingredient_name,
                                "ingre_count":ingredient_count,
                                "ingre_unit":ingredient_unit,}
                ingre_list.append(ingre_dict)

                # set에 업데이트
                ingre_set.add(ingredient_name)
    except(AttributeError):
        pass

    # recipe
    res = soup.find('div','view_step')
    for n in res.find_all('div','view_step_cont'):
        recipe_step_txt = n.get_text().replace('\n',' ')         
        tmp = n.find('img')
        if tmp == None:
            continue
        recipe_step_img = tmp.get('src')

        # recipe_list
        recipe_dict = {"txt":recipe_step_txt,
                        "img":recipe_step_img,}
        recipe_list.append(recipe_dict)

    # 재료 형식에 맞지 않게 올라온 글들 skip
    if not ingre_list:
        print("Skip")
        skip_cnt = skip_cnt + 1
        continue

    num_id = num_id + 1
    food_dict = {"id":num_id,
                "name":menu_name,
                "img":menu_img,
                "summary":menu_summary,
                "info":info_dict,
                "ingre":ingre_list,
                "recipe":recipe_list}

    food_dicts.append(food_dict)

    print('{0:<5}% Success........{1}/{2}'.format((num_id*100/len(url_lists)),num_id,len(url_lists)))

# json 생성
toJson(food_dicts)

# ingredients list csv 생성
ingre_list_csv=[]
for i in ingre_set:
    tmp_l = []
    tmp_l.append(i)
    ingre_list_csv.append(tmp_l)
toCSV(ingre_list_csv)

print("Complete! {0}recipes, {1} skip".format(num_id, skip_cnt))