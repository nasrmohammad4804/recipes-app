let userInfo;
let recipesCategories;
let recipesInfos;


let personImage = document.querySelector('.aside__avatar');
let personName = document.querySelector('.aside__name');
let personEmail = document.querySelector('.aside__email');


let recipesMenu = document.querySelector('.main__menu');
let recipesItem = document.querySelector('.main__recipes');

const option = {
    method:"GET",
    headers:{
        'content-type':"application/json"
    },
    credentials:"include"
};

const promises = [getUserInfo(),getRecipesCategories(),getAllRecipes()]; 

Promise.all(promises)
.then(values => {
    
    const userInfo = values[0];
    const recipesCategories = values[1];
    const allRecipes = values[2];

    showUserInfo(userInfo);
    showRecipesCategories(recipesCategories);
    showAllRecipes(allRecipes);    
})
.catch(err => {
});

function showUserInfo(userInfoPromise){
    

    userInfoPromise.json()
    .then(response => {

        userInfo=response.data;

        personName.textContent=userInfo.firstName;
        personEmail.textContent=userInfo.email;

        if(userInfo.avatar == null){
            personImage.setAttribute('src','../images/default-avatar.png');
        }
        else {
            personImage.src= `data:${userInfo.avatar.contentType};base64,${userInfo.avatar.dataEncoded}`;
        }
    });

}

function showRecipesCategories(recipesCategoriesPromise){
    
    let parent='main__menu';

    
    recipesCategoriesPromise.json()
    .then(response => {
        recipesCategories = response.data; 

        createDefaultRecipesCategories(parent);
        recipesCategories.forEach(category => {
            
            let element = document.createElement('div');
            element.id=`menu-${category.id}`;
            element.textContent=category.title;
            element.className=`${parent}__item`;
            element.style.textAlign='center';

            recipesMenu.append(element);
        });
    });
}

function createDefaultRecipesCategories(parentClassName){
    let element = document.createElement('div');
    element.id='menu-All';
    element.classList.add(`${parentClassName}__item--default`);
    element.textContent='All';


    recipesMenu.append(element);
}

function showAllRecipes(recipesPromise){

    let parent = 'main__recipes';

    recipesPromise.json()
    .then(response => {
        
        recipesInfos= response.data;

        recipesInfos.forEach(info => {
            addRecipesItem(info,parent);
        });
    });
}

function addRecipesItem(info , parent){

    let item = document.createElement('div');
    item.className=`${parent}__item`;
    item.id=`recipes-${info.id}`;

    let imageBlock = document.createElement('img');
    imageBlock.className=`${parent}__item__image`;
    imageBlock.src=`data:${info.imageInfo.contentType};base64,${info.imageInfo.dataEncoded}`;


    let content = document.createElement('div');
    content.className=`${parent}__item__content`;

    let title =document.createElement('h3');
    title.className=`${parent}__item__content__title`;
    title.textContent=info.title;

    let summary= document.createElement('p');
    summary.className=`${parent}__item__content__summary`;
    summary.textContent=info.summary;
    
    content.append(title,summary);


    item.append(imageBlock,content);

    recipesItem.append(item);
}
function getUserInfo(){

    return fetch("http://127.0.0.1:8080/user-info",option);
};


function getRecipesCategories(){

    return fetch("http://127.0.0.1:8080/recipes-category/all",option);
};

function getAllRecipes(){

    return fetch("http://127.0.0.1:8080/recipes/all",option);
}
