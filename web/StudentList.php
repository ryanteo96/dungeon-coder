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

$return;

$sql = "SELECT Username, Email, Class, Grade, LockStatus, Priority FROM Users WHERE Type='student'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $return -> username = $row["Username"];
        $return -> email = $row["Email"];
        $return -> className = $row["Class"];
        $return -> grade = $row["Grade"];
        $return -> lock = $row["LockStatus"];
        $return -> priority = $row["Priority"];
        
        $returnJSON = json_encode($return);
        echo $returnJSON . "&";
    }
}

$conn->close();

?>