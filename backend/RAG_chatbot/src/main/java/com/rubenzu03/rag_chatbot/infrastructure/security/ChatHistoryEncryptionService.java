package com.rubenzu03.rag_chatbot.infrastructure.security;

import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

@Service
public class ChatHistoryEncryptionService {

    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final String ALGORITHM = "AES";
    private static final int IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH_BITS = 128;

    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final ConversationEncryptionKeyService conversationEncryptionKeyService;

    public ChatHistoryEncryptionService(ConversationEncryptionKeyService conversationEncryptionKeyService) {
        this.conversationEncryptionKeyService = conversationEncryptionKeyService;
    }

    public String encrypt(String data, String conversationId) {
        try {
            return getString(data, conversationEncryptionKeyService.getOrCreateKey(conversationId));
        } catch (Exception e) {
            throw new RuntimeException("Error encrypting chat history", e);
        }
    }

    static String getString(String data, byte[] key) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        byte[] iv = new byte[ChatHistoryEncryptionService.IV_LENGTH];
        ChatHistoryEncryptionService.SECURE_RANDOM.nextBytes(iv);

        Cipher cipher = Cipher.getInstance(ChatHistoryEncryptionService.TRANSFORMATION);
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, ChatHistoryEncryptionService.ALGORITHM), new GCMParameterSpec(ChatHistoryEncryptionService.GCM_TAG_LENGTH_BITS, iv));
        byte[] encryptedBytes = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));

        ByteBuffer payload = ByteBuffer.allocate(iv.length + encryptedBytes.length);
        payload.put(iv);
        payload.put(encryptedBytes);
        return Base64.getEncoder().encodeToString(payload.array());
    }

    public String decrypt(String encryptedData, String conversationId) {
        try {
            byte[] key = conversationEncryptionKeyService.getOrCreateKey(conversationId);
            byte[] payload = Base64.getDecoder().decode(encryptedData);

            if (payload.length <= IV_LENGTH) {
                return encryptedData;
            }

            return getString(key, payload);
        } catch (Exception e) {
            throw new RuntimeException("Chat history decryption failed. Data integrity may be compromised.", e);
        }
    }

    @NonNull
    static String getString(byte[] key, byte[] payload) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {
        ByteBuffer byteBuffer = ByteBuffer.wrap(payload);
        byte[] iv = new byte[ChatHistoryEncryptionService.IV_LENGTH];
        byteBuffer.get(iv);

        byte[] cipherText = new byte[byteBuffer.remaining()];
        byteBuffer.get(cipherText);

        Cipher cipher = Cipher.getInstance(ChatHistoryEncryptionService.TRANSFORMATION);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, ChatHistoryEncryptionService.ALGORITHM), new GCMParameterSpec(ChatHistoryEncryptionService.GCM_TAG_LENGTH_BITS, iv));
        return new String(cipher.doFinal(cipherText), StandardCharsets.UTF_8);
    }
}
