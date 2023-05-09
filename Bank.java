import java.sql.*;

public class Bank {
    private String name;

    public Bank(String name) {
        this.name = name;
    }
    public void listAccount(){
        Connection con = BankConnection.connect();
        try {
            Statement statement = con.createStatement();
            String sql = "select * from account";
            ResultSet results = statement.executeQuery(sql);

            while (results.next()) {
                System.out.println(results.getString(1)+" "+results.getString(2)+ " "
                +results.getString(3));
            }
            System.out.println();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void openAccount(Account account) {
        Connection con = BankConnection.connect();
        String sql = "insert into account(accountID,accountName,accountBalance) values(?,?,?)";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,account.getNumber());
            preparedStatement.setString(2,account.getName());
            preparedStatement.setDouble(3,account.getBalance());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void closeAccount(int number) {
        Connection con = BankConnection.connect();
        String sql = "delete from account where accountID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1,number);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }
    public void depositMoney(Account account, double amount) {
        account.deposit(amount);
        System.out.println(account.getBalance());
        Connection con = BankConnection.connect();
        String sql = "UPDATE account set accountBalance = ? where accountID = ?";
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setDouble(1, account.getBalance());
            preparedStatement.setInt(2, account.getNumber());
            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void withdrawMoney(Account account, double amount) {
        account.withdraw(amount);
    }

    /*public Account getAccount(int number) {
        Connection con = BankConnection.connect();
        String sql = "select * from account";
        Account account=null;
        Statement statement = null;
        try {
            statement = con.createStatement();
            ResultSet results = statement.executeQuery(sql);
            String accountName = results.getString(2);
            double balance = results.getDouble(3);
            account = new Account(number, accountName, balance);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }


        return account;
    }*/

    public Account getAccount(int number) {
        Connection con = BankConnection.connect();
        String sql = "select * from account where accountID=?";
        Account account = null;
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, number);
            ResultSet result = preparedStatement.executeQuery();

            if (result.next()) {
                String accountName = result.getString(2);
                double balance = result.getDouble(3);
                account = new Account(number, accountName, balance);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return account;
    }

}
