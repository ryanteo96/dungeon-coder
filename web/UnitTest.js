/*var testUsername = "test";
var testPassword = "testPass";
var testStudent = "student";
var testTeacher = "teacher";
var response;*/


function body_onload() {
    testBtn.onclick = testBtn_onclick;
}

function testBtn_onclick() {
    testAccountCreationStudent(testAccountCreationTeacher);
}

function testAccountCreationStudent(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 1");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testAccountCreationStudentRes.innerHTML = "PASS";
            } else {
                testAccountCreationStudentRes.innerHTML = "FAIL";
            }

            callback(testSignInCorrectLogin);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=account_create_student", true);
    xmlhttp.send();   
}

function testAccountCreationTeacher(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 2");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testAccountCreationTeacherRes.innerHTML = "PASS";
            } else {
                testAccountCreationTeacherRes.innerHTML = "FAIL";
            }

            callback(testSignInWrongUsername);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=account_create_teacher", true);
    xmlhttp.send();   
}

function testSignInCorrectLogin(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 3");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSignInCorrectLoginRes.innerHTML = "PASS";
            } else {
                testSignInCorrectLoginRes.innerHTML = "FAIL";
            }

            callback(testSignInWrongPassword);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=sign_in&mode=correct_login", true);
    xmlhttp.send();
}

function testSignInWrongUsername(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 4");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSignInWrongUsernameRes.innerHTML = "PASS";
            } else {
                testSignInWrongUsernameRes.innerHTML = "FAIL";
            }

            callback(testCreateAnnouncement);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=sign_in&mode=wrong_username", true);
    xmlhttp.send();
}

function testSignInWrongPassword(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 5");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSignInWrongPasswordRes.innerHTML = "PASS";
            } else {
                testSignInWrongPasswordRes.innerHTML = "FAIL";
            }

            callback(testSettingGrade);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=sign_in&mode=wrong_password", true);
    xmlhttp.send();
}

function testCreateAnnouncement(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 6");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testCreateAnnouncementRes.innerHTML = "PASS";
            } else {
                testCreateAnnouncementRes.innerHTML = "FAIL";
            }

            callback(testSettingPriority);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=create_announcement", true);
    xmlhttp.send();
}

function testSettingGrade(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 7");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSettingGradeRes.innerHTML = "PASS";
            } else {
                testSettingGradeRes.innerHTML = "FAIL";
            }

            callback(testSettingLock);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=grade", true);
    xmlhttp.send();
}

function testSettingPriority(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 8");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSettingPriorityRes.innerHTML = "PASS";
            } else {
                testSettingPriorityRes.innerHTML = "FAIL";
            }

            callback();
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=priority", true);
    xmlhttp.send();
}

function testSettingLock() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 9");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSettingLockRes.innerHTML = "PASS";
            } else {
                testSettingLockRes.innerHTML = "FAIL";
            }
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=lock", true);
    xmlhttp.send();
}


