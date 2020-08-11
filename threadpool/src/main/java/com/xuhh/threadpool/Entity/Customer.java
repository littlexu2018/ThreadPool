package com.xuhh.threadpool.Entity;

public class Customer implements Runnable{

    private Account account;

    public Customer(Account account){
        this.account=account;
    }


    @Override
    public void run() {
        for (int i = 0; i < 3; i++) {
            account.addMoney(1000);
        }
    }

    public static void main(String[] args) {
        Account account =new Account();

        Customer customer1 =new Customer(account);
        Customer customer2 =new Customer(account);
        Thread t1 =new Thread(customer1);
        Thread t2 =new Thread(customer2);
        t1.setName("甲");
        t2.setName("乙");
        t1.start();
        t2.start();
    }
}
