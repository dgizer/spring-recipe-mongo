## Use to run mysql db docker image, optional if you're not using a local mysqldb
# docker run --name mysql-db -p 3307:3306 -e MYSQL_ROOT_PASSWORD=12345 -d mysql

# connect to mysql and run as root user
#Create Databases
CREATE DATABASE dev;
CREATE DATABASE prod;

#Create database service accounts
CREATE USER 'dev_user'@'localhost' IDENTIFIED BY 'passdev';
CREATE USER 'prod_user'@'localhost' IDENTIFIED BY 'passprod';
CREATE USER 'dev_user'@'%' IDENTIFIED BY 'passdev';
CREATE USER 'prod_user'@'%' IDENTIFIED BY 'passprod';

#Database grants
GRANT DELETE, INSERT, SELECT, UPDATE ON dev.* to 'dev_user'@'localhost', 'dev_user'@'%';
GRANT DELETE, INSERT, SELECT, UPDATE ON prod.* to 'prod_user'@'localhost', 'prod_user'@'%';