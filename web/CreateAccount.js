var username;
var password;
var confirmPassword;

function body_onload() {
    registerBtn.onclick = registerBtn_onclick;
}

function registerBtn_onclick() {
    if (usernameInput.value.length <= 0) {
        alert("Username required.");
        usernameInput.focus();
    } else if (passwordInput.value.length <= 0) {
        alert("Password required.");
        passwordInput.focus();
    } else if (confirmPasswordInput.value.length <= 0) {
        alert("Confirm Password required.");
        confirmPasswordInput.focus();
    } else {
        username = usernameInput.value;
        password = passwordInput.value;
        confirmPassword = confirmPasswordInput.value;

        if (password !== confirmPassword) {
            alert("Confirm Password incorrect.");
        } else {
            attemptCreateAccount();
        }
    }
}

function attemptCreateAccount() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            var response = this.responseText;
            checkResponse(response);
        }
    };
    xmlhttp.open("GET", "CreateAccount.php?username=" + username + "&password=" + password, true);
    xmlhttp.send();
}

function checkResponse(response) {
    if (response === "True") {
        alert("Account successfully created.");
        location.href = "TeacherDashboard.html";
    }

    if (response === "False") {
        alert("Username has been taken.");
    }
}