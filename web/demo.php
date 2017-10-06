<?php
//37536
$PORT = 37536; //the port on which we are connecting to the "remote" machine
//HOST = "localhost";
$HOST = "13.59.183.75"; //the ip of the remote machine (in this case it's the same machine)
$sock = socket_create(AF_INET, SOCK_STREAM, 0) //Creating a TCP socket
  or die("error: could not create socket\n");

$succ = socket_connect($sock, $HOST, $PORT) //Connecting to to server using that socket
    or die("error: could not connect to host\n");

$text = "Hello, Java!"; //the text we want to send to the server
$user = "TestUser";
$pass = "NewPass";
$true = "true";
$false = "false";
$opcode = "0x00";

function writeUTF($string) {
    $utfString = utf8_encode($string);
    $length = strlen($utfString);
    $pack = pack("n", $length);
    return $pack . $utfString;
}

$test = hex2bin($opcode);

$utfUser = writeUTF($user . "\n");
$utfPass = writeUTF($pass . "\n");

socket_sendto($sock, $test . "\n", strlen($test) + 1)
    or die("error: failed to write to socket\n");
/*$reply = socket_read($sock, 10000)
    or die("error: failed to read to socket\n");
echo $reply;*/

if (socket_read($sock, 10000, PHP_NORMAL_READ) == false) {
    socket_sendto($sock, $utfUser ,strlen($utfUser));
    socket_sendto($sock, $utfPass ,strlen($utfPass));
    echo "success!";
    if (socket_read($sock, 10000, PHP_NORMAL_READ) == "$msg\x10") {
        echo $true;
    } else if (socket_read($sock, 10000, PHP_NORMAL_READ) == "$msg\x60") {
        echo $false;
    } else if (socket_read($sock, 10000, PHP_NORMAL_READ) == "$msg\x40") {
        echo $false;
    }
}

/*socket_write($sock, $text . "\n", strlen($text) + 1) //Writing the text to the socket
        or die("error: failed to write to socket\n");

$reply = socket_read($sock, 10000, PHP_NORMAL_READ) //Reading the reply from socket
        or die("error: failed to read from socket\n");

echo $reply;
/*


$dsn = 'mysql:host=dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com;port=3306;dbname=userAccounts';
$username = 'dungeoncoder';
$password = 'DungeonCoder23';

$dbh = new PDO($dsn, $username, $password);

$result = $dbh->query('SELECT USERNAME FROM UserAccounts');

echo $result;*/
?>