let currentForm = "Sign In";

let showConfirmPassword = document.getElementById("show-confirm-password");
let hideConfirmPassword = document.getElementById("hide-confirm-password");
let passwordConfirmInput = document.getElementById("password-confirm-input");

let signupShowPassowrd = document.getElementById("signup-show-password");
let signupHidePassowrd = document.getElementById("signup-hide-password");
let signupPasswordInput = document.getElementById("signup-password-input");

let signinShowPassword = document.getElementById("signin-show-password");
let signinHidePassword = document.getElementById("signin-hide-password");
let signinPasswordInput = document.getElementById("signin-password-input");

let signupForm = document.getElementById("signup-form");
let signinForm = document.getElementById("signin-form");

let authBoxex = document.querySelectorAll(".main__section__form__auth-box");
let passwordGroup = document.querySelectorAll(".password-group");


let signupFirstNameError= document.getElementById('signup-first-name-error');
let signupLastNameError= document.getElementById('signup-last-name-error');
let signupPasswordError =document.getElementById('signup-password-error');
let signupConfirmPasswordError = document.getElementById('signup-password-confirm-error');
let signupAgreeError = document.getElementById('signup-agree-error');



let signInFormServerError = document.getElementById('signin-form-submit-server-error');
let signupFormServerError = document.getElementById('signup-form-submit-server-error');

authBoxex.forEach((authBox) => {
  authBox.addEventListener("click", (e) => {
    e.preventDefault();

    const targetElement = e.target;

    let value = targetElement.textContent;

    if (value == "Sign In" && value != currentForm) {
      currentForm = "Sign In";
      signupForm.style.display = "none";
      signinForm.style.display = "block";
    } else if (value == "Sign Up" && value != currentForm) {
      currentForm = "Sign Up";
      signinForm.style.display = "none";
      signupForm.style.display = "block";
    }
  });
});

passwordGroup.forEach((password) => {
  password.addEventListener("click", (e) => {
    let target = e.target;
    if (currentForm == "Sign In") {
      initializeSigninPasswordVisibility(target);
    } else {
      initializeSignupPasswordVisibility(target);
    }
  });
});

signupForm.addEventListener("submit", (e) => {
  e.preventDefault();

  let result = checkSignUpFormIsValid(e.target);

  if(!result)
    return;

  const {firstName,lastName,email,password}  = e.target;

  const body = {
    firstName : firstName.value,
    lastName : lastName.value,
    email : email.value,
    password : password.value
  };

  const option = {

    method:"POST",
    credentials:'include',
    body:JSON.stringify(body),
    headers:{
      'content-type':"application/json"
    }
  };
  
  let statusCode;
  fetch("http://127.0.0.1:8080/sign-up",option)
  .then(res =>  {

    statusCode=res.status;
    return res.json();
  })
  .then(data => {

    if(statusCode!='201')
      throw new Error(data.message);

    location.replace("http://127.0.0.1:5500/pages/home.html");
  })
  .catch(error => {
    signupFormServerError.style.display='block';
    signupFormServerError.textContent= error.message;
  })

});

signinForm.addEventListener("submit", (e) => {
  e.preventDefault();

  let result = checkSigninFormIsValid(e.target);
  
  if(!result)
  return;

  let formData= new FormData(signinForm);

  const option = {
    method:"POST",
    credentials:"include",
    body:formData
  };

  let statusCode;

  fetch("http://127.0.0.1:8080/api/login",option)
  .then(response => {
    statusCode=response.status;
    return response.json();
  })
  .then(data => {

   if(statusCode!='200')
   throw new Error(data.message);

   location.replace("http://127.0.0.1:5500/pages/home.html");
  })
  .catch(err => {
    
   signInFormServerError.style.display='block';
   signInFormServerError.textContent=err.message;
  });


});


