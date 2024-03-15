<?php
// Файл api.php

// Параметры подключения к базе данных
$db_host = 'localhost';
$db_user = 'root';
$db_password = '';
$db_name = 'lab5';

// Подключение к базе данных
$conn = new mysqli($db_host, $db_user, $db_password, $db_name);

// Проверка соединения
if ($conn->connect_error) {
    die("Connection failed: " . $conn->connect_error);
}

// Пример запроса к базе данных
$query = "SELECT * FROM my_table";
$result = $conn->query($query);

// Преобразование результата в массив
$data = array();
while ($row = $result->fetch_assoc()) {
    $data[] = $row;
}

// Вывод данных в формате JSON
header('Content-Type: application/json');
echo json_encode($data);

// Закрытие соединения
$conn->close();
?>
