package banking;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * The Bank implementation.
 */
public class Bank implements BankInterface {
    private LinkedHashMap<Long, Account> accounts;
    private Long accountCounter; // New counter for sequential account numbers

    public Bank() {
        this.accounts = new LinkedHashMap<Long, Account>();
        this.accountCounter = 1L; // Initialize the counter to 1
    }

    private Account getAccount(Long accountNumber) {
        return accounts.get(accountNumber);
    }

    public Long openCommercialAccount(Company company, int pin, double startingDeposit) {
        Long accountNumber = generateAccountNumber();
        CommercialAccount account = new CommercialAccount(company, accountNumber, pin, startingDeposit);
        accounts.put(accountNumber, account);
        return accountNumber;
    }

    public Long openConsumerAccount(Person person, int pin, double startingDeposit) {
        Long accountNumber = generateAccountNumber();
        ConsumerAccount account = new ConsumerAccount(person, accountNumber, pin, startingDeposit);
        accounts.put(accountNumber, account);
        return accountNumber;
    }

    public double getBalance(Long accountNumber) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            return account.getBalance();
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    public void credit(Long accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            account.creditAccount(amount);
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    public boolean debit(Long accountNumber, double amount) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            return account.debitAccount(amount);
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    public boolean authenticateUser(Long accountNumber, int pin) {
        Account account = getAccount(accountNumber);
        if (account != null) {
            return account.validatePin(pin);
        } else {
            throw new RuntimeException("Account not found");
        }
    }

    public void addAuthorizedUser(Long accountNumber, Person authorizedPerson) {
        Account account = getAccount(accountNumber);
        if (account instanceof CommercialAccount) {
            CommercialAccount commercialAccount = (CommercialAccount) account;
            commercialAccount.addAuthorizedUser(authorizedPerson);
        } else {
            throw new RuntimeException("Cannot add authorized user to a non-commercial account");
        }
    }

    public boolean checkAuthorizedUser(Long accountNumber, Person authorizedPerson) {
        Account account = getAccount(accountNumber);
        if (account instanceof CommercialAccount) {
            CommercialAccount commercialAccount = (CommercialAccount) account;
            return commercialAccount.isAuthorizedUser(authorizedPerson);
        } else {
            throw new RuntimeException("Cannot check authorized user for a non-commercial account");
        }
    }

    public Map<String, Double> getAverageBalanceReport() {
        Map<String, Double> averageBalances = new HashMap<String, Double>();
        int commercialCount = 0;
        int consumerCount = 0;
        double commercialTotal = 0;
        double consumerTotal = 0;

        for (Account account : accounts.values()) {
            if (account instanceof CommercialAccount) {
                commercialTotal += account.getBalance();
                commercialCount++;
            } else if (account instanceof ConsumerAccount) {
                consumerTotal += account.getBalance();
                consumerCount++;
            }
        }

        if (commercialCount > 0) {
            double commercialAverage = commercialTotal / commercialCount;
            averageBalances.put("CommercialAccount", commercialAverage);
        }

        if (consumerCount > 0) {
            double consumerAverage = consumerTotal / consumerCount;
            averageBalances.put("ConsumerAccount", consumerAverage);
        }

        return averageBalances;
    }

    private Long generateAccountNumber() {
        Long accountNumber = accountCounter;
        accountCounter++; // Increment the counter for the next account
        return accountNumber;
    }
}