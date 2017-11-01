var currentUser;
var entries;
var currentTask;
var currentDeadline;
var taskObj;
var allStudentTask;
var temp;
var announcements;
var announcementObj;

function body_onload() {
    if (!checkLoggedIn() || checkLoggedIn() === "null") {
        alert("Sign in required.");
        location.href = "SignIn.html";
    }

    currentUser = sessionStorageGet("CurrentUser", null);

    retrieveStudentList();
    retrieveAnnouncements();
    task1.onclick = task1_onclick;
    changeDeadlineBtn.onclick = changeDeadlineBtn_onclick;
    manageAccountBtn.onclick = manageAccountBtn_onclick;
    saveBtn.onclick = saveBtn_onclick;
    signOutBtn.onclick = signOutBtn_onclick;
    createAnnouncementBtn.onclick = createAnnouncementBtn_onclick;
}

function displayStudents() {
    studentListDivData.innerHTML = "";
    //console.log(entries);
    //console.log(entries.length);
    // traversing the array of entries and creating HTML components and CSS styles.
    for(var i = 0; i < entries.length; i++) {
        //console.log("yes");
        //console.log(entries);
        var entry = entries[i];
    
        var row = document.createElement("div");
        var col1 = document.createElement("div");
        var col2 = document.createElement("div");
        var col3 = document.createElement("div");
        var col4 = document.createElement("div");
        var col5 = document.createElement("div");

        row.className = "divEntriesRow";
        col1.className = "divEntriesRowCol1";
        col2.className = "divEntriesRowCol2";
        col3.className = "divEntriesRowCol3";
        col4.className = "divEntriesRowCol4";
        col5.className = "divEntriesRowCol5";

        col1.innerHTML = entry.studentName;
        col2.innerHTML = entry.studentID;
        col3.innerHTML = entry.studentEmail;
        col4.innerHTML = entry.studentScore;
        col5.innerHTML = entry.studentTag;
        
        /*row.ondblclick = row_onblclick // run corresponding functions when buttons are pressed.
        row.index = i; 
        row.style.cursor = 'pointer'; // changing the pointer when mouse is hovered over.*/

        row.appendChild(col1);
        row.appendChild(col2);
        row.appendChild(col3);
        row.appendChild(col4);
        row.appendChild(col5);

        studentListDivData.appendChild(row);
    }
}

function retrieveStudentList() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            var response = this.responseText;
            var array = response.split("&");
            parseEntries(array);
        }
    };
    xmlhttp.open("GET", "StudentList.php", true);
    xmlhttp.send();
}

function parseEntries(array) {
    entries = new Array();
    for (var i = 0; i < array.length - 1; i++) {
        var entry = new Object();
        entry.studentName = array[i];
        entry.studentID = "NOTHING";
        entry.studentEmail = "NOTHING";
        entry.studentScore = "NOTHING";
        entry.studentTag = "NOTHING";
        //console.log(entry);
        entries.push(entry);
    }
    //console.log(entries);
    displayStudents();
}

function task1_onclick() {
    showModal("Task1", null);
    currentTask = "Task1";
    restoreState(currentTask);
}

function restoreState(task) {
    if (task === "Task1") {
        taskHdr.innerHTML = "TASK 1";
        attemptRetrieveTask(task);
        updateDeadline();
    }
}

function changeDeadlineBtn_onclick() {
    var deadline = changeDeadlineInput.value;

    if (currentTask === "Task1") {
        if (validDate(deadline)) {
            attemptChangeDeadline(currentTask, deadline);
        } else {
            alert("Valid date required.");
        }
    }
}

function attemptChangeDeadline(task, deadline) {
    //console.log(task);
    //console.log(deadline);  
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            var response = this.responseText;
            attemptRetrieveTask(task);
        }
    };
    xmlhttp.open("GET", "UpdateDeadline.php?task=" + task + "&deadline=" + deadline, true);
    xmlhttp.send();
}

function attemptRetrieveTask(task) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            allStudentTask = new Array();
            var responses = this.responseText.split("&");

            for (var i = 0; i < responses.length -1; i++) {
                //console.log(responses[i]);
                taskObj = new Object();
                taskObj = JSON.parse(responses[i]);
                //console.log(taskObj);
                //console.log(taskObj.deadline);
                currentDeadline = taskObj.deadline;
                updateDeadline();
                allStudentTask.push(taskObj);
            }

            displayTaskStudents();
            retrieveStatistics();
        }
    };
    xmlhttp.open("GET", "Tasks.php?task=" + task , true);
    xmlhttp.send();
    
}

