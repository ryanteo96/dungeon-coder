function localStorageGet(token, defaultValue) {
    var value = localStorage.getItem(token);

    if (value === null) {
        return defaultValue;
    }
    return value;
}

function localStorageSet(token, value) {
    localStorage.setItem(token, value);
}

function sessionStorageGet(token, defaultValue) {
    var value = sessionStorage.getItem(token);

    if (value === null) {
        return defaultValue;
    }
    return value;
}

function sessionStorageSet(token, value) {
    sessionStorage.setItem(token, value);
}


function showModal(type, previousModal) {
    var modal;  
    var span;

    if (type === "Task1") {
        modal = document.getElementById("taskModal");
        span = document.getElementById("taskClose");
    } else if (type === "manageAccount") {
        modal = document.getElementById("manageAccountModal");
        span = document.getElementById("manageAccountClose");
    }

    /*
    if (type === "signInModal") {
        span = document.getElementById("signInClose");
    } else if (type === "editModal") {
        span = document.getElementById("editClose");
    } else if (type === "alertModal") {
        span = document.getElementById("alertClose");
    } else if (type === "confirmationModal") {
        span = document.getElementById("confirmationClose");
    }*/

    modal.style.display = "block";

    span.onclick = function() {
        modal.style.display = "none";
        /*
        if (type === "editModal") {
            userConfirmation("Are you sure you want to discard your changes to this entry?", modal, "cancel");
        }else if (checkLogIn()) {
            modal.style.display = "none";
        } else
            alertUser("Sign In Required.", modal);
            */

    }

    modal.onclick = function(event) {
        if (event.target == modal) {
            modal.style.display = "none";
        }

        /*
        if (event.target == modal) {
            if (type === "editModal") {
                userConfirmation("Are you sure you want to discard your changes to this entry?", modal, "cancel");
            } else if (checkLogIn()) {
                modal.style.display = "none";
            } else 
                alertUser("Sign In Required.", modal);
        }
        */
    }
}

// validating the date fields.
function validDate(dateString) {
    var date = new Date(dateString);
    
    if (date === "Invalid Date") {
        return false;
    }
    return true;
}


function checkLoggedIn() {
    return sessionStorageGet("CurrentUser", null);
}

function signOut() {
    location.href = "SignIn.html";
}
