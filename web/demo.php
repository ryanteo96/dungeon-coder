
mysql -h dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com -P 3306 -u dungeoncoder -p


$dsn = 'mysql:host=dbinstance.ch8dbkdmk4qi.us-east-2.rds.amazonaws.com;port=3306;dbname=userAccounts';
$username = 'dungeoncoder';
$password = 'DungeonCoder23';

$dbh = new PDO($dsn, $username, $password);