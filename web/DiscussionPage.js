var currentUser;
var discussions;
var discussionObj;
var discussionResponses;
var index;

function body_onload() {
    currentUser = sessionStorageGet("CurrentUser", null);

    createDiscussionBtn.onclick = createDiscussionBtn_onclick;
    backBtn.onclick = backBtn_onclick;
    retrieveDiscussions();
}

function createDiscussionBtn_onclick() {
    showModal("createDiscussion", null);
    
    createDiscussionCreateBtn.onclick = function() {
        var xmlhttp = new XMLHttpRequest();
        xmlhttp.onreadystatechange = function() {
            if (this.readyState == 4 && this.status == 200) {
                //console.log(this.responseText);
                var response = this.responseText;
                //retrieveAnnouncements();
                var modal = document.getElementById("createDiscussionModal");
                modal.style.display = "none";
                retrieveDiscussions();
            }
        };
        xmlhttp.open("GET", "CreateDiscussion.php?user=" + currentUser + "&title=" + createDiscussionTitle.value + "&discussion=" + createDiscussionInput.value, true);
        xmlhttp.send();
    }
}

function retrieveDiscussions() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            discussions = new Array();
            var responses = this.responseText.split("&");

            for (var i = 0; i < responses.length -1; i++) {
                discussionObj = new Object();
                discussionObj = JSON.parse(responses[i]);
                discussions.push(discussionObj);
            }

            displayDiscussions();
        }
    };
    xmlhttp.open("GET", "GetDiscussion.php", true);
    xmlhttp.send();
}

function displayDiscussions() {
    discussionQuestionsData.innerHTML = "";
    for(var i = discussions.length - 1; i >= 0; i--) {
        var entry = discussions[i];

        var row = document.createElement("div");
        var col1 = document.createElement("div");

        row.className = "divDiscussionsRow";
        col1.className = "divDiscussionsRowCol";

        if (entry.title.length > 20) {
            col1.innerHTML = entry.title.substring(0,20) + "...";
        } else {
            col1.innerHTML = entry.title;
        }
        
        row.style.cursor = 'pointer'; // changing the pointer when mouse is hovered over.*/
        row.ondblclick = row_onblclick; // run corresponding functions when buttons are pressed.
        row.index = i; 

        row.appendChild(col1);

        discussionQuestionsData.appendChild(row);
    }
}

function row_onblclick() {
    index = this.index;
    retrieveDiscussionThread();
}

function retrieveDiscussionThread() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            //console.log(this.responseText);
            discussionResponses = new Array();
            var responses = this.responseText.split("&");

            for (var i = 0; i < responses.length -1; i++) {
                discussionObj = new Object();
                discussionObj = JSON.parse(responses[i]);
                discussionResponses.push(discussionObj);
            }

            displayDiscussionThread();
        }
    };
    xmlhttp.open("GET", "GetDiscussionPage.php?id=" + discussions[index].id, true);
    xmlhttp.send();
}

function displayDiscussionThread() {
    discussionThreadTitle.innerHTML = discussions[index].title;
    discussionThreadAuthor.innerHTML = "Discussion&nbspby&nbsp" + discussions[index].author;
    discussionThreadDesc.innerHTML = discussions[index].description;
    discussionResponsesData.innerHTML = "Responses:";

    for(var i = 0; i < discussionResponses.length; i++) {
        var entry = discussionResponses[i];

        var author = document.createElement("div");
        author.className = "divDiscussionResponsesAuthor";
        author.innerHTML = "Response by " + discussionResponses[i].author;
        discussionResponsesData.appendChild(author);

        var description = document.createElement("div");
        description.className = "divDiscussionResponsesDesc";
        description.innerHTML = discussionResponses[i].description;
        discussionResponsesData.appendChild(description);

        var newline = document.createElement("div");
        newline.className = "divNewLine";
        discussionResponsesData.appendChild(newline);
    }

    var textArea = document.createElement("textarea");
    textArea.id = "discussionResponseTextarea";

    discussionResponsesData.appendChild(textArea);

    var respondBtn = document.createElement("input");
    respondBtn.type = "button";
    respondBtn.value = "Respond";
    respondBtn.className = "discussionRespondBtn";

    discussionResponsesData.appendChild(respondBtn);

    respondBtn.onclick = respondBtn_onclick;
}

function respondBtn_onclick() {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (this.readyState == 4 && this.status == 200) {
            console.log(this.responseText);
            var response = this.responseText;
            retrieveDiscussionThread();
        }
    };
    xmlhttp.open("GET", "CreateResponse.php?user=" + currentUser + "&response=" + discussionResponseTextarea.value + "&id=" + discussions[index].id, true);
    xmlhttp.send();
}

function backBtn_onclick() {
    history.back();
}