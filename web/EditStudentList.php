<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$user = $_GET["user"];
$lock = $_GET["lock"];
$priority = $_GET["priority"];
$grade = $_GET["grade"];

$query = "";

if ($lock !== null) {
    $query = $query . "LockStatus=" . $lock;
}

if ($priority !== null) {
    $query = $query . "Priority=" . $priority;
}

if ($grade !== null) {
    $query = $query . "Grade='" . $grade . "'";
}


$sql = "UPDATE Users SET " . $query . " WHERE Username='" . $user ."'" ;
echo $sql;
$conn->query($sql);

$conn->close();

?>