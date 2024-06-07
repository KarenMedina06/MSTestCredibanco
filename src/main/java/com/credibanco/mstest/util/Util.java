package com.credibanco.mstest.util;

import java.security.SecureRandom;
import java.util.Calendar;

import org.springframework.stereotype.Service;

@Service
public class Util {
	
	public static final int LONGITUD_TARJETA = 16;
	public static final int LONGITUD_ID_PRODUCTO = 6;
	public static final Long MIN = (long) 100000;
	public static final Long MAX = (long) 999999;
    
    private SecureRandom random = new SecureRandom();
    
    public Long generateCardNumber(String idProducto) {
        StringBuilder number = new StringBuilder(idProducto);
        for (int i = 0; i < LONGITUD_TARJETA - LONGITUD_ID_PRODUCTO; i++) {
            int digito = random.nextInt(10);
            number.append(digito);
        }
        return Long.parseLong(number.toString());
    }

    public String calculateExpirationDate() {
        Calendar actualDate = Calendar.getInstance();
        actualDate.add(Calendar.YEAR, 3);  
        int month = actualDate.get(Calendar.MONTH) + 1;  
        int year = actualDate.get(Calendar.YEAR);
        return String.format("%02d/%d", month, year);
    }
    
    public Long generateTransactionId() {
    	return random.nextLong((MAX - MIN) + 1) + MIN;
    }

}
