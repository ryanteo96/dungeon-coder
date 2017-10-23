var username;
var password;
var confirmPassword;
var userType;

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

        var userTypeSelect = document.getElementById("userTypeSelect");
        userType = userTypeSelect.options[userTypeSelect.selectedIndex].value;

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
    xmlhttp.open("GET", "CreateAccount.php?username=" + username + "&password=" + password + "&userType=" + userType, true);
    xmlhttp.send();
}

function checkResponse(response) {
    if (response === "True") {
        alert("Account successfully created.");
        if (userType === "teacher") {
            location.href = "TeacherDashboard.html";
            sessionStorageSet("CurrentUser", username);
        } else if (userType === "student") {
            location.href = "StudentDashboard.html";
            sessionStorageSet("CurrentUser", username);
        }
    }

    if (response === "False") {
        alert("Username has been taken.");
    }
}