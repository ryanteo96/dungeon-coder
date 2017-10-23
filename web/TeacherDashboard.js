var entries;
var currentTask;
var currentDeadline;
var taskObj;
var allStudentTask;
var temp;

function body_onload() {
    retrieveStudentList();
    task1.onclick = task1_onclick;
    changeDeadlineBtn.onclick = changeDeadlineBtn_onclick;
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
            console.log(this.responseText);
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
    console.log(task);
    console.log(deadline);  
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
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
                console.log(responses[i]);
                taskObj = new Object();
                taskObj = JSON.parse(responses[i]);
                console.log(taskObj);
                console.log(taskObj.deadline);
                currentDeadline = taskObj.deadline;
                updateDeadline();
                allStudentTask.push(taskObj);
                displayTaskStudents();
            }
            
        }
    };
    xmlhttp.open("GET", "Tasks.php?task=" + task , true);
    xmlhttp.send();
    
}

function displayTaskStudents() {
    taskListDivData.innerHTML = "";
    // traversing the array of entries and creating HTML components and CSS styles.
    for(var i = 0; i < allStudentTask.length; i++) {
        console.log("yes");
        console.log(allStudentTask);
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
        col2.innerHTML = entry.completion;
        col3.innerHTML = entry.deadline;
        col4.innerHTML = entry.attempt;
        col5.innerHTML = entry.code;
        col6.innerHTML = entry.pointValue;
        
        row.ondblclick = row_onblclick // run corresponding functions when buttons are pressed.
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
    taskDeadline.innerHTML = currentDeadline;
}

function row_onblclick() {
    temp = new Object();
    temp = allStudentTask[this.index];
    currentStudent.innerHTML = temp.student;
    changePointValueBtn.onclick = changePointValueBtn_onclick;
}

function changePointValueBtn_onclick() {
    var pointValue = changePointValueInput.value;
    if (pointValue.length <= 0) {
        alert("Point Value required.");
    } else {
        attemptChangePointValue(currentTask, temp.student, pointValue);
    }
}

function attemptChangePointValue(task, user, pointValue) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);  
            attemptRetrieveTask(task);
        }
    };
    xmlhttp.open("GET", "PointValue.php?task=" + task + "&user=" + user + "&pointValue=" + pointValue, true);
    xmlhttp.send();
    
}