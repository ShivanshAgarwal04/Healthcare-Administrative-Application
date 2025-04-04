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
    patientName VARCHAR(255) NOT NULL,
    doctorName VARCHAR(255) NOT NULL,
    FOREIGN KEY (patientID) REFERENCES Patients(patientID),
    FOREIGN KEY (doctorID) REFERENCES Doctors(doctorID)
);

-- Visits Table
CREATE TABLE Visits (
    visitID INT PRIMARY KEY AUTO_INCREMENT,
    bookingNo INT UNIQUE NOT NULL,
    notes TEXT,
    FOREIGN KEY (bookingNo) REFERENCES Bookings(bookingNo) ON DELETE CASCADE
);

-- Prescriptions Table
CREATE TABLE Prescriptions (
    prescriptionID INT PRIMARY KEY AUTO_INCREMENT,
    visitID INT NOT NULL,
    medicationName VARCHAR(100) NOT NULL,
    dosage VARCHAR(50) NOT NULL,
    duration VARCHAR(50) NOT NULL,
    instructions TEXT,
    FOREIGN KEY (visitID) REFERENCES Visits(visitID) ON DELETE CASCADE
);

-- Insert into Patients table
INSERT INTO Patients (patientID, patientName, phoneNo, email) VALUES
(10001, 'John Doe', '123-456-7890', 'johndoe@example.com'),
(10002, 'Jane Smith', '987-654-3210', 'janesmith@example.com'),
(10003, 'Robert Johnson', '321-654-0987', 'robert.johnson@example.com'),
(10004, 'Emily Davis', '789-123-4560', 'emily.davis@example.com');

-- Insert into Doctors table
INSERT INTO Doctors (doctorID, doctorName, specialization, phoneNo, email) VALUES
(20001, 'Dr. Alice Brown', 'Cardiology', '555-123-4567', 'alice.brown@example.com'),
(20002, 'Dr. Bob White', 'Neurology', '555-987-6543', 'bob.white@example.com'),
(20003, 'Dr. Charlie Green', 'Dermatology', '555-741-8529', 'charlie.green@example.com');

-- Insert into doctorcredentials table
INSERT INTO doctorcredentials (username, password, doctorID) VALUES
('alicebrown', 'securepassword1', 20001),
('bobwhite', 'securepassword2', 20002),
('charliegreen', 'securepassword3', 20003);

-- Insert into Bookings table
INSERT INTO Bookings (bookingNo, dayOfBooking, monthOfBooking, yearOfBooking, bookingTime, patientID, doctorID) VALUES
(30001, 5, 4, 2025, '10:00:00', 10001, 20001),
(30002, 6, 4, 2025, '11:00:00', 10002, 20002),
(30003, 7, 4, 2025, '09:30:00', 10003, 20003),
(30004, 8, 4, 2025, '14:45:00', 10004, 20001);

-- Insert into Visits table
INSERT INTO Visits (visitID, bookingNo, notes) VALUES
(40001, 30001, 'Routine check-up. Blood pressure normal.'),
(40002, 30002, 'Follow-up visit. Patient reports improvement.'),
(40003, 30003, 'Skin rash examination. Prescribed ointment.'),
(40004, 30004, 'General consultation. No major issues.');

-- Insert into Prescriptions table
INSERT INTO Prescriptions (prescriptionID, visitID, medicationName, dosage, duration, instructions) VALUES
(50001, 40001, 'Aspirin', '100mg', '7 days', 'Take one tablet daily with food.'),
(50002, 40002, 'Ibuprofen', '200mg', '5 days', 'Take after meals to avoid stomach upset.'),
(50003, 40003, 'Hydrocortisone Cream', '1%', '10 days', 'Apply twice daily to affected area.'),
(50004, 40004, 'Multivitamin', '1 tablet', '30 days', 'Take one tablet every morning.');




