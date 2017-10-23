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

$currentUser = $_GET["currentUser"];
$user = $_GET["user"];
$pass = $_GET["pass"];
$email = $_GET["email"];

$salt = "E6F455FCCF52E1F9EC295EE052682AC8";

if ($pass != null) {
    exec($param . $pass . " " . $salt , $hashed);
}

if ($hashed != null) {
    $sql = "UPDATE Users SET Hash='" . $hashed[0] . "'" . " WHERE Username='" . $currentUser ."'" ;
    $conn->query($sql);
}

if ($email != null) {
    $sql = "UPDATE Users SET Email='" . $email . "'" . " WHERE Username='" . $currentUser ."'" ;
    $conn->query($sql);
}

if ($user != null) {

    $sql = "SELECT Username FROM Users";
    $result = $conn->query($sql);
    
    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            if ($user == $currentUser ) {}
            else if ($user == $row["Username"]) {
                echo "False";
                $conn->close();
            }
        }
    }

    $sql = "UPDATE Users SET Username='" . $user . "'" . " WHERE Username='" . $currentUser ."'" ;
    $conn->query($sql);
    $sql = "UPDATE Task1 SET Student='" . $user . "'" . " WHERE Student='" . $currentUser ."'" ;
    $conn->query($sql);
}

echo "True";
$conn->close();
?>