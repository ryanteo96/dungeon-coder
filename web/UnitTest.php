<?php
$servername = "dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com";
$username = "dungeoncoder";
$password = "DungeonCoder23";
$dbname = "userAccounts";

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
$test = $_GET["test"];
$mode = $_GET["mode"];
$salt = "E6F455FCCF52E1F9EC295EE052682AC8";
$user = "test";
$pass = "testPass";

$param = "java Hash ";
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

if ($test === "account_create_student") {
    $sql = "SELECT Username FROM Users";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            if ($row["Username"] === "test") {
                echo "FAIL";
                $conn->close();
            } 
        }
    }

    exec($param . "testPass " . $salt , $return);
    $sql = "INSERT INTO Users (Username, Hash, Salt, Type) VALUES ('test','". $return[0] . "','" . $salt . "','student')";
    $conn->query($sql);

    $sql = "INSERT INTO Task1 (Student) VALUES ('test')";
    $conn->query($sql);

    $sql = "SELECT Username FROM Users";
    $result = $conn->query($sql);
    // output data of each row
    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            if ($row["Username"] === 'test') {
                echo "PASS";
            }
        } 
    }

    $sql = "DELETE FROM Users WHERE Username='test'";
    $conn->query($sql);
    $sql = "DELETE FROM Task1 WHERE Student='test'";
    $conn->query($sql);
}

if ($test === "account_create_teacher") {
    $sql = "SELECT Username FROM Users";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            if ($row["Username"] === "test") {
                echo "FAIL";
                $conn->close();
            } 
        }
    }

    exec($param . "testPass " . $salt , $return);
    $sql = "INSERT INTO Users (Username, Hash, Salt, Type) VALUES ('test','". $return[0] . "','" . $salt . "','teacher')";
    $conn->query($sql);

    $sql = "SELECT Username FROM Users";
    $result = $conn->query($sql);
    // output data of each row
    if ($result->num_rows > 0) {
        while($row = $result->fetch_assoc()) {
            if ($row["Username"] === 'test') {
                echo "PASS";
            }
        } 
    }

    $sql = "DELETE FROM Users WHERE Username='test'";
    $conn->query($sql);
}


