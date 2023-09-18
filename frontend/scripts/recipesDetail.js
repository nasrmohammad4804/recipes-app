const urlParams = new URLSearchParams(location.search);
const id = urlParams.get('id');

let bigSizeRecipesImage = document.querySelector('.aside__recipes__image--big');
let smallSizeRecipesImages = document.querySelectorAll('.aside__recipes__image--small__image');
let recipesTitle = document.querySelector('.main__recipes__title');
let recipesSummary = document.querySelector('.main__recipes__summary');
let recipesDetail = document.querySelector('.main__recipes__detail');

const option = {

    method: "GET",
    headers: {
      "content-type": "application/json",
    },
    credentials: "include",
};

getById();

function getById(){

    let statusCode;

    fetch(`http://127.0.0.1:8080/recipes/${id}`,option)
    .then(response => {
        statusCode= response.status;
        return response.json();
    })
    .then(response => {
        
        if(statusCode!='200')
        throw new Error(response.message);
        
        showRecipe(response.data);
    })
    .catch(err => {

        if(statusCode=='403')
        location.replace("http://127.0.0.1:5500/pages/credentials.html");
    });
};


function showRecipe(data){

    bigSizeRecipesImage.style.backgroundImage=`url(data:${data.imageInfo.contentType};base64,${data.imageInfo.dataEncoded})`;
    
    smallSizeRecipesImages.forEach(value => {
        
        value.style.backgroundImage=`url(data:${data.imageInfo.contentType};base64,${data.imageInfo.dataEncoded})`;

    });

    recipesTitle.textContent=data.title;
    recipesSummary.textContent=data.summary;
    recipesDetail.textContent=data.detail;
}