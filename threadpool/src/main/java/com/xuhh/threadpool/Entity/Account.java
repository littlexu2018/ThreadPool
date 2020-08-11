package com.xuhh.threadpool.Entity;

public class Account {

    double money;

    public Account(){

    }

    public synchronized void addMoney(double a){
        money=a+this.money;
        System.out.println("before = " + Thread.currentThread().getName()+": "+money);

        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after = " + Thread.currentThread().getName()+": "+money);
        System.out.println("===================================");

    }

    public double getMoney(){
        return money;
    }

}
