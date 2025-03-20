-- Patients table
CREATE TABLE Patients (
    patientID INT PRIMARY KEY,
    patientName VARCHAR(100),
    phoneNo VARCHAR(20),
    email VARCHAR(100)
);

-- Doctors table
CREATE TABLE Doctors (
    doctorID INT PRIMARY KEY,
    doctorName VARCHAR(100),
    specialization VARCHAR(100),
    phoneNo VARCHAR(20),
    email VARCHAR(100)
);

-- Doctor credentials linked to doctors
CREATE TABLE doctorcredentials (
    username VARCHAR(255) NOT NULL,
    password VARCHAR(64) DEFAULT NULL,
    doctorID INT,
    PRIMARY KEY (username),
    FOREIGN KEY (doctorID) REFERENCES Doctors(doctorID) ON DELETE CASCADE
);

-- Bookings table linked to patients and doctors
CREATE TABLE Bookings (
    bookingNo INT PRIMARY KEY,
    dayOfBooking INT NOT NULL,
    monthOfBooking INT NOT NULL,
    yearOfBooking YEAR NOT NULL,
    bookingTime TIME DEFAULT NULL,
    patientID INT,
    doctorID INT,
    FOREIGN KEY (patientID) REFERENCES Patients(patientID),
    FOREIGN KEY (doctorID) REFERENCES Doctors(doctorID)
);

-- Visit details table linked to bookings
CREATE TABLE Visits (
    visitID INT PRIMARY KEY,
    bookingNo INT,
    visitDate DATETIME DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    FOREIGN KEY (bookingNo) REFERENCES Bookings(bookingNo)
);

-- Prescriptions table connected to visits
CREATE TABLE Prescriptions (
    prescriptionID INT PRIMARY KEY,
    visitID INT,
    medicationName VARCHAR(100),
    dosage VARCHAR(50),
    duration VARCHAR(50),
    instructions TEXT,
    FOREIGN KEY (visitID) REFERENCES Visits(visitID)
);
