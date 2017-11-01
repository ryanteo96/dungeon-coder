<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);

$date = $_GET["date"];
$announcement = $_GET["announcement"];

$sql = "INSERT INTO Announcements (" ."Date" .", Announcement) VALUES ('" . $date . "','" . $announcement . "')";
$conn->query($sql);

$conn->close();
?>