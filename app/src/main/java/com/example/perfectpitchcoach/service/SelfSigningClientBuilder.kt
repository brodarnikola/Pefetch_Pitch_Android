package com.example.perfect_pitch_trainer.service

import android.content.Context;
import com.example.perfectpitchcoach.R

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;

import okhttp3.OkHttpClient;
import javax.net.ssl.*

class SelfSigningClientBuilder {

   fun createClient(context: Context): OkHttpClient? {

        var client: OkHttpClient? = null

        var cf: CertificateFactory? = null
        var cert: InputStream? = null
        var ca: Certificate? = null
        var sslContext: SSLContext? = null
        try {
            cf = CertificateFactory.getInstance("X.509")
            cert = context.getResources().openRawResource(R.raw.certificate_android) // Place your 'my_cert.crt' file in `res/raw`

            ca = cf!!.generateCertificate(cert)
            cert!!.close()

            val keyStoreType = KeyStore.getDefaultType()
            val keyStore = KeyStore.getInstance(keyStoreType)
            keyStore.load(null, null)
            keyStore.setCertificateEntry("ca", ca)

            val tmfAlgorithm = TrustManagerFactory.getDefaultAlgorithm()
            val tmf = TrustManagerFactory.getInstance(tmfAlgorithm)
            tmf.init(keyStore)

            sslContext = SSLContext.getInstance("TLS")
            sslContext!!.init(null, tmf.getTrustManagers(), null)

            val hostnameVerifier1 = HostnameVerifier { _, session ->
               return@HostnameVerifier true
            }

           // client?.hostnameVerifier()

           /* client.hostnameVerifier = HostnameVerifier { _, session ->
                HttpsURLConnection.getDefaultHostnameVerifier().run {
                    verify("aaaa", session)
                }
            } */

            client = OkHttpClient.Builder()
                 .hostnameVerifier(hostnameVerifier1)
                .sslSocketFactory(sslContext!!.getSocketFactory())
                .build()

        } catch (e: KeyStoreException) {
            e.printStackTrace()
        } catch (e: CertificateException) {
            e.printStackTrace()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: KeyManagementException) {
            e.printStackTrace()
        }

        return client
    }
}