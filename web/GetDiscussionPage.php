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

$id = $_GET["id"];
$return;

$sql = "SELECT Author, " . "Description" . " FROM Discussions WHERE Type='response' AND ResponseTo=" . $id . " ORDER BY OrderNo ASC" ;
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $return -> author= $row["Author"];
        $return -> description = $row["Description"];
        
        $returnJSON = json_encode($return);
        echo $returnJSON . "&";
    }
}

$conn->close();
?>