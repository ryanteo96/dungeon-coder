<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

$user = $_GET["user"];
$pass = $_GET["password"];
$file = $_GET["filename"];

$param = "java CodeTransfer ";
//echo $param . $file . " " . $user . " " . $pass;

exec($param . $file . " " . $user . " " . $pass , $return);
echo $return[0];

$conn->close();

?>