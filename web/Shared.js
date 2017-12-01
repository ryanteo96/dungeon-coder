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
    } else if (type === "createAnnouncement") {
        modal = document.getElementById("createAnnouncementModal");
        span = document.getElementById("createAnnouncementClose");
    } else if (type === "editStudentTask") {
        modal = document.getElementById("editStudentTaskModal");
        span = document.getElementById("editStudentTaskClose");
    } else if (type === "editStudentList") {
        modal = document.getElementById("editStudentListModal");
        span = document.getElementById("editStudentListClose");
    } else if (type === "createDiscussion") {
        modal = document.getElementById("createDiscussionModal");
        span = document.getElementById("createDiscussionClose");
    }

    modal.style.display = "block";
    //console.log("SET OPEN: " + modal.id);
    //console.log("SET OPEN: " + span.id);

    span.onclick = function() {
        //console.log("PRESS CLOSE: " + span.id);
        modal.style.display = "none";

        if (previousModal === "Task1") {
            modal = document.getElementById("taskModal");
            span = document.getElementById("taskClose");
        } else if (previousModal === "manageAccount") {
            modal = document.getElementById("manageAccountModal");
            span = document.getElementById("manageAccountClose");
        } else if (previousModal === "createAnnouncement") {
            modal = document.getElementById("createAnnouncementModal");
            span = document.getElementById("createAnnouncementClose");
        } else if (previousModal === "editStudentTask") {
            modal = document.getElementById("editStudentTaskModal");
            span = document.getElementById("editStudentTaskClose");
        } else if (previousModal === "editStudentList") {
            modal = document.getElementById("editStudentListModal");
            span = document.getElementById("editStudentListClose");
        } else if (type === "createDiscussion") {
            modal = document.getElementById("createDiscussionModal");
            span = document.getElementById("createDiscussionClose");
        }
    }

    window.onclick = function(event) {
        if (event.target == modal) {
            //console.log("PRESS CLOSE: " + modal.id);
            modal.style.display = "none";

            if (previousModal === "Task1") {
                modal = document.getElementById("taskModal");
                span = document.getElementById("taskClose");
            } else if (previousModal === "manageAccount") {
                modal = document.getElementById("manageAccountModal");
                span = document.getElementById("manageAccountClose");
            } else if (previousModal === "createAnnouncement") {
                modal = document.getElementById("createAnnouncementModal");
                span = document.getElementById("createAnnouncementClose");
            } else if (previousModal === "editStudentTask") {
                modal = document.getElementById("editStudentTaskModal");
                span = document.getElementById("editStudentTaskClose");
            } else if (previousModal === "editStudentList") {
                modal = document.getElementById("editStudentListModal");
                span = document.getElementById("editStudentListClose");
            } else if (type === "createDiscussion") {
                modal = document.getElementById("createDiscussionModal");
                span = document.getElementById("createDiscussionClose");
            }
        } 
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
