const createRecipesForm = document.querySelector('.main__form');

const recipesCategories= document.getElementById('recipes-categories');

const option = {

    method: "GET",
    headers: {
      "content-type": "application/json",
    },
    credentials: "include",
};

getRecipesCategory();

function getRecipesCategory(){

    let statusCode;

    fetch("http://127.0.0.1:8080/recipes-category/all",option)
    .then(response => {
        statusCode=response.status;
        return response.json();
    })
    .then(response => {

        if(statusCode!='200')
        throw new Error(response.message);


        setRecipesCategoryInDropDown(response.data);

    })
    .catch(err => {
        if(statusCode=='403')
        location.replace("http://127.0.0.1:5500/pages/credentials.html");
    })
}

function setRecipesCategoryInDropDown(categories){
    

    categories.forEach(element => {
        const option = document.createElement('option');
        option.value=element.id;
        option.textContent=element.title;

        recipesCategories.append(option);

    });
}
createRecipesForm.addEventListener('submit', e => {
    e.preventDefault();

    const {title,summary,detail,file} = e.target;

    console.log(recipesCategories.value);

    let formData= new FormData();
    
    formData.append('title',title.value);
    formData.append('summary',summary.value);
    formData.append('detail',detail.value);
    formData.append('file',file.files[0]);

    const option = {
        method:"POST",
        credentials:"include",
        body:formData
      };

      let statusCode;

    fetch(`http://127.0.0.1:8080/recipes/${recipesCategories.value}`,option)
    .then(response => {
        statusCode= response.status;
        return response.json();
    })
    .then(response => {
        if(statusCode!='200')
            throw new Error(response.message);

            console.log('created  recipe!');
           createMovementBox();
    })
    .catch(err => {
        if(statusCode=='403')
        location.replace("http://127.0.0.1:5500/pages/credentials.html");
    })
})

function createMovementBox(){

    let parentName = 'main__form';

    let movementBox =document.createElement('div');

    movementBox.classList.add(`${parentName}__movement`);
    movementBox.classList.add(`${parentName}__movement--successful`);
    movementBox.textContent='create recipes successfully';

    createRecipesForm.append(movementBox);

    setTimeout(() => {
        movementBox.style.display='none';
    },2500);
}