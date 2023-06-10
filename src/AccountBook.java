import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.text.*;
import java.util.*;

public class AccountBook extends JFrame {
    private JLabel dateLabel, itemLabel, amountLabel, noteLabel;
    private JTextField dateField, itemField, amountField, noteField;
    private JButton addButton, removeButton, editButton, reportButton, exitButton;
    private JTextArea recordArea;
    private JScrollPane scrollPane;
    private JMenuBar menuBar;
    private JMenu fileMenu;
    private JMenuItem openItem, saveItem, exitItem;

    private ArrayList<Account> accountList = new ArrayList<>();

//    绘制记账本洁面
    public AccountBook() {
        setTitle("Account Book");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new GridLayout(4, 2));

//        添加元素
        dateLabel = new JLabel("Date:");
        inputPanel.add(dateLabel);
        dateField = new JTextField();
        inputPanel.add(dateField);

        itemLabel = new JLabel("Item:");
        inputPanel.add(itemLabel);
        itemField = new JTextField();
        inputPanel.add(itemField);

        amountLabel = new JLabel("Amount:");
        inputPanel.add(amountLabel);
        amountField = new JTextField();
        inputPanel.add(amountField);

//        添加功能
        noteLabel = new JLabel("Note:");
        inputPanel.add(noteLabel);
        noteField = new JTextField();
        inputPanel.add(noteField);

        add(inputPanel, BorderLayout.NORTH);

        addButton = new JButton("Add");
        addButton.addActionListener(new AddButtonListener());
        removeButton = new JButton("Remove");
        removeButton.addActionListener(new RemoveButtonListener());
        editButton = new JButton("Edit");
        editButton.addActionListener(new EditButtonListener());
        reportButton = new JButton("Report");
        reportButton.addActionListener(new ReportButtonListener());
        exitButton = new JButton("Exit");
        exitButton.addActionListener(new ExitButtonListener());

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new GridLayout(1, 5));
        buttonPanel.add(addButton);
        buttonPanel.add(removeButton);
        buttonPanel.add(editButton);
        buttonPanel.add(reportButton);
        buttonPanel.add(exitButton);

        add(buttonPanel, BorderLayout.CENTER);

        recordArea = new JTextArea();
        scrollPane = new JScrollPane(recordArea);
        add(scrollPane, BorderLayout.SOUTH);

        menuBar = new JMenuBar();
        fileMenu = new JMenu("File");

        openItem = new JMenuItem("Open");
        openItem.addActionListener(new OpenMenuItemListener());
        saveItem = new JMenuItem("Save");
        saveItem.addActionListener(new SaveMenuItemListener());
        exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(new ExitMenuItemListener());

        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.add(exitItem);

        menuBar.add(fileMenu);
        setJMenuBar(menuBar);

        setVisible(true);
    }

//    添加数据类型
    private class Account {
        private Date date;
        private String item;
        private double amount;
        private String note;

        public Account(Date date, String item, double amount, String note) {
            this.date = date;
            this.item = item;
            this.amount = amount;
            this.note = note;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public void setItem(String item) {
            this.item = item;
        }

        public void setAmount(double amount) {
            this.amount = amount;
        }

        public void setNote(String note) {
            this.note = note;
        }

//        声明添加数据方法
        public Date getDate() {
            return date;
        }

        public String getItem() {
            return item;
        }

        public double getAmount() {
            return amount;
        }

        public String getNote() {
            return note;
        }
    }

//    添加数据接口，实现数据添加方法
    private class AddButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String dateString = dateField.getText().trim();
            String itemString = itemField.getText().trim();
            String amountString = amountField.getText().trim();
            String noteString = noteField.getText().trim();

//            数据验证  防止  null
            if (dateString.equals("") || itemString.equals("") || amountString.equals("")) {
                JOptionPane.showMessageDialog(null, "Please input valid data!");
                return;
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date date;

            try {
                date = dateFormat.parse(dateString);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Please input date in the format of 'yyyy/MM/dd'!");
                return;
            }

            double amount;

//            数据验证  防止  null
            try {
                amount = Double.parseDouble(amountString);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "Please input valid amount!");
                return;
            }

            accountList.add(new Account(date, itemString, amount, noteString));
            recordArea.append(dateString + " | " + itemString + " | " + amountString + " | " + noteString + "\n");
            clearFields();
        }
    }

//    实现更改功能
    private class RemoveButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int index = recordArea.getCaretPosition();

            if (index < 0 || index >= accountList.size()) {
                JOptionPane.showMessageDialog(null, "Please select a record to remove!");
                return;
            }

            accountList.remove(index);
            recordArea.setText("");

            for (Account account : accountList) {
                Date date = account.getDate();
                String dateString = new SimpleDateFormat("yyyy/MM/dd").format(date);
                String itemString = account.getItem();
                double amount = account.getAmount();
                String amountString = String.format("%.2f", amount);
                String noteString = account.getNote();

                recordArea.append(dateString + " | " + itemString + " | " + amountString + " | " + noteString + "\n");
            }
        }
    }

