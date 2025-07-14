/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package sikopet;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;
import java.text.DecimalFormat;

public class JLaporan extends JFrame {

    JTable table;
    DefaultTableModel model;
    JLabel lblTotalPemasukan, lblTotalPengeluaran, lblSaldoAkhir;
    JComboBox<String> cbBulan;
    JTextField txtSearchBulan;
    JButton btnCari, btnReset, btnCariManual;

    public JLaporan() {
        // Initialize form components first
        initComponents();
        
        // Setup table model untuk form yang sudah di-generate
        String[] kolom = {"ID", "Nama", "NIK", "No HP", "Tanggal", "Jumlah", "Kategori"};
        model = new DefaultTableModel(kolom, 0);
        tblkeuangan.setModel(model);
        
        // Tambahkan styling untuk tabel
        customizeTable();
        
        // Tambahkan panel ringkasan
        addSummaryPanel();
        
        // Tambahkan panel pencarian
        addSearchPanel();
        
        // Set judul window dan icon
        setTitle("SIKOPET - Laporan Keuangan");
        
        // Set posisi tengah layar
        setLocationRelativeTo(null);

        // Tampilkan data dari database
        loadDataFromDatabase();
        
        // Tambahkan event listener untuk menu items
        addMenuListeners();
    }
    
    private void addSummaryPanel() {
        // Panel ringkasan dengan padding dan margin yang lebih baik
        JPanel summaryPanel = new JPanel(new GridLayout(1, 3, 15, 15));
        summaryPanel.setBackground(new Color(240, 240, 255));
        summaryPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(null, "Ringkasan Keuangan", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                new Font("Segoe UI", Font.BOLD, 16), 
                new Color(0, 0, 102)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        // Label untuk total pemasukan dengan desain yang lebih menarik
        JPanel panelPemasukan = new JPanel(new BorderLayout());
        panelPemasukan.setBackground(new Color(144, 238, 144)); // Light green
        panelPemasukan.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 150, 0), 2),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel labelPemasukan = new JLabel("💰 TOTAL PEMASUKAN", JLabel.CENTER);
        labelPemasukan.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelPemasukan.setForeground(new Color(0, 100, 0));
        lblTotalPemasukan = new JLabel("Rp 0", JLabel.CENTER);
        lblTotalPemasukan.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalPemasukan.setForeground(new Color(0, 100, 0));
        panelPemasukan.add(labelPemasukan, BorderLayout.NORTH);
        panelPemasukan.add(lblTotalPemasukan, BorderLayout.CENTER);
        