function checkSignUpFormIsValid(element){

  const {firstName,lastName,email,password,confirmPassword,isAgree} =  element;

  let results = [];

  const alaphabeticpattern = /^[a-z A-Z]+$/;
  
  if(!alaphabeticpattern.test(firstName.value)){

      enableRedBorder(signupFirstNameError,firstName,"your firstName not valid format");

      results.push(false);
  }
  else {
    disableRedBorder(signupFirstNameError,firstName);
  }

  if(!alaphabeticpattern.test(lastName.value)){

    enableRedBorder(signupLastNameError,lastName,"your lastName not valid format");

    results.push(false);
  }
  else {
  disableRedBorder(signupLastNameError,lastName);
  }

  const passwordIsValid = checkPassword(password,signupPasswordError);

  if(!passwordIsValid){
    enableRedBorder(signupPasswordError,password,"your password length not must be greater than 8");
    results.push(false);
  }
  
  else disableRedBorder(signupPasswordError,password);


  let confirmPasswordIsValid = checkPasswordConfirmIsValid(confirmPassword,password);
  
  if(!confirmPasswordIsValid){
    enableRedBorder(signupConfirmPasswordError,confirmPassword,"your confirm passwor not be same with password");
    results.push(false);
  }

  else disableRedBorder(signupConfirmPasswordError,confirmPassword);

  const isAgreeSelected = isAgree.checked;
  const agreePrivacy =  isAgree.parentNode;

  if(!isAgreeSelected){
    enableRedBorder(signupAgreeError,agreePrivacy,"you must accept and agree of service policy");
    results.push(false);
  }
  else {
    disableRedBorder(signupAgreeError,agreePrivacy);
  }

  return results.length > 0 ? false : true;
}

function checkSigninFormIsValid(element) {
  const { email, password } = element;

  let passwordErrorBlock = document.getElementById("signin-password-error");
  return checkPassword(password,passwordErrorBlock);
}

function checkPasswordConfirmIsValid(confirmPassword,password){
  
  const passwordValue = password.value;
  const confirmPasswordValue = confirmPassword.value;

  return passwordValue==confirmPasswordValue;

}
function checkPassword(password,passwordErrorBlock){

  let passwordValue = password.value;

  if (passwordValue.length < 8) {

    enableRedBorder(passwordErrorBlock,password,
      "your password length must be greater than 8");
      return false;
  }

  disableRedBorder(passwordErrorBlock,password);
  return true;
}

function enableRedBorder(errorBlock,element,message){
  errorBlock.style.display = "block";
  errorBlock.textContent = message;
  element.style.borderColor = "crimson";
  element.style.borderWidth = "2px";
  element.style.borderStyle = "solid";

}
function disableRedBorder(errorBlock,element){
  errorBlock.textContent = "";
  errorBlock.style.display = "none";
  element.style.borderWidth = "0px";
}

function initializeSigninPasswordVisibility(target) {
  if (target.id == "signin-show-password") {
    showPasswordFunction(target, signinPasswordInput, signinHidePassword);
  } else if (target.id == "signin-hide-password") {
    hidePasswordFunction(target, signinPasswordInput, signinShowPassword);
  }
}

function initializeSignupPasswordVisibility(target) {
  if (target.id == "signup-show-password") {
    showPasswordFunction(target, signupPasswordInput, signupHidePassowrd);
  } else if (target.id == "signup-hide-password") {
    hidePasswordFunction(target, signupPasswordInput, signupShowPassowrd);
  } else if (target.id == "show-confirm-password") {
    showPasswordFunction(target, passwordConfirmInput, hideConfirmPassword);
  } else if (target.id == "hide-confirm-password") {
    hidePasswordFunction(target, passwordConfirmInput, showConfirmPassword);
  }
}

function showPasswordFunction(element, passwordInput, hidePassword) {
  element.style.display = "none";
  passwordInput.setAttribute("type", "text");
  hidePassword.style.display = "block";
}

function hidePasswordFunction(element, passwordInput, showPassword) {
  element.style.display = "none";
  passwordInput.setAttribute("type", "password");
  showPassword.style.display = "block";
}
