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

$user = $_GET["user"];
$return;
$returnJSON;

$sql = "SELECT Username, Email FROM Users WHERE Username='" . $user . "'";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $return -> username = $row["Username"];
        $return -> email = $row["Email"];
        
        if ($returnJSON === null)
            $returnJSON = json_encode($return);
        else
            $returnJSON = $returnJSON . "&". json_encode($return);
    }
}

echo $returnJSON;
$conn->close();
?>