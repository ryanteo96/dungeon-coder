var currentUser;
var userInformation;
var taskInformation;

function body_onload() {
    if (!checkLoggedIn() || checkLoggedIn() === "null") {
        alert("Sign in required.");
        location.href = "SignIn.html";
    }

    currentUser = sessionStorageGet("CurrentUser", null);
    welcomeHdr.innerHTML = "WELCOME " + currentUser + "!";

    manageAccountBtn.onclick = manageAccountBtn_onclick;
    task1.onclick = function() {
        task_onclick("Task1");
    }
    saveBtn.onclick = saveBtn_onclick;
}

function task_onclick(task) {
    showModal(task);
    if (task === "Task1") {
        taskHdr.innerHTML = "TASK 1";
        getUserTaskInformation(task);
    }
    
}

function getUserTaskInformation(task) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            var taskInformation = new Object();
            var response = this.responseText;
            var array = response.split("&");
            taskInformation = JSON.parse(array);
            completionVal.innerHTML = taskInformation.completion + "%";
            attemptsVal.innerHTML = taskInformation.attempts;
            pointsVal.innerHTML = taskInformation.points;
            codeVal.innerHTML = taskInformation.code;
            commentsVal.innerHTML = taskInformation.comments;

            if (taskInformation.deadline === null) 
                deadlineVal.innerHTML = '-';
            else 
                deadlineVal.innerHTML = taskInformation.deadline;
        }
    };
    xmlhttp.open("GET", "GetUserTaskInformation.php?user=" + currentUser + "&task=" + task, true);
    xmlhttp.send();
}