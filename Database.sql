CREATE TABLE `bookings` (
  `bookingNo` int NOT NULL AUTO_INCREMENT,
  `bookingDate` date DEFAULT NULL,
  `bookingTime` time DEFAULT NULL,
  `patientName` varchar(100) DEFAULT NULL,
  `doctorName` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`bookingNo`)
  
CREATE TABLE `doctorcredentials` (
  `username` varchar(255) NOT NULL,
  `password` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`username`)
  
 
--  doctorinterface is the name of the database i used so please name it in the same way. 