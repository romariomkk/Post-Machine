/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;



public class PostWork extends javax.swing.JFrame {

    JTable algoTable = new JTable();
    JTable listTable = new JTable();
   
    public PostWork() throws IOException{
        initComponents();
        initMainComponents();
    }
    
    private void initMainComponents()throws IOException{

        initMainFrame();
        buttonRun();
        initHelp();
        createListTable();
        createAlgoTable();
    }
    
    private void initMainFrame(){
        setLocationRelativeTo(null);
        setTitle("Машина Поста");
        setResizable(false);
        
        JLabel head = new JLabel("МАШИНА ПОСТА ДЛЯ АЛГОРИТМУ N1 + N2 - N3 - N4 + N5 + N6");
        head.setFont(new Font("Courier New", Font.BOLD, 16));
        head.setBounds(this.getWidth()/3, 10, 600, 15);
        add(head);
    }
    
    private void buttonRun(){
        JButton runButton = new JButton("RUN");
        
        runButton.setFont(new Font("Courier New", Font.BOLD, 20));
        add(runButton);
        runButton.setBounds(800,90,100,100);
        runButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent evt){
                try {
                    runPostMachine();
                } catch (IOException ex) {
                    Logger.getLogger(PostWork.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
    });
    }
    
    private void initHelp(){
        String text = "A - ABORT\n\n"
                + "E - EXCHANGE\n\n"
                + "R - RIGHT MOVE\n\n"
                + "L - LEFT MOVE\n\n"
                + "S - SET SYMBOL\n\n"
                + "D - DELETE SYMBOL";
        JTextArea helpLabel = new JTextArea();
        helpLabel.append(text);
        helpLabel.setEditable(false);
        
        helpLabel.setFont(new Font("Courier New", Font.BOLD, 12));
        helpLabel.setBounds(800, 220, 170, 180);
        add(helpLabel);
    }
    
    private JTable createListTable(){
        DefaultTableModel listTableModel = new ListModel(1,60);//Таблица ЛЕНТА
        
        listTable.setModel(listTableModel);
        listTable.setRowHeight(30);
        listTable.setShowGrid(true);
        listTable.setRowSelectionAllowed(false);
        listTable.setBounds(10, 50, super.getWidth()-20, 31);//отступы и размеры
        add(listTable);
        //визуальное выделение отдельной клеточки
        listTable.setCellSelectionEnabled(true);
        listTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        //заполнение V при нажатии мышкой
        listTable.addMouseListener(new MouseAdapter(){
            @Override
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                int row = listTable.getSelectedRow();
                int col = listTable.getSelectedColumn();
                if (listTable.getValueAt(row, col) == null){
                    listTable.setValueAt("V", row, col);
                }else if (listTable.getValueAt(row, col) == "V"){
                    listTable.setValueAt(null, row, col);
                    }
            }
        }); 
        return listTable;
    }
    
    private JTable createAlgoTable() throws IOException{

        Vector<String> newRow = new Vector<>();
        Scanner scanner = new Scanner(new File("src\\main\\dataHead.txt"));
        while(scanner.hasNextLine()){//запишем строку заста
            newRow.add(scanner.nextLine());
        }
        
        DefaultTableModel algoTableModel = new MyModel(newRow,0);//таблица АЛГОРИТМ
        
        algoTable.setModel(algoTableModel);
        algoTable.setRowHeight(20);
        algoTable.setShowGrid(true);
        
        
        int colCount = algoTable.getColumnCount();//запрет resize
        for (int i=0; i < colCount; i++){
            algoTable.getColumnModel().getColumn(i).setResizable(false);
        }
        algoTable.getColumnModel().getColumn(0).setMaxWidth(60);//установка ширины столбцов
        algoTable.getColumnModel().getColumn(1).setMaxWidth(60);
        algoTable.getColumnModel().getColumn(2).setMaxWidth(80);
        algoTable.getColumnModel().getColumn(3).setMaxWidth(80);
        
        algoTable.getTableHeader().setReorderingAllowed(false);
        
        JScrollPane scr = new JScrollPane(algoTable);//помещаем таблицу в ScrollPane
        scr.setBounds(20, 90, 700, super.getHeight()-120);
        add(scr);
        algoTable.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        
        //считываем таблицу из файла
        Vector<String> newR;
        scanner = new Scanner(new File("src\\main\\testData.txt"));
        System.out.println(scanner.hasNext());
        int i = 0;
        while(scanner.hasNextLine()){
            newR = new Vector<>();
            newR.add(scanner.nextLine());
            newR.add(scanner.nextLine());
            newR.add(scanner.nextLine());
            newR.add(scanner.nextLine());
            newR.add(scanner.nextLine());
            
            algoTableModel.addRow(newR);
        }
        
        return algoTable;
    }
    
    private void runPostMachine() throws IOException{
        Thread thread = new Thread(new Runnable(){
            JTable list= listTable;
            JTable algo = algoTable;
                    
            int currNumber = 1;
            String currAction = null;
            int currListPos = 0;
            @Override
            public void run() {
                    
                    boolean flag = true;
                    while (flag){
                        currAction = algo.getValueAt(currNumber-1, 1).toString();
                        System.out.println(currAction);
                        switch(currAction){
                            case "D":
                                list.setValueAt(null, 0, currListPos);
                                currNumber = Integer.parseInt(algo.getValueAt(currNumber-1, 2).toString());
                                break;
                            case "R":
                                currListPos++;
                                currNumber = Integer.parseInt(algo.getValueAt(currNumber-1, 2).toString());
                                break;
                            case "L":
                                currListPos--;
                                currNumber = Integer.parseInt(algo.getValueAt(currNumber-1, 2).toString());
                                break;
                            case "S":
                                list.setValueAt("V", 0, currListPos);
                                currNumber = Integer.parseInt(algo.getValueAt(currNumber-1, 2).toString());
                                break;
                            case "E":
                                if (list.getValueAt(0, currListPos) == null){
                                    currNumber = Integer.parseInt(algo.getValueAt(currNumber-1, 2).toString());
                                }else /*if(list.getValueAt(0, currListPos).equals("V"))*/{
                                    currNumber = Integer.parseInt(algo.getValueAt(currNumber-1, 3).toString());
                                }
                                break;
                            case "A":
                                JOptionPane.showMessageDialog(algo, "Программа завершила работу!",
                                "Конец выполнения", JOptionPane.PLAIN_MESSAGE);
                                flag = false;
                                break;
                        }
                        updateListTable(currListPos);
                        if (!flag)
                            break;
                        try { 
                            Thread.sleep(100); 
                        } catch (InterruptedException e) { 
                            e.printStackTrace(); 
                        } 

                        
                    }
            } 
            private void updateListTable(int currListPos){
                TableModel mod = list.getModel();
                ((DefaultTableModel)mod).fireTableDataChanged();
                for (int i = 0; i < list.getColumnCount(); i++)
                    list.getColumnModel().getColumn(i).setCellRenderer(new DefaultRender());
                list.getColumnModel().getColumn(currListPos).setCellRenderer(new MyRender());
            }
//            private void updateAlgoTable(int currListPos){
//                TableModel mod = algo.getModel();
//                ((DefaultTableModel)mod).fireTableDataChanged();
//                for (int i = 0; i < algo.getRowCount(); i++)
//                    list.getColumnModel().getColumn(i).setCellRenderer(new DefaultRender());
//                algo.getr
//                list.getColumnModel().getColumn(currListPos).setCellRenderer(new MyRender());
//            }
        });
        thread.start();
        
        
    }
    
    public class ListModel extends DefaultTableModel{
        ListModel(int row, int column){
            super(row,column);
        }
        ListModel(Vector list, int row){
            super(list,row);
        }
        boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            };

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
    }
    
    public class MyModel extends DefaultTableModel{
        MyModel(int row, int column){
            super(row,column);
        }
        MyModel(Vector list, int row){
            super(list,row);
        }
        boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false, 
            false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false
            };

        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
    }
    
    
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1140, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 530, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    
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
            java.util.logging.Logger.getLogger(PostWork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(PostWork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(PostWork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(PostWork.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new PostWork().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(PostWork.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