if ($test === "sign_in") {
    $sql = "SELECT Username FROM Users";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            if ($row["Username"] === "test") {
                echo "FAIL";
                $conn->close();
            } 
        }
    }

    exec($param . "testPass " . $salt , $return);
    $sql = "INSERT INTO Users (Username, Hash, Salt, Type) VALUES ('test','". $return[0] . "','" . $salt . "','student')";
    $conn->query($sql);
    $sql = "INSERT INTO Task1 (Student) VALUES ('test')";
    $conn->query($sql);

    if ($mode === "correct_login") {
        $sql = "SELECT Username , Hash, Salt, Type FROM Users";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Username"]) {
                    $salt = $row["Salt"];
                    $hash = $row["Hash"];
                    $type = $row["Type"];
                    exec($param . $pass . " " . $salt , $hashed);
                    if ($hashed[0] == $hash) {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "wrong_username") {
        $sql = "SELECT Username , Hash, Salt, Type FROM Users";
        $result = $conn->query($sql);
        $return = "PASS";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($row["Username"] === "test1") {
                    $salt = $row["Salt"];
                    $hash = $row["Hash"];
                    $type = $row["Type"];
                    exec($param . $pass . " " . $salt , $hashed);
                    if ($hashed[0] == $hash) {
                        $return = "FAIL";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "wrong_password") {
        $sql = "SELECT Username , Hash, Salt, Type FROM Users";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Username"]) {
                    $salt = $row["Salt"];
                    $hash = $row["Hash"];
                    $type = $row["Type"];
                    exec($param . "testPass1 " . $salt , $hashed);
                    if ($hashed[0] == $hash) {
                        $return = "FAIL";
                    } else 
                        $return = "PASS";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    $sql = "DELETE FROM Users WHERE Username='test'";
    $conn->query($sql);
    $sql = "DELETE FROM Task1 WHERE Student='test'";
    $conn->query($sql);
}

if ($test === "create_announcement") {
    $date = "2017-10-10";
    $announcement = "test";

    $sql = "INSERT INTO Announcements (" ."Date" .", Announcement) VALUES ('" . $date . "','" . $announcement . "')";
    $conn->query($sql);

    $sql = "SELECT " . "Date" . ",Announcement FROM Announcements";
    $result = $conn->query($sql);

    $return = "FAIL";

    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            if ($date === $row["Date"] && $announcement === $row["Announcement"])
                $return = "PASS";
            else 
                $return = "FAIL";
        }

        echo $return;
    }

    $sql = "DELETE FROM Announcements WHERE " . "Date" . "='" . $date . "' AND Announcement='" . $announcement . "'";
    $conn->query($sql);
}

if ($test === "set_values") {
    $sql = "SELECT Username FROM Users";
    $result = $conn->query($sql);

    if ($result->num_rows > 0) {
        // output data of each row
        while($row = $result->fetch_assoc()) {
            if ($row["Username"] === "test") {
                echo "FAIL";
                $conn->close();
            } 
        }
    }

    exec($param . "testPass " . $salt , $return);
    $sql = "INSERT INTO Users (Username, Hash, Salt, Type) VALUES ('test','". $return[0] . "','" . $salt . "','student')";
    $conn->query($sql);
    $sql = "INSERT INTO Task1 (Student) VALUES ('test')";
    $conn->query($sql);

    if ($mode === "grade") {
        $sql = "UPDATE Users SET Grade='A' WHERE Username='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Username, Grade FROM Users";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Username"]) {
                    if ($row["Grade"] === "A") {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "priority") {
        $sql = "UPDATE Users SET Priority=1 WHERE Username='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Username, Priority FROM Users";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Username"]) {
                    if ($row["Priority"] === "1") {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "lock") {
        $sql = "UPDATE Users SET LockStatus=1 WHERE Username='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Username, LockStatus FROM Users";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Username"]) {
                    if ($row["LockStatus"] === "1") {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "comment") {
        $sql = "UPDATE Task1 SET Comments='HI' WHERE Student='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Student, Comments FROM Task1";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Student"]) {
                    if ($row["Comments"] === "HI") {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "points") {
        $sql = "UPDATE Task1 SET PointValue=100 WHERE Student='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Student, PointValue FROM Task1";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Student"]) {
                    if ($row["PointValue"] === "100") {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "deadline") {
        $sql = "UPDATE Task1 SET Deadline='2017-10-10' WHERE Student='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Student, Deadline FROM Task1";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Student"]) {
                    if ($row["Deadline"] === "2017-10-10") {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "email") {
        $sql = "UPDATE Users SET Email='test@test.com' WHERE Username='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Username, Email FROM Users";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user == $row["Username"]) {
                    if ($row["Email"] === "test@test.com") {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    if ($mode === "password") {
        exec($param . "newPass " . $salt , $hashed);
        $sql = "UPDATE Users SET Hash='" . $hashed[0] . "' WHERE Username='" . $user ."'" ;
        $conn->query($sql);

        $sql = "SELECT Username, Hash FROM Users";
        $result = $conn->query($sql);
        $return = "FAIL";

        if ($result->num_rows > 0) {
            // output data of each row
            while($row = $result->fetch_assoc()) {
                if ($user === $row["Username"]) {
                    exec($param . "newPass " . $salt , $hashed);
                    if ($row["Hash"] === $hashed[0]) {
                        $return = "PASS";
                    } else 
                        $return = "FAIL";
                }
            }
        } else {
            $return = "FAIL";
        }

        echo $return;
    }

    $sql = "DELETE FROM Users WHERE Username='test'";
    $conn->query($sql);
    $sql = "DELETE FROM Task1 WHERE Student='test'";
    $conn->query($sql);
}


$conn->close();

?>