<?php
$servername = "localhost";
$username = "id4223198_mobile";
$password = "MOBILE12345";
$dbname = "id4223198_mobile";
$r = $_REQUEST['search'];
$o = $_REQUEST['order'];

// Create connection
$conn = new mysqli($servername, $username, $password, $dbname);
// Check connection
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
} 

$sql = "SELECT * FROM `mobile` join `price` on mobile.id = price.id WHERE name LIKE '%$r%' order by $o";

$result = $conn->query($sql);
$i = 1;
if ($result->num_rows > 0) {
    // output data of each row
    while($row = $result->fetch_assoc()) {
    
        echo $row["id"]."#".$row["brand"]."#".$row["name"]."#".$row["announced"]."#".$row["dimensions"]."#".$row["screen"]."#".$row["weight"]."#".$row["os"]."#".$row["resolution"]."#".$row["cpu"]."#".$row["gpu"]."#".$row["ram"]."#".$row["rom"]."#".$row["exrom"]."#".$row["technology"]."#".$row["bluetooth"]."#".$row["wifi"]."#".$row["nfc"]."#".$row["gps"]."#".$row["fcam"]."#".$row["bcam"]."#".$row["video"]."#".$row["battery"]."#".$row["color"]."#".$row["duosim"]."#".$row["waterproof"]."#".$row["image"]."#".$row["insideImage"]."#".$row["lazada"]."#".$row["supertstore"]."#".$row["topvalue"]."#".$row["link1"]."#".$row["link2"]."#".$row["link3"].";";
    
        $i++;
    }
    
} else {
    echo "0 results";
}
    $conn->close();

?>