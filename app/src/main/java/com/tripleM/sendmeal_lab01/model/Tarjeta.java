package com.tripleM.sendmeal_lab01.model;

import java.util.Date;

public class Tarjeta {
    private String numero;
    private String ccv;
    private Date vencimiento;
    private boolean esCredito;

    public Tarjeta(String numero, String ccv, Date vencimiento, boolean esCredito) {
        this.numero = numero;
        this.ccv = ccv;
        this.vencimiento = vencimiento;
        this.esCredito = esCredito;
    }

    public String getNumero() {
        return numero;
    }

    public String getCcv() {
        return ccv;
    }

    public Date getVencimiento() {
        return vencimiento;
    }

    public boolean isEsCredito() {
        return esCredito;
    }

    @Override
    public String toString() {
        return "Tarjeta{" +
                "numero='" + numero + '\'' +
                ", ccv='" + ccv + '\'' +
                ", vencimiento=" + vencimiento +
                ", esCredito=" + esCredito +
                '}';
    }
}
