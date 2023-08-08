package kr.co.seulchuksaeng.seulchuksaengweb.security.impl;

import kr.co.seulchuksaeng.seulchuksaengweb.security.Crypto;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Component;

@Component
public class CryptoImpl implements Crypto {
    @Override
    public String getSalt() {
        return BCrypt.gensalt();
    }

    @Override
    public String saltPassword(String password, String salt) {
        return BCrypt.hashpw(password, salt);
    }
}
