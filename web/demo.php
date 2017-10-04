<?php
$myObj->name = "John";
$myObj->age = 30;
$myObj->city = "New York";

$myJSON = json_encode($myObj);

echo $myJSON;

/*$dsn = 'mysql:host=dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com;port=3306;dbname=userAccounts';
$username = 'dungeoncoder';
$password = 'DungeonCoder23';

$dbh = new PDO($dsn, $username, $password);

$result = $dbh->query('SELECT USERNAME FROM UserAccounts');

echo $result;*/
?>