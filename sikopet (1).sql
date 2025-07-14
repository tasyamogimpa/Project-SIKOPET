-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jul 14, 2025 at 09:18 AM
-- Server version: 10.4.32-MariaDB
-- PHP Version: 8.0.30

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `sikopet`
--

-- --------------------------------------------------------

--
-- Table structure for table `data_warga`
--

CREATE TABLE `data_warga` (
  `id` int(20) NOT NULL,
  `Nama_Warga` varchar(30) NOT NULL,
  `NIK` varchar(20) NOT NULL,
  `No_HP` varchar(15) DEFAULT NULL,
  `Jumlah` varchar(20) NOT NULL,
  `Kategori` varchar(20) NOT NULL,
  `Tanggal` varchar(20) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data for table `data_warga`
--

INSERT INTO `data_warga` (`id`, `Nama_Warga`, `NIK`, `No_HP`, `Jumlah`, `Kategori`, `Tanggal`) VALUES
(2, 'sdsd', '1287', '1130', 'RP. 400.000', 'Pengeluaran', '1/JAN/2025'),
(3, 'sdsd', '1287', '1130', 'RP. 400.000', 'Pengeluaran', '1/JAN/2025'),
(5, 'dww', 'ww', '324', 'RP. 100.000', 'Pemasukan', '3/JAN/2025'),
(6, 'Test Pengeluaran', '12345', '08123456789', 'RP. 50.000', 'Pengeluaran', '5/JAN/2025'),
(7, 'jhgsdcsh', '8347', '03897', 'RP.1.000.000', 'Pemasukan', '3/MAR/2026'),
(8, 'adda', '12212', '88323', 'RP. 100.000', 'Pemasukan', '1/JAN/2025'),
(9, 'tasya', '265', '98868', 'RP. 700.000', 'Pemasukan', '1/MAR/2027'),
(10, 'sdsd', '1214', '113', 'RP. 600.000', 'Pengeluaran', '4/JAN/2025');

-- --------------------------------------------------------

--
-- Table structure for table `pemasukan`
--

CREATE TABLE `pemasukan` (
  `id` int(35) NOT NULL,
  `Nama` varchar(40) NOT NULL,
  `NIK` varchar(20) NOT NULL,
  `Hari/Tgl` varchar(15) NOT NULL,
  `Jumlah` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Table structure for table `pengeluaran`
--

CREATE TABLE `pengeluaran` (
  `id` int(35) NOT NULL,
  `Nama` varchar(40) NOT NULL,
  `NIK` varchar(20) NOT NULL,
  `Hari/Tgl` varchar(30) NOT NULL,
  `Jumlah` int(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `data_warga`
--
ALTER TABLE `data_warga`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pemasukan`
--
ALTER TABLE `pemasukan`
  ADD PRIMARY KEY (`id`);

--
-- Indexes for table `pengeluaran`
--
ALTER TABLE `pengeluaran`
  ADD PRIMARY KEY (`id`);

--
-- AUTO_INCREMENT for dumped tables
--

--
-- AUTO_INCREMENT for table `data_warga`
--
ALTER TABLE `data_warga`
  MODIFY `id` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=11;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
