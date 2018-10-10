package kr.co.digitalanchor.pangchat.utils;

import com.orhanobut.logger.Logger;

/**
 * Created by Xian on 2015-06-15.
 */
public class StringValidator {

    //휴대폰 전화번호 - 없이
    public static boolean isPhoneNumber(String phoneNumber) {

        boolean result;

        try {

            String reg = "^01(?:0|1[6-9])(?:\\d{3}|\\d{4})\\d{4}$";

            result = phoneNumber.matches(reg);

        } catch (Exception e) {

            result = false;

        }

        return result;
    }

    public static boolean isNumber(String number) {

        boolean result;

        try {

            String reg = "^[0-9]*$";

            result = number.matches(reg);

        } catch (Exception e) {

            result = false;

        }

        return result;
    }

    //-없이
    public static boolean isJumin(String jumin) {

        boolean result;

        try {

            String reg = "\\d{6}[1234]\\d{6}";

            result = jumin.matches(reg);

            Logger.i("isJumin : " + result);

            if(result){

                String birthYear = (2 >= Integer.valueOf(jumin.substring(6, 7) ).intValue() ? "19" : "20") ;
                birthYear += jumin.substring(0, 2);
                Logger.i("birthYear : " + birthYear);
                String birthMonth = jumin.substring(2, 4);
                Logger.i("birthMonth : " + birthMonth);
                String birthDate = jumin.substring(4, 6);
                Logger.i("birthDate : " + birthDate);

                int bYear = Integer.parseInt(birthYear);
                int bMonth = Integer.parseInt(birthMonth);
                int bDate = Integer.parseInt(birthDate);

                if(bYear < 1900 || bYear > 2100 || bMonth < 1|| bMonth > 12 || bDate < 1 || bDate > 31){
                    Logger.i("is false year");
                    return false;
                }

                int sum=0;
                // 1. 각각의 자리에 2 3 4 5 6 7 8 9 2 3 4 5를 곱한 합을 구한다.
                for(int i=0;i<jumin.length()-1;i++){
                    // char형을 숫자로 바꾸는 방법 : '1' - '0' = 1, '5'-'0' = 5
                    // 숫자를 char형으로 바꾸는 방법 : 1 + '0' = '1' , 7 + '0' = '7'
                    sum += (jumin.charAt(i)-'0') * (i<8 ? i+2 : i-6);
                }

                // 2. 합을 11로 나눈 나머지를 구한다.
                // 3. 11에서 나머지를 뺀다.
                // 4. 위의결과를 1으로 나눈 나머지를 구한다.
                sum = (11 - sum%11)%10;

                if(jumin.charAt(12)-'0' == sum) {
                    Logger.i("맞는 주민번호입니다.");
                    return true;
                } else {
                    Logger.i("틀린 주민번호입니다.");
                    return false;
                }
            }else {
                return false;
            }

        } catch (Exception e) {

            result = false;

        }

        return result;
    }

}
