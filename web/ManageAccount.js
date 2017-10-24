function manageAccountBtn_onclick() {
    showModal("manageAccount", null);
    manageAccountRestoreState();
}

function manageAccountRestoreState() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var userInformation = new Object();
            var response = this.responseText;
            var array = response.split("&");
            userInformation = JSON.parse(array);
            usernameInput.value = userInformation.username;
            emailInput.value = userInformation.email;
        }
    };
    xmlhttp.open("GET", "GetUserInformation.php?user=" + currentUser, true);
    xmlhttp.send();
}

function saveBtn_onclick() {
    var response;

    if (usernameInput.value.length >= 0) 
        var username = usernameInput.value;

    if (passwordInput.value.length >= 0) 
        var password = passwordInput.value;

    if (confirmPasswordInput.value.length >= 0) 
        var confirmPassword = confirmPasswordInput.value;

    if (emailInput.value.length >= 0) 
        var email = emailInput.value;

    if (password !== confirmPassword) {
        alert("Confirm Password incorrect.");
    } else {

        var query = "";

        if (username != null) {
            query += "&user=" + username;
        } 

        if (password != null) {
            query += "&pass=" + password;
        }

        if (email != null) {
            query += "&email=" + email;
        }
        
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                console.log(this.responseText);
                response = this.responseText;
                if (response === "True") {
                    if (username != null) 
                        sessionStorageSet("CurrentUser", username);
                    
                    modal = document.getElementById("manageAccountModal");
                    modal.style.display = "none";
                    location.reload();
                } else {
                    alert("This username has been taken.");
                }
            }
        };
        xmlhttp.open("GET", "UpdateUserInformation.php?currentUser=" + currentUser + query, true);
        xmlhttp.send();
    }
}