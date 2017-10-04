function body_onload() {
    signInBtn.onclick = signInBtn_onclick;
    createAccountBtn.onclick = createAccountBtn_onclick;
}

function signInBtn_onclick() {
    location.href = "TeacherDashboard.html";
}

function createAccountBtn_onclick() {
    location.href = "CreateAccount.html";
}