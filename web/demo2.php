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

$sql = "SELECT username , hash, salt FROM UserAccounts";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        $salt = $row["salt"];
        $enSalt = base64_encode($salt);
        $hashed = $row["hash"];
        exec($param . "NewPass " . $salt . " " . $hashed , $return);
        echo $return[0];
    }
} else {
    echo "0 results";
}
$conn->close();

?>