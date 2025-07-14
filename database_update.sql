-- Script SQL untuk mengupdate database SIKOPET
-- Jalankan script ini di phpMyAdmin atau MySQL client

-- 1. Menambahkan kolom No_HP dan Tanggal ke tabel data_warga
ALTER TABLE `data_warga` 
ADD COLUMN `No_HP` VARCHAR(15) NULL AFTER `NIK`,
ADD COLUMN `Tanggal` VARCHAR(20) NULL AFTER `Kategori`;

-- 2. Mengupdate data yang sudah ada dengan nilai default
UPDATE `data_warga` 
SET `No_HP` = '0000000000', 
    `Tanggal` = '1/JANUARI/2025' 
WHERE `No_HP` IS NULL OR `Tanggal` IS NULL;

-- 3. Menampilkan struktur tabel yang sudah diupdate
DESCRIBE `data_warga`;

-- 4. Menampilkan data yang sudah diupdate
SELECT * FROM `data_warga`;

-- CATATAN: Setelah menjalankan script ini, struktur tabel data_warga akan menjadi:
-- - id (int)
-- - Nama_Warga (varchar)
-- - NIK (varchar)
-- - No_HP (varchar) <- BARU
-- - Jumlah (varchar)
-- - Kategori (varchar)
-- - Tanggal (varchar) <- BARU
