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
$pointValue = $_GET["pointValue"];

$sql = "UPDATE " . $task . " SET PointValue='" . $pointValue . "'" . " WHERE Student='" . $user ."'" ;
echo $sql;
$conn->query($sql);

$conn->close();

?>