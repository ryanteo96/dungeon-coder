<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

$param = "java Hash ";
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$task = $_GET["task"];
$user = $_GET["user"];
$point = $_GET["point"];
$deadline = $_GET["deadline"];
$comment = $_GET["comment"];

$query = "";

echo $point;
echo $deadline;
echo $comment;

if ($point !== "") {
    if ($query === "")
        $query = $query . "PointValue='" . $point . "'";
    else
        $query = $query . ",PointValue='" . $point . "'";
}

if ($deadline !== "") {
    if ($query === "")
        $query = $query . "Deadline='" . $deadline . "'";
    else
        $query = $query . ",Deadline='" . $deadline . "'";
}

if ($comment !== "") {
    if ($query === "")
        $query = $query . "Comments='" . $comment . "'";
    else
        $query = $query . ",Comments='" . $comment . "'";
}


$sql = "UPDATE " . $task . " SET " . $query . " WHERE Student='" . $user ."'" ;
echo $sql;
$conn->query($sql);

$conn->close();

?>