<?php
$PORT = 37536; //the port on which we are connecting to the "remote" machine
$HOST = "localhost"
//$HOST = "13.59.183.75"; //the ip of the remote machine (in this case it's the same machine)
$sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
  or die("error: could not create socket\n");

$succ = socket_connect($sock, $HOST, $PORT) //Connecting to to server using that socket
    or die("error: could not connect to host\n");

$text = "Hello, Java!"; //the text we want to send to the server
$user = "TestUser";
$pass = "NewPass";

socket_write($socket,"$msg\x01" . "\n",strlen($msg)+1);

if (socket_read($sock, 10000, PHP_NORMAL_READ) == "$msg\x10") {
    socket_write($socket, $user . "\n",strlen($user)+1);
    socket_write($socket, $pass . "\n",strlen($pass)+1);
    if (socket_read($sock, 10000, PHP_NORMAL_READ) == "$msg\x10") {
        echo true;
    } else if (socket_read($sock, 10000, PHP_NORMAL_READ) == "$msg\x60") {
        echo false;
    } else if (socket_read($sock, 10000, PHP_NORMAL_READ) == "$msg\x40") {
        echo false;
    }
}


/*
socket_write($sock, $text . "\n", strlen($text) + 1) //Writing the text to the socket
        or die("error: failed to write to socket\n");

$reply = socket_read($sock, 10000, PHP_NORMAL_READ) //Reading the reply from socket
        or die("error: failed to read from socket\n");

echo $reply;


$dsn = 'mysql:host=dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com;port=3306;dbname=userAccounts';
$username = 'dungeoncoder';
$password = 'DungeonCoder23';

$dbh = new PDO($dsn, $username, $password);

$result = $dbh->query('SELECT USERNAME FROM UserAccounts');

echo $result;*/
?>