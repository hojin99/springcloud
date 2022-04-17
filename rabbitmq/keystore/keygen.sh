# create private key
keytool -genkeypair -alias apiEncryptionKey -keyalg RSA \
-dname "CN=hojin cho, OU=personal, O=personal, L=Seoul, C=KR" \
-keypass "1234abcd" -keystore apiEncryptionKey.jks -storepass "1234abcd"