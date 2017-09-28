function body_onload() {
    signInBtn.onclick = signInBtn_onclick;
    createAccountBtn.onclick = createAccountBtn_onclick;
}

function signInBtn_onclick() {
    alert("In progress.");
}

function createAccountBtn_onclick() {
    window.location.href = "CreateAccount.html";
}