# create public keystore using cert
keytool -import -alias trustServer -file trustServer.cer -keystore publicKey.jks
