<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$user = $_GET["username"];
$pass = $_GET["password"];
$type = $_GET["userType"];
$salt = "E6F455FCCF52E1F9EC295EE052682AC8";


$param = "java Hash ";
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "SELECT Username FROM Users";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        if ($user == $row["Username"]) {
            echo "False";
            $conn->close();
        } 
    }
}

exec($param . $pass . " " . $salt , $return);
$sql = "INSERT INTO Users (Username, Hash, Salt, Type) VALUES ('" . $user . "','" . $return[0] . "','" . $salt . "','" . $type . "')";
$conn->query($sql);

if ($type == "student") {
    $sql = "INSERT INTO Task1 (Student) VALUES ('" . $user . "')";
    $conn->query($sql);
}

$sql = "SELECT Username FROM Users";
$result = $conn->query($sql);
// output data of each row
if ($result->num_rows > 0) {
    while($row = $result->fetch_assoc()) {
        if ($user == $row["Username"]) {
            echo "True";
            $conn->close();
        }
    } 
}

$conn->close();

?>