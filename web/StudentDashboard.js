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
    getUserInformation();
    retrieveAnnouncements();

    manageAccountBtn.onclick = manageAccountBtn_onclick;
    task1.onclick = function() {
        task_onclick("Task1");
    }
    saveBtn.onclick = saveBtn_onclick;
    signOutBtn.onclick = signOutBtn_onclick;
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
            exampleCodeVal.innerHTML = taskInformation.savedCode;
            createCodeLink();
            createExampleCodeLink();

            if (taskInformation.deadline === null) 
                deadlineVal.innerHTML = '-';
            else 
                deadlineVal.innerHTML = taskInformation.deadline;
        }
    };
    xmlhttp.open("GET", "GetUserTaskInformation.php?user=" + currentUser + "&task=" + task, true);
    xmlhttp.send();
}

function signOutBtn_onclick() {
    signOut();
}

function getUserInformation() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            userInformation = new Object();
            //console.log(this.responseText);
            var response = this.responseText;
            var array = response.split("&");
            userInformation = JSON.parse(array);
            studentGrade.innerHTML = userInformation.grade;
        }
    };
    xmlhttp.open("GET", "GetUserInformation.php?user=" + currentUser, true);
    xmlhttp.send();
}

function retrieveAnnouncements() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            announcements = new Array();
            var responses = this.responseText.split("&");

            for (var i = 0; i < responses.length - 1; i++) {
                announcementObj = new Object();
                announcementObj = JSON.parse(responses[i]);
                announcements.push(announcementObj);
                displayAnnouncements();
            }
        }
    };
    xmlhttp.open("GET", "GetAnnouncements.php", true);
    xmlhttp.send();
}

function displayAnnouncements() {
    announcementListData.innerHTML = "";
    for (var i = 0; i < announcements.length; i++) {
        var entry = announcements[i];

        var row = document.createElement("div");
        var col1 = document.createElement("div");
        var col2 = document.createElement("div");

        row.className = "divAnnouncementsRow";
        col1.className = "divAnnouncementsRowCol1";
        col2.className = "divAnnouncementsRowCol2";

        col1.innerHTML = entry.date;
        col2.innerHTML = entry.announcement;

        /*row.ondblclick = row_onblclick // run corresponding functions when buttons are pressed.
        row.index = i; 
        row.style.cursor = 'pointer'; // changing the pointer when mouse is hovered over.*/

        row.appendChild(col1);
        row.appendChild(col2);

        announcementListData.appendChild(row);
    }
}

function createCodeLink(file) {
    codeVal.innerHTML = "";

    var text = document.createElement("button");
    text.id = "codeDownload";
    text.innerHTML = "View Code<br><br>Right click -> Save As to download file." ;    
    codeVal.appendChild(text);  

    codeDownload.onclick = function() {
        window.open('/files/test.java', '_blank');
    }
}

function createExampleCodeLink(file) {
    exampleCodeVal.innerHTML = "";

    var text = document.createElement("button");
    text.id = "exampleCodeDownload";
    text.innerHTML = "View Code<br><br>Right click -> Save As to download file.";
    exampleCodeVal.appendChild(text);

    exampleCodeDownload.onclick = function () {
        window.open('/files/test2.java', '_blank');
    }
}