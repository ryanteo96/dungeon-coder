<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

$user = $_GET["user"];
$discussion = $_GET["discussion"];
$title = $_GET["title"];
$index;

$sql = "SELECT ID FROM Discussions ORDER BY ID DESC";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $index = $row["ID"] + 1;
} else {
    $index = 1;
}

$sql = "INSERT INTO Discussions (ID, Author, Title," . "Description" . ", Type) VALUES ('" . $index . "','" . $user . "','" . $title . "','". $discussion . "','post')";
$conn->query($sql);

$conn->close();
?>