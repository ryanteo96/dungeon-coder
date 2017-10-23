<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$user = $_GET["username"];
$pass = $_GET["password"];
$return = "False";

$param = "java Hash ";
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 


$sql = "SELECT Username , Hash, Salt, Type FROM Users";
$result = $conn->query($sql);

if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
        if ($user == $row["Username"]) {
            $salt = $row["Salt"];
            $hash = $row["Hash"];
            $type = $row["Type"];
            exec($param . $pass . " " . $salt , $hashed);
            if ($hashed[0] == $hash) {
                if ($type == "student")
                    $return = "True, Student";
                else if ($type == "teacher")
                    $return = "True, Teacher";
                else
                    $return = "False";
            } else 
                $return = "False";
        }
    }
} else {
    $return = "False";
}

echo $return;
$conn->close();

?>