        // Label untuk total pengeluaran dengan desain yang lebih menarik
        JPanel panelPengeluaran = new JPanel(new BorderLayout());
        panelPengeluaran.setBackground(new Color(255, 182, 193)); // Light pink
        panelPengeluaran.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(200, 0, 0), 2),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel labelPengeluaran = new JLabel("💸 TOTAL PENGELUARAN", JLabel.CENTER);
        labelPengeluaran.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelPengeluaran.setForeground(new Color(139, 0, 0));
        lblTotalPengeluaran = new JLabel("Rp 0", JLabel.CENTER);
        lblTotalPengeluaran.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblTotalPengeluaran.setForeground(new Color(139, 0, 0));
        panelPengeluaran.add(labelPengeluaran, BorderLayout.NORTH);
        panelPengeluaran.add(lblTotalPengeluaran, BorderLayout.CENTER);
        
        // Label untuk saldo akhir dengan desain yang lebih menarik
        JPanel panelSaldo = new JPanel(new BorderLayout());
        panelSaldo.setBackground(new Color(173, 216, 230)); // Light blue
        panelSaldo.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createLineBorder(new Color(0, 100, 200), 2),
            BorderFactory.createEmptyBorder(15, 10, 15, 10)
        ));
        JLabel labelSaldo = new JLabel("💰 SALDO AKHIR", JLabel.CENTER);
        labelSaldo.setFont(new Font("Segoe UI", Font.BOLD, 13));
        labelSaldo.setForeground(new Color(0, 0, 139));
        lblSaldoAkhir = new JLabel("Rp 0", JLabel.CENTER);
        lblSaldoAkhir.setFont(new Font("Segoe UI", Font.BOLD, 18));
        lblSaldoAkhir.setForeground(new Color(0, 0, 139));
        panelSaldo.add(labelSaldo, BorderLayout.NORTH);
        panelSaldo.add(lblSaldoAkhir, BorderLayout.CENTER);
        
        summaryPanel.add(panelPemasukan);
        summaryPanel.add(panelPengeluaran);
        summaryPanel.add(panelSaldo);
        
        // Tambahkan panel ringkasan ke layout utama
        jPanel1.setLayout(new BorderLayout());
        jPanel1.add(jScrollPane2, BorderLayout.NORTH);
        jPanel1.add(jPanel2, BorderLayout.CENTER);
        jPanel1.add(summaryPanel, BorderLayout.SOUTH);
    }

    private void loadDataFromDatabase() {
        // Kosongkan tabel sebelum memuat data baru
        model.setRowCount(0);
        
        try {
            // Koneksi ke MySQL (XAMPP)
            Connection cn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sikopet", "root", "");

            // Query semua data dari data_warga
            String sqlAllData = "SELECT * FROM data_warga ORDER BY Kategori, Tanggal";
            ResultSet rsAllData = cn.createStatement().executeQuery(sqlAllData);

            int totalPemasukan = 0;
            int totalPengeluaran = 0;
            
            // Isi data ke tabel dan hitung total
            while (rsAllData.next()) {
                // Konversi jumlah ke angka menggunakan helper method
                String jumlahStr = rsAllData.getString("Jumlah");
                int jumlahInt = parseJumlah(jumlahStr);
                
                String kategori = rsAllData.getString("Kategori");
                
                // Hitung total berdasarkan kategori
                if (kategori.equalsIgnoreCase("Pemasukan")) {
                    totalPemasukan += jumlahInt;
                } else if (kategori.equalsIgnoreCase("Pengeluaran")) {
                    totalPengeluaran += jumlahInt;
                }
                
                Object[] row = {
                    rsAllData.getInt("id"),
                    rsAllData.getString("Nama_Warga"),
                    rsAllData.getString("NIK"),
                    rsAllData.getString("No_HP"),
                    rsAllData.getString("Tanggal"),
                    jumlahInt,
                    kategori
                };
                model.addRow(row);
            }
            rsAllData.close();
            
            // Update labels ringkasan
            updateSummaryLabels(totalPemasukan, totalPengeluaran);

            cn.close();

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mengambil data:\n" + e.getMessage());
        }
    }
    
    private void customizeTable() {
        // Styling untuk header tabel
        tblkeuangan.getTableHeader().setBackground(new Color(102, 102, 255));
        tblkeuangan.getTableHeader().setForeground(Color.WHITE);
        tblkeuangan.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        
        // Styling untuk baris tabel
        tblkeuangan.setRowHeight(25);
        tblkeuangan.setGridColor(new Color(200, 200, 200));
        tblkeuangan.setSelectionBackground(new Color(200, 200, 255));
        tblkeuangan.setSelectionForeground(Color.BLACK);
        
        // Auto resize kolom
        tblkeuangan.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
    }
    
    private void updateSummaryLabels(int totalPemasukan, int totalPengeluaran) {
        // Format angka dengan pemisah ribuan
        java.text.DecimalFormat df = new java.text.DecimalFormat("#,###");
        
        // Update label total pemasukan
        lblTotalPemasukan.setText("Rp " + df.format(totalPemasukan));
        
        // Update label total pengeluaran
        lblTotalPengeluaran.setText("Rp " + df.format(totalPengeluaran));
        
        // Update label saldo akhir
        int saldoAkhir = totalPemasukan - totalPengeluaran;
        lblSaldoAkhir.setText("Rp " + df.format(saldoAkhir));
        
        // Ubah warna saldo akhir berdasarkan nilai dengan efek visual
        if (saldoAkhir > 0) {
            lblSaldoAkhir.setForeground(new Color(0, 150, 0)); // Hijau untuk positif
            lblSaldoAkhir.setText("+ Rp " + df.format(saldoAkhir));
        } else if (saldoAkhir < 0) {
            lblSaldoAkhir.setForeground(new Color(200, 0, 0)); // Merah untuk negatif  
            lblSaldoAkhir.setText("- Rp " + df.format(Math.abs(saldoAkhir)));
        } else {
            lblSaldoAkhir.setForeground(new Color(0, 0, 139)); // Biru untuk nol
            lblSaldoAkhir.setText("Rp " + df.format(saldoAkhir));
        }
    }
    
    private void addSearchPanel() {
        // Ganti jScrollPane2 dengan panel pencarian
        jScrollPane2.removeAll();
        
        // Panel pencarian dengan desain yang menarik
        JPanel searchPanel = new JPanel(new GridBagLayout());
        searchPanel.setBackground(new Color(230, 230, 250));
        searchPanel.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createTitledBorder(null, "🔍 Pencarian Data Bulanan", 
                javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, 
                javax.swing.border.TitledBorder.DEFAULT_POSITION, 
                new Font("Segoe UI", Font.BOLD, 14), 
                new Color(0, 0, 102)),
            BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        
        // BARIS 1 - Pencarian dengan Text Field
        gbc.gridy = 0;
        
        // Label untuk text field search
        gbc.gridx = 0;
        JLabel lblSearch = new JLabel("Cari Bulan:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblSearch.setForeground(new Color(0, 0, 102));
        searchPanel.add(lblSearch, gbc);
        
        // Text field untuk input bulan manual
        gbc.gridx = 1;
        txtSearchBulan = new JTextField("Masukkan bulan (JAN, FEB, etc.)");
        txtSearchBulan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        txtSearchBulan.setPreferredSize(new Dimension(200, 30));
        txtSearchBulan.setForeground(Color.GRAY);
        
        // Tambahkan placeholder behavior
        txtSearchBulan.addFocusListener(new java.awt.event.FocusAdapter() {
            @Override
            public void focusGained(java.awt.event.FocusEvent evt) {
                if (txtSearchBulan.getText().equals("Masukkan bulan (JAN, FEB, etc.)")) {
                    txtSearchBulan.setText("");
                    txtSearchBulan.setForeground(Color.BLACK);
                }
            }
            
            @Override
            public void focusLost(java.awt.event.FocusEvent evt) {
                if (txtSearchBulan.getText().isEmpty()) {
                    txtSearchBulan.setText("Masukkan bulan (JAN, FEB, etc.)");
                    txtSearchBulan.setForeground(Color.GRAY);
                }
            }
        });
        
        // Tambahkan Enter key listener untuk search
        txtSearchBulan.addKeyListener(new java.awt.event.KeyAdapter() {
            @Override
            public void keyPressed(java.awt.event.KeyEvent evt) {
                if (evt.getKeyCode() == java.awt.event.KeyEvent.VK_ENTER) {
                    cariDataBulananManual();
                }
            }
        });
        
        searchPanel.add(txtSearchBulan, gbc);
        
        // Tombol cari manual
        gbc.gridx = 2;
        btnCariManual = new JButton("🔍 Cari");
        btnCariManual.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCariManual.setBackground(new Color(40, 167, 69));
        btnCariManual.setForeground(Color.WHITE);
        btnCariManual.setPreferredSize(new Dimension(80, 30));
        btnCariManual.setBorder(BorderFactory.createRaisedBevelBorder());
        btnCariManual.setFocusPainted(false);
        searchPanel.add(btnCariManual, gbc);
        
        // BARIS 2 - Separator
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JSeparator separator = new JSeparator();
        separator.setPreferredSize(new Dimension(400, 1));
        searchPanel.add(separator, gbc);
        
        // BARIS 3 - Atau label
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        JLabel lblOr = new JLabel("ATAU", JLabel.CENTER);
        lblOr.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblOr.setForeground(new Color(108, 117, 125));
        searchPanel.add(lblOr, gbc);
        
        // BARIS 4 - Pencarian dengan ComboBox
        gbc.gridy = 3;
        gbc.gridwidth = 1;
        
        // Label untuk ComboBox
        gbc.gridx = 0;
        JLabel lblBulan = new JLabel("Pilih Bulan:");
        lblBulan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        lblBulan.setForeground(new Color(0, 0, 102));
        searchPanel.add(lblBulan, gbc);
        
        // ComboBox untuk memilih bulan
        gbc.gridx = 1;
        String[] bulanOptions = {
            "Semua Bulan", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", 
            "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"
        };
        cbBulan = new JComboBox<>(bulanOptions);
        cbBulan.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        cbBulan.setBackground(Color.WHITE);
        cbBulan.setPreferredSize(new Dimension(200, 30));
        searchPanel.add(cbBulan, gbc);
        
        // Tombol cari ComboBox
        gbc.gridx = 2;
        btnCari = new JButton("🔍 Cari");
        btnCari.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnCari.setBackground(new Color(0, 123, 255));
        btnCari.setForeground(Color.WHITE);
        btnCari.setPreferredSize(new Dimension(80, 30));
        btnCari.setBorder(BorderFactory.createRaisedBevelBorder());
        btnCari.setFocusPainted(false);
        searchPanel.add(btnCari, gbc);
        
        // BARIS 5 - Tombol Reset
        gbc.gridy = 4;
        gbc.gridx = 0;
        gbc.gridwidth = 3;
        btnReset = new JButton("🔄 Reset Semua Filter");
        btnReset.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnReset.setBackground(new Color(108, 117, 125));
        btnReset.setForeground(Color.WHITE);
        btnReset.setPreferredSize(new Dimension(200, 30));
        btnReset.setBorder(BorderFactory.createRaisedBevelBorder());
        btnReset.setFocusPainted(false);
        searchPanel.add(btnReset, gbc);
        
        // Tambahkan event listener untuk tombol
        btnCariManual.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cariDataBulananManual();
            }
        });
        
        btnCari.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cariDataBulanan();
            }
        });
        
        btnReset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                resetFilter();
            }
        });
        
        // Ganti konten jScrollPane2
        jScrollPane2.setViewportView(searchPanel);
        jScrollPane2.revalidate();
        jScrollPane2.repaint();
    }
    
    private void cariDataBulanan() {
        String bulanDipilih = (String) cbBulan.getSelectedItem();
        
        if ("Semua Bulan".equals(bulanDipilih)) {
            // Tampilkan semua data
            loadDataFromDatabase();
            return;
        }
        
        // Kosongkan tabel
        model.setRowCount(0);
        
        try {
            // Koneksi ke MySQL (XAMPP)
            Connection cn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sikopet", "root", "");
            
            // Konversi nama bulan ke nomor bulan menggunakan helper method
            int nomorBulan = getMonthNumber(bulanDipilih);
            
            if (nomorBulan == -1) {
                JOptionPane.showMessageDialog(this, "Bulan tidak valid!");
                return;
            }
            
            // Query data berdasarkan bulan
            String sql = "SELECT * FROM data_warga WHERE MONTH(STR_TO_DATE(Tanggal, '%Y-%m-%d')) = ? ORDER BY Kategori, Tanggal";
            java.sql.PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, nomorBulan);
            ResultSet rs = ps.executeQuery();
            
            int totalPemasukan = 0;
            int totalPengeluaran = 0;
            boolean dataFound = false;
            
            // Isi data ke tabel dan hitung total
            while (rs.next()) {
                dataFound = true;
                
                // Konversi jumlah ke angka menggunakan helper method
                String jumlahStr = rs.getString("Jumlah");
                int jumlahInt = parseJumlah(jumlahStr);
                
                String kategori = rs.getString("Kategori");
                
                // Hitung total berdasarkan kategori
                if (kategori.equalsIgnoreCase("Pemasukan")) {
                    totalPemasukan += jumlahInt;
                } else if (kategori.equalsIgnoreCase("Pengeluaran")) {
                    totalPengeluaran += jumlahInt;
                }
                
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("Nama_Warga"),
                    rs.getString("NIK"),
                    rs.getString("No_HP"),
                    rs.getString("Tanggal"),
                    jumlahInt,
                    kategori
                };
                model.addRow(row);
            }
            
            if (!dataFound) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk bulan " + bulanDipilih + "!");
            }
            
            // Update labels ringkasan
            updateSummaryLabels(totalPemasukan, totalPengeluaran);
            
            rs.close();
            ps.close();
            cn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mencari data:\n" + e.getMessage());
        }
    }
    
    private void cariDataBulananManual() {
        String inputBulan = txtSearchBulan.getText().trim();
        
        // Validasi input - jika kosong atau masih placeholder
        if (inputBulan.isEmpty() || inputBulan.equals("Masukkan bulan (JAN, FEB, etc.)")) {
            JOptionPane.showMessageDialog(this, "Silakan masukkan nama bulan terlebih dahulu!\n" +
                "Contoh: JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC");
            return;
        }
        
        // Konversi input ke uppercase untuk konsistensi
        String bulanDicari = inputBulan.toUpperCase();
        
        // Validasi format bulan
        int nomorBulan = getMonthNumber(bulanDicari);
        if (nomorBulan == -1) {
            JOptionPane.showMessageDialog(this, "Format bulan tidak valid!\n" +
                "Gunakan format 3 huruf: JAN, FEB, MAR, APR, MAY, JUN, JUL, AUG, SEP, OCT, NOV, DEC");
            return;
        }
        
        // Kosongkan tabel
        model.setRowCount(0);
        
        try {
            // Koneksi ke MySQL (XAMPP)
            Connection cn = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306/sikopet", "root", "");
            
            // Query data berdasarkan bulan
            String sql = "SELECT * FROM data_warga WHERE MONTH(STR_TO_DATE(Tanggal, '%Y-%m-%d')) = ? ORDER BY Kategori, Tanggal";
            java.sql.PreparedStatement ps = cn.prepareStatement(sql);
            ps.setInt(1, nomorBulan);
            ResultSet rs = ps.executeQuery();
            
            int totalPemasukan = 0;
            int totalPengeluaran = 0;
            boolean dataFound = false;
            
            // Isi data ke tabel dan hitung total
            while (rs.next()) {
                dataFound = true;
                
                // Konversi jumlah ke angka menggunakan helper method
                String jumlahStr = rs.getString("Jumlah");
                int jumlahInt = parseJumlah(jumlahStr);
                
                String kategori = rs.getString("Kategori");
                
                // Hitung total berdasarkan kategori
                if (kategori.equalsIgnoreCase("Pemasukan")) {
                    totalPemasukan += jumlahInt;
                } else if (kategori.equalsIgnoreCase("Pengeluaran")) {
                    totalPengeluaran += jumlahInt;
                }
                
                Object[] row = {
                    rs.getInt("id"),
                    rs.getString("Nama_Warga"),
                    rs.getString("NIK"),
                    rs.getString("No_HP"),
                    rs.getString("Tanggal"),
                    jumlahInt,
                    kategori
                };
                model.addRow(row);
            }
            
            if (!dataFound) {
                JOptionPane.showMessageDialog(this, "Tidak ada data untuk bulan " + bulanDicari + "!\n" +
                    "Pastikan format bulan benar (JAN, FEB, dll) dan data tersedia.");
            } else {
                JOptionPane.showMessageDialog(this, "Data berhasil ditampilkan untuk bulan " + bulanDicari + "\n" +
                    "Total data ditemukan: " + model.getRowCount() + " record");
            }
            
            // Update labels ringkasan
            updateSummaryLabels(totalPemasukan, totalPengeluaran);
            
            rs.close();
            ps.close();
            cn.close();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Gagal mencari data:\n" + e.getMessage());
        }
    }
    
    private void resetFilter() {
        // Reset ComboBox ke "Semua Bulan"
        cbBulan.setSelectedIndex(0);
        
        // Reset text field ke placeholder
        txtSearchBulan.setText("Masukkan bulan (JAN, FEB, etc.)");
        txtSearchBulan.setForeground(Color.GRAY);
        
        // Muat ulang semua data
        loadDataFromDatabase();
        
        JOptionPane.showMessageDialog(this, "Filter telah direset. Menampilkan semua data.");
    }
    
    /**
     * Helper method untuk membersihkan dan mengkonversi string jumlah ke integer
     * @param jumlahStr String jumlah yang akan dikonversi
     * @return int nilai jumlah yang sudah dibersihkan
     */
    private int parseJumlah(String jumlahStr) {
        if (jumlahStr == null || jumlahStr.trim().isEmpty()) {
            return 0;
        }
        
        // Bersihkan format rupiah
        String cleanJumlah = jumlahStr.toUpperCase()
                .replace("RP. ", "")
                .replace("RP ", "")
                .replace("RP.", "")
                .replace(".", "")
                .replace(",", "")
                .replace(" ", "")
                .trim();
        
        try {
            return Integer.parseInt(cleanJumlah);
        } catch (NumberFormatException e) {
            System.err.println("Error parsing jumlah: " + jumlahStr + " -> " + cleanJumlah);
            return 0;
        }
    }
    
    /**
     * Helper method untuk mendapatkan nomor bulan dari nama bulan
     * @param namaBulan Nama bulan (JAN, FEB, dll)
     * @return int nomor bulan (1-12) atau -1 jika tidak valid
     */
    private int getMonthNumber(String namaBulan) {
        String[] months = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", 
                          "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};
        
        for (int i = 0; i < months.length; i++) {
            if (months[i].equals(namaBulan)) {
                return i + 1;
            }
        }
        return -1;
    }
    
    private void addMenuListeners() {
        // Event listener untuk menu Data Warga
        txtDatawarga.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtDatawargaActionPerformed(e);
            }
        });
        
        // Event listener untuk menu Keuangan
        txtKeuangan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtKeuanganActionPerformed(e);
            }
        });
        
        // Event listener untuk menu Laporan
        txtLaporan.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                txtLaporanActionPerformed(e);
            }
        });
    }


   
              
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblkeuangan = new javax.swing.JTable();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        menuopen = new javax.swing.JMenuItem();
        jMenuItem2 = new javax.swing.JMenuItem();
        jMenuItem3 = new javax.swing.JMenuItem();
        jMenu2 = new javax.swing.JMenu();
        txtDatawarga = new javax.swing.JMenuItem();
        txtKeuangan = new javax.swing.JMenuItem();
        txtLaporan = new javax.swing.JMenuItem();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SIKOPET - Laporan Keuangan");
        setPreferredSize(new java.awt.Dimension(800, 600));

        jPanel2.setBackground(new java.awt.Color(204, 204, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Laporan Keuangan SIKOPET", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 1, 20), new java.awt.Color(0, 0, 102))); // NOI18N

        tblkeuangan.setBackground(new java.awt.Color(153, 153, 255));
        tblkeuangan.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        tblkeuangan.setForeground(new java.awt.Color(255, 255, 255));
        tblkeuangan.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        tblkeuangan.setGridColor(new java.awt.Color(200, 200, 200));
        tblkeuangan.setRowHeight(25);
        tblkeuangan.setSelectionBackground(new java.awt.Color(200, 200, 255));
        jScrollPane1.setViewportView(tblkeuangan);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 605, Short.MAX_VALUE)
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(43, 43, 43)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jMenu1.setText("File");

        menuopen.setText("Buka File");
        jMenu1.add(menuopen);

        jMenuItem2.setText("Simpan");
        jMenu1.add(jMenuItem2);

        jMenuItem3.setText("Cetak");
        jMenu1.add(jMenuItem3);

        jMenuBar1.add(jMenu1);

        jMenu2.setText("Edit");

        txtDatawarga.setText("Data Warga");
        jMenu2.add(txtDatawarga);

        txtKeuangan.setText("Pencatatan Keuangan");
        jMenu2.add(txtKeuangan);

        txtLaporan.setText("Laporan");
        jMenu2.add(txtLaporan);

        jMenuBar1.add(jMenu2);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(JLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(JLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(JLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(JLaporan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new JLaporan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu2;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JMenuItem jMenuItem2;
    private javax.swing.JMenuItem jMenuItem3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JMenuItem menuopen;
    private javax.swing.JTable tblkeuangan;
    private javax.swing.JMenuItem txtDatawarga;
    private javax.swing.JMenuItem txtKeuangan;
    private javax.swing.JMenuItem txtLaporan;
    // End of variables declaration//GEN-END:variables

    private void txtDatawargaActionPerformed(java.awt.event.ActionEvent evt) {
        DataWarga newWin = new DataWarga();
        newWin.setVisible(true);
        this.dispose();
    }
    
    private void txtKeuanganActionPerformed(java.awt.event.ActionEvent evt) {
        JKeuangan newWin = new JKeuangan();
        newWin.setVisible(true);
        this.dispose();
    }
    
    private void txtLaporanActionPerformed(java.awt.event.ActionEvent evt) {
        JLaporan newWin = new JLaporan();
        newWin.setVisible(true);
        this.dispose();
    }
}
