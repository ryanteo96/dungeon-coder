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
$return;

$sql = "SELECT Student, Completion, Deadline, Attempts, Code, PointValue FROM " . $task;
//echo $sql;
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $return -> student = $row["Student"];
        $return -> completion = $row["Completion"];
        $return -> deadline = $row["Deadline"];
        $return -> attempts = $row["Attempts"];
        $return -> code = $row["Code"];
        $return -> pointValue = $row["PointValue"];
        
        $returnJSON = json_encode($return);
        echo $returnJSON . "&";
    }
}

$conn->close();
?>