function displayTaskStudents() {
    taskListDivData.innerHTML = "";
    // traversing the array of entries and creating HTML components and CSS styles.
    for(var i = 0; i < allStudentTask.length; i++) {
        //console.log("yes");
        //console.log(allStudentTask);
        var entry = allStudentTask[i];
    
        var row = document.createElement("div");
        var col1 = document.createElement("div");
        var col2 = document.createElement("div");
        var col3 = document.createElement("div");
        var col4 = document.createElement("div");
        var col5 = document.createElement("div");
        var col6 = document.createElement("div");

        row.className = "divTaskEntriesRow";
        col1.className = "divTaskEntriesRowCol1";
        col2.className = "divTaskEntriesRowCol2";
        col3.className = "divTaskEntriesRowCol3";
        col4.className = "divTaskEntriesRowCol4";
        col5.className = "divTaskEntriesRowCol5";
        col6.className = "divTaskEntriesRowCol6";

        col1.innerHTML = entry.student;
        col2.innerHTML = entry.deadline;
        col3.innerHTML = entry.completion;
        col4.innerHTML = entry.attempts;
        col5.innerHTML = entry.code;
        col6.innerHTML = entry.pointValue;
        
        row.ondblclick = taskRow_onblclick // run corresponding functions when buttons are pressed.
        row.index = i; 
        row.style.cursor = 'pointer'; // changing the pointer when mouse is hovered over.*/

        row.appendChild(col1);
        row.appendChild(col2);
        row.appendChild(col3);
        row.appendChild(col4);
        row.appendChild(col5);
        row.appendChild(col6);

        taskListDivData.appendChild(row);
    }
}

function updateDeadline() {
    deadlineVal.innerHTML = currentDeadline;
}

function signOutBtn_onclick() {
    signOut();
}

function createAnnouncementBtn_onclick() {
    showModal("createAnnouncement", null);

    createAnnouncementSubmitBtn.onclick = function() {
        if (createAnnouncementInput.value.length > 0)
            createAnnouncement();

        var modal = document.getElementById("createAnnouncementModal");
        modal.style.display = "none";
    }
}

function createAnnouncement() {
    var date = new Date();
    var dateString = date.toISOString()

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            var response = this.responseText;
            retrieveAnnouncements();
        }
    };
    xmlhttp.open("GET", "CreateAnnouncement.php?date=" + dateString.substring(0, 10) + "&announcement=" + createAnnouncementInput.value, true);
    xmlhttp.send();
}

function retrieveAnnouncements() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            announcements = new Array();
            var responses = this.responseText.split("&");

            for (var i = 0; i < responses.length -1; i++) {
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
    for(var i = 0; i < announcements.length; i++) {
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

function retrieveStatistics() {
    var classCompletion = 0;
    var averageAttempts = 0;
    var averagePoints = 0;

    for (var i = 0; i < allStudentTask.length; i++) {
        classCompletion += parseFloat(allStudentTask[i].completion);
        averageAttempts += parseFloat(allStudentTask[i].attempts);
        averagePoints += parseFloat(allStudentTask[i].pointValue);
    }   

    if (classCompletion !== 0) {
        classCompletion = classCompletion / allStudentTask.length;
    }

    if (averageAttempts !== 0) {
        averageAttempts = averageAttempts / allStudentTask.length;
    }

    if (averagePoints !== 0) {
        averagePoints = averagePoints / allStudentTask.length;
    }

    completionVal.innerHTML = classCompletion.toFixed(2) + "%";
    attemptsVal.innerHTML = averageAttempts.toFixed(2);
    pointsVal.innerHTML = averagePoints.toFixed(2);
}


function taskRow_onblclick() {
    temp = new Object();
    temp = allStudentTask[this.index];
    showModal("editStudent", "Task1");

    editStudentName.innerHTML = temp.student;
    editStudentDeadlineInput.value = temp.deadline;
    editStudentPointInput.value = temp.pointValue;
    editStudentCommentInput.value = temp.comments;

    editStudentSaveBtn.onclick = function() {

        var obj = new Object();
        obj.student = temp.student;
        obj.deadline = editStudentDeadlineInput.value;
        obj.pointValue = editStudentPointInput.value;
        obj.comment = editStudentCommentInput.value;

        attemptEditUser(currentTask, obj);

        var modal = document.getElementById("editStudentModal");
        modal.style.display = "none";
    }
}


function attemptEditUser(task, obj) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);  
            attemptRetrieveTask(task);
            retrieveStatistics();
        }
    };

    var query = "&task=" + currentTask;

    if (obj.pointValue !== null) {
        if (obj.pointValue < 0) {
            alert("Valid point required.");
        }

        query += "&point=" + obj.pointValue;
    }

    if (obj.deadline !== null) {
        query += "&deadline=" + obj.deadline;
    }

    if (obj.comment !== null) {
        query += "&comment=" + obj.comment;
    }

    xmlhttp.open("GET", "EditStudent.php?user=" + obj.student + query, true);
    xmlhttp.send();
}