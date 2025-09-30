# 1 Abrir o terminal do MySQL
# Utilizando cmd adm:
"C:\Program Files\MySQL\MySQL Server 8.0\bin\mysql.exe" -u root -p

# 2 Conectar como root
mysql -u root -p
# 3 Depois digitar a senha
# Enter password: senhaprojeto@2025

# 4 Selecionar o banco que foi criado
USE drone_delivery;

# 5 Limpar dados das tabelas 
DELETE FROM Entrega;
ALTER TABLE Entrega AUTO_INCREMENT = 1;

DELETE FROM Drone;
ALTER TABLE Drone AUTO_INCREMENT = 1;

DELETE FROM Cliente;
ALTER TABLE Cliente AUTO_INCREMENT = 1;

# 6 Mostrar os dados das tabelas
SELECT * FROM Cliente;
SELECT * FROM Drone;
SELECT * FROM Entrega;

# 7 Fechar a sessão (mantem os dados salvos)
EXIT;