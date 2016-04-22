package ui;

import domain.Employee;
import io.FileOperator;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static javax.swing.JFileChooser.APPROVE_OPTION;

public class MainFrame extends JFrame {
    public final static int COLUMN_COUNT = 10;

    private JButton importExcelFileButton;
    private JPanel contentPane;
    private JTable table;
    private java.util.List<Employee> employeeList = new ArrayList<Employee>();
    private java.util.List<String> headerList = new ArrayList<String>();
    private JButton exportButton;
    private String targetFilePath = "/Users/zhangruojia/Documents/code/web_project/scheduling/trunk/doc/3-design/temp.txt";

    public static void main(String[] args) {
        new MainFrame();

    }

    public MainFrame() {
        initUi();
        initListener();
        headerList.add("序号");
        headerList.add("工号");
    }

    private void initUi() {
        setSize(300, 200);
        setPreferredSize(new Dimension(300, 200));
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setVisible(true);

        contentPane = new JPanel();
        this.setContentPane(contentPane);
        contentPane.setLayout(new BorderLayout());
        initToolbar();
        initTable();
    }

    private void initToolbar() {
        importExcelFileButton = new JButton();
        importExcelFileButton.setText("导入excel文件");

        JToolBar toolBar = new JToolBar();
        // toolBar.setBackground(new Color());
        toolBar.setPreferredSize(new Dimension(0, 50));
        toolBar.add(importExcelFileButton);
        exportButton = new JButton();

        toolBar.add(exportButton);
        contentPane.add(toolBar, BorderLayout.NORTH);
    }

    private void initTable() {
        table = new JTable();
        contentPane.add(table, BorderLayout.CENTER);
        table.getTableHeader().setVisible(true);
        table.setModel(new DefaultTableModel() {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }

            @Override
            public int getColumnCount() {
                return headerList.size();
            }

            @Override
            public Object getValueAt(int row, int column) {
                return super.getValueAt(row, column);
            }

            @Override
            public int getRowCount() {
                return employeeList.size();
            }

            @Override
            public String getColumnName(int column) {
                return headerList.get(column);
            }
        });
    }

    private void initListener() {
        importExcelFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileChooserAndImportDataFromFile();
            }
        });

        exportButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser fileChooser = new JFileChooser("/Users/zhangruojia/Documents/code/web_project/scheduling/trunk/doc/3-design/");
                int operationType = fileChooser.showSaveDialog(MainFrame.this);
                if (operationType == APPROVE_OPTION) {
                    try {
                        FileOutputStream fileOutputStream = new FileOutputStream(
                                fileChooser.getSelectedFile());
                        FileWriter fileWriter = new FileWriter(fileChooser
                                .getSelectedFile());
                        fileWriter.write("insert into ");
                    } catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }

            }
        });
    }

    private void showFileChooserAndImportDataFromFile() {
        JFileChooser fileChooser = new JFileChooser("e://");
        fileChooser.setDialogTitle("选择excel文件");
        // fileChooser.setFileFilter(new FileFilter() {
        // @Override
        // public boolean accept(File f) {
        // return f.isFile() && f.getName().endsWith(".cvs");
        // }
        //
        // @Override
        // public String getDescription() {
        // return null;
        // }
        // });

        int operationType = fileChooser.showOpenDialog(MainFrame.this);
        if (operationType == APPROVE_OPTION) {
            File selectedFile = fileChooser.getSelectedFile();
            List<Employee> employees = readFile(selectedFile);
            FileOperator.writeFile(new File(targetFilePath), employees);
            for (Employee employee : employees) {
                System.out.println(employee.toSql());
            }
            JOptionPane.showMessageDialog(null, "save success =" + employees.size());
            showData(employees);
        }
    }

    private java.util.List<Employee> readFile(File file) {
        return FileOperator.readExcelFile(file);
    }

    private void showData(java.util.List<Employee> employeeList) {
        this.employeeList = employeeList;
        ((DefaultTableModel) this.table.getModel()).fireTableDataChanged();
    }
}
