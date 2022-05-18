function checkPasswords() {
    let password = document.getElementById("password")
    let passwordConfirm = document.getElementById("passwordConfirm")
    if (password.value === passwordConfirm.value) {
        document.getElementById("button").textContent = "Регистрация";
        document.getElementById("button").style.color = 'white';
        document.querySelector('button').disabled = false;
    } else {
        document.getElementById("button").textContent = "Регистрация";
        document.getElementById("button").style.background = 'gray';
        document.querySelector('button').disabled = true;
    }
}