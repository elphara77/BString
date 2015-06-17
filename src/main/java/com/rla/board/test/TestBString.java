package com.rla.board.test;

import com.rla.board.main.BString;

public class TestBString {

    public static void main(String[] args) {

        BString bs = BString.newInstance();
        System.out.println(bs.toString());

        BString bs2 = BString.newInstance("bonjour Monsieur Dupond :-) comment allez-vous aujourd'hui ?!");
        System.out.println(bs2.toString());

        BString bs3 = BString.newInstance();
        bs3.add("bonjour Monsieur Dupont :-) comment allez-vous aujourd'hui ?!");
        System.out.println(bs3.toString());
    }

}
