package kr.co.seulchuksaeng.seulchuksaengweb.security;

public interface Crypto {

     //비밀번호 Salt 생성
     String getSalt();

     //비밀번호 Salt 처리
     String saltPassword(String password, String salt);

}
