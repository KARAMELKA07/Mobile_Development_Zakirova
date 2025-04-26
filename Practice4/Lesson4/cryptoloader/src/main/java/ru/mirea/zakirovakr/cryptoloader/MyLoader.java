package ru.mirea.zakirovakr.cryptoloader;

import android.content.Context;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.loader.content.AsyncTaskLoader;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MyLoader extends AsyncTaskLoader<String> {
    public static final String ARG_WORD = "word";
    private byte[] encryptedText;
    private byte[] key;

    public MyLoader(@NonNull Context context, Bundle args) {
        super(context);
        if (args != null) {
            encryptedText = args.getByteArray(ARG_WORD);
            key = args.getByteArray("key");
        }
    }

    @Override
    protected void onStartLoading() {
        super.onStartLoading();
        forceLoad(); // Запускает загрузку
    }

    @Override
    public String loadInBackground() {
        if (encryptedText == null || key == null) {
            return "Ошибка: данные или ключ отсутствуют";
        }

        try {
            // Восстановление ключа
            SecretKey originalKey = new SecretKeySpec(key, 0, key.length, "AES");
            // Дешифрование текста
            return decryptMsg(encryptedText, originalKey);
        } catch (Exception e) {
            return "Ошибка дешифрования: " + e.getMessage();
        }
    }

    // Метод дешифрования
    private String decryptMsg(byte[] cipherText, SecretKey secret) {
        try {
            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secret);
            return new String(cipher.doFinal(cipherText));
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException
                 | BadPaddingException | InvalidKeyException e) {
            throw new RuntimeException(e);
        }
    }
}