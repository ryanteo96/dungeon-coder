var entries;

function body_onload() {
    retrieveStudentList();
}

function displayStudents() {
    studentListDivData.innerHTML = "";
    console.log(entries);
    console.log(entries.length);
    // traversing the array of entries and creating HTML components and CSS styles.
    for(var i = 0; i < entries.length; i++) {
        console.log("yes");
        console.log(entries);
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
        console.log(entry);
        entries.push(entry);
    }
    console.log(entries);
    displayStudents();
}