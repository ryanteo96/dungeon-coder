<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

$user = $_GET["user"];
$response = $_GET["response"];
$id = $_GET["id"];
$index;
$key;

$sql = "SELECT ID FROM Discussions ORDER BY ID DESC";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $key = $row["ID"] + 1;
} else {
    $key = 1;
}

$sql = "SELECT OrderNo FROM Discussions WHERE ResponseTo=" . $id . " ORDER BY OrderNo DESC";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    $row = $result->fetch_assoc();
    $index = $row["OrderNo"] + 1;
} else {
    $index = 1;
}

$sql = "INSERT INTO Discussions (ID, Author, " . "Description" . ", Type, OrderNo, ResponseTo) VALUES (" . $key . ",'". $user . "','" . $response . "','response'," . $index . ",". $id . ")";
echo $sql;
$conn->query($sql);

$conn->close();
?>