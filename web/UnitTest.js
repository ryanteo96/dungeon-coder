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

            callback(testSettingComment);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=priority", true);
    xmlhttp.send();
}

function testSettingLock(callback) {
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

            callback(testSettingPointValue);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=lock", true);
    xmlhttp.send();
}

function testSettingComment(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 10");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSettingCommentRes.innerHTML = "PASS";
            } else {
                testSettingCommentRes.innerHTML = "FAIL";
            }

            callback(testSettingDeadline);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=comment", true);
    xmlhttp.send();
}

function testSettingPointValue(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 11");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSettingPointValueRes.innerHTML = "PASS";
            } else {
                testSettingPointValueRes.innerHTML = "FAIL";
            }

            callback(testSettingEmail);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=points", true);
    xmlhttp.send();
}

function testSettingDeadline(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 12");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSettingDeadlineRes.innerHTML = "PASS";
            } else {
                testSettingDeadlineRes.innerHTML = "FAIL";
            }

            callback(testChangingPassword);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=deadline", true);
    xmlhttp.send();
}

function testSettingEmail(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 13");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testSettingEmailRes.innerHTML = "PASS";
            } else {
                testSettingEmailRes.innerHTML = "FAIL";
            }

            callback(testAddDiscussion);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=email", true);
    xmlhttp.send();
}

function testChangingPassword(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 14");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testChangingPasswordRes.innerHTML = "PASS";
            } else {
                testChangingPasswordRes.innerHTML = "FAIL";
            }

            callback(testAddResponse);
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=set_values&mode=password", true);
    xmlhttp.send();
}

function testAddDiscussion(callback) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 15");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testAddDiscussionRes.innerHTML = "PASS";
            } else {
                testAddDiscussionRes.innerHTML = "FAIL";
            }

            callback();
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=add_discussion", true);
    xmlhttp.send();
}

function testAddResponse() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
            console.log("TEST 16");
            console.log(this.responseText);
            response = this.responseText;

            if (response === "PASS") {
                testAddResponseRes.innerHTML = "PASS";
            } else {
                testAddResponseRes.innerHTML = "FAIL";
            }
        }
    };

    xmlhttp.open("GET", "UnitTest.php?test=add_response", true);
    xmlhttp.send();
}
