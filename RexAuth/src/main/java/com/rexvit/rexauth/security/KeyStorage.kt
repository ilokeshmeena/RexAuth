package com.rexvit.rexauth.security

import android.content.Context
import android.util.Base64
import javax.crypto.Cipher

class KeyStorage(context: Context) {

    private val cryptoManager = CryptoManager(context)
    private val sharedPreferences = cryptoManager.getEncryptedSharedPreferences(
        "encrypted_keys",
        context
    )

    fun encryptAndStoreData(key: String, data: String, cipher: Cipher? = null): Boolean {
        return try {
            val encryptedBytes = if (cipher != null) {
                cipher.doFinal(data.toByteArray())
            } else {
                cryptoManager.getInitializedCipherForEncryption(key).doFinal(data.toByteArray())
            }
            val encryptedString = Base64.encodeToString(encryptedBytes, Base64.DEFAULT)
            sharedPreferences.edit().putString(key, encryptedString).apply()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun decryptData(key: String, cipher: Cipher? = null): String? {
        return try {
            val encryptedString = sharedPreferences.getString(key, null) ?: return null
            val encryptedBytes = Base64.decode(encryptedString, Base64.DEFAULT)

            val decryptedBytes = if (cipher != null) {
                cipher.doFinal(encryptedBytes)
            } else {
                cryptoManager.getInitializedCipherForDecryption(key).doFinal(encryptedBytes)
            }
            String(decryptedBytes)
        } catch (e: Exception) {
            null
        }
    }

    fun containsKey(key: String): Boolean {
        return sharedPreferences.contains(key)
    }

    fun removeKey(key: String) {
        sharedPreferences.edit().remove(key).apply()
    }
}