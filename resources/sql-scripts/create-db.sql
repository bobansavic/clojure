USE [master]
IF NOT EXISTS (SELECT name FROM master.sys.databases WHERE name = N'agilityTestDb')
BEGIN
CREATE DATABASE [agilityTestDb]
END