//    实现删除功能
    private class EditButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            int index = recordArea.getCaretPosition();

            if (index < 0 || index >= accountList.size()) {
                JOptionPane.showMessageDialog(null, "Please select a record to edit!");
                return;
            }

            Account account = accountList.get(index);

            String dateString = dateField.getText().trim();
            String itemString = itemField.getText().trim();
            String amountString = amountField.getText().trim();
            String noteString = noteField.getText().trim();

            if (!dateString.equals("")) {
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                Date date;

                try {
                    date = dateFormat.parse(dateString);
                    account.setDate(date);
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Please input date in the format of 'yyyy/MM/dd'!");
                    return;
                }
            }

            if (!itemString.equals("")) {
                account.setItem(itemString);
            }

            if (!amountString.equals("")) {
                double amount;

                try {
                    amount = Double.parseDouble(amountString);
                    account.setAmount(amount);
                } catch (NumberFormatException e) {
                    JOptionPane.showMessageDialog(null, "Please input valid amount!");
                    return;
                }
            }

            if (!noteString.equals("")) {
                account.setNote(noteString);
            }

            recordArea.setText("");

            for (Account a : accountList) {
                Date date = a.getDate();
                String dateStringNew = new SimpleDateFormat("yyyy/MM/dd").format(date);
                String itemStringNew = a.getItem();
                double amount = a.getAmount();
                String amountStringNew = String.format("%.2f", amount);
                String noteStringNew = a.getNote();

                recordArea.append(dateStringNew + " | " + itemStringNew + " | " + amountStringNew + " | " + noteStringNew + "\n");
            }

            clearFields();
        }
    }

//    实现汇总功能
    private class ReportButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            String startDateString = JOptionPane.showInputDialog("Please input start date (yyyy/MM/dd):");
            String endDateString = JOptionPane.showInputDialog("Please input end date (yyyy/MM/dd):");

            if (startDateString == null || endDateString == null) {
                return;
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
            Date startDate, endDate;

            try {
                startDate = dateFormat.parse(startDateString);
                endDate = dateFormat.parse(endDateString);
            } catch (ParseException e) {
                JOptionPane.showMessageDialog(null, "Please input date in the format of 'yyyy/MM/dd'!");
                return;
            }

            double totalIncome = 0, totalExpense = 0;

            for (Account account : accountList) {
                Date date = account.getDate();

                if (date.compareTo(startDate) >= 0 && date.compareTo(endDate) <= 0) {
                    double amount = account.getAmount();

                    if (amount > 0) {
                        totalIncome += amount;
                    } else {
                        totalExpense -= amount;
                    }
                }
            }

            double netIncome = totalIncome - totalExpense;

            JOptionPane.showMessageDialog(null,"Report from " + startDateString + " to " + endDateString + "\n" +
                    "Total Income: " + String.format("%.2f", totalIncome) + "\n" +
                    "Total Expense: " + String.format("%.2f", totalExpense) + "\n" +
                    "Net Income: " + String.format("%.2f", netIncome));
        }
    }

//    实现退出功能
    private class ExitButtonListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

//    实现查看功能
    private class OpenMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try {
                    BufferedReader reader = new BufferedReader(new FileReader(file));
                    String line;

                    while ((line = reader.readLine()) != null) {
                        String[] splitLine = line.split("\\|");
                        Date date = new SimpleDateFormat("yyyy/MM/dd").parse(splitLine[0].trim());
                        String item = splitLine[1].trim();
                        double amount = Double.parseDouble(splitLine[2].trim());
                        String note = splitLine[3].trim();
                        accountList.add(new Account(date, item, amount, note));
                    }

                    reader.close();

                    recordArea.setText("");

                    for (Account account : accountList) {
                        Date date = account.getDate();
                        String dateString = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        String itemString = account.getItem();
                        double amount = account.getAmount();
                        String amountString = String.format("%.2f", amount);
                        String noteString = account.getNote();

                        recordArea.append(dateString + " | " + itemString + " | " + amountString + " | " + noteString + "\n");
                    }

                    clearFields();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Failed to open file!");
                } catch (ParseException e) {
                    JOptionPane.showMessageDialog(null, "Failed to parse date from file!");
                }
            }
        }
    }

//    实现保存功能
    private class SaveMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            JFileChooser fileChooser = new JFileChooser();

            if (fileChooser.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile();

                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(file));

                    for (Account account : accountList) {
                        Date date = account.getDate();
                        String dateString = new SimpleDateFormat("yyyy/MM/dd").format(date);
                        String itemString = account.getItem();
                        double amount = account.getAmount();
                        String amountString = String.format("%.2f", amount);
                        String noteString = account.getNote();

                        writer.write(dateString + " | " + itemString + " | " + amountString + " | " + noteString + "\n");
                    }

                    writer.close();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Failed to save file!");
                }
            }
        }
    }

    private class ExitMenuItemListener implements ActionListener {
        public void actionPerformed(ActionEvent event) {
            System.exit(0);
        }
    }

//    数据清空
    private void clearFields() {
        dateField.setText("");
        itemField.setText("");
        amountField.setText("");
        noteField.setText("");
    }

    public static void main(String[] args) {
        new AccountBook();
    }
}