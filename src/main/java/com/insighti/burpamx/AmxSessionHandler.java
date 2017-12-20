package com.insighti.burpamx;

import burp.BurpExtender;
import burp.IHttpRequestResponse;
import burp.IRequestInfo;
import burp.ISessionHandlingAction;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AmxSessionHandler implements ISessionHandlingAction {

    public String getActionName() {
        return BurpExtender.EXTENSION_NAME;
    }

    private void debugln(String x) {
        if (BurpExtender.DEBUG) {
            BurpExtender.stdout.println(x);
        }
    }

    public void performAction(IHttpRequestResponse currentRequest, IHttpRequestResponse[] macroItems) {
        // fetch the configuration
        String _appId = BurpExtender.amxSuiteTab.getAppId();
        String _appSecret = BurpExtender.amxSuiteTab.getAppSecret();

        if (null == _appId || _appId.isEmpty()) {
            BurpExtender.callbacks.issueAlert("App Id is empty or null");
            return;
        }

        if (null == _appSecret || _appSecret.isEmpty()) {
            BurpExtender.callbacks.issueAlert("App Secret is empty or null");
            return;
        }

        // parse the request
        IRequestInfo requestInfo = BurpExtender.helpers.analyzeRequest(currentRequest);

        byte[] requestBody = Arrays.copyOfRange(currentRequest.getRequest(), requestInfo.getBodyOffset(),
                currentRequest.getRequest().length);
        debugln("requestBody: " + BurpExtender.helpers.bytesToString(requestBody) +
                ", length: " + requestBody.length);

        // prepare data to be signed
        long requestTimeStamp = System.currentTimeMillis() / 1000;
        String nonce = UUID.randomUUID().toString();

        String md5sumRequestBody = "";
        if (requestBody.length > 0) {
            md5sumRequestBody = BurpExtender.helpers.base64Encode(md5sum(requestBody));
        }

        String signatureStringData = String.format("%s%s%s%s%s%s", _appId, requestInfo.getMethod(),
                requestInfo.getUrl().toString().toLowerCase(), requestTimeStamp, nonce, md5sumRequestBody);
        byte[] signatureData = signatureStringData.getBytes();
        byte[] key = BurpExtender.helpers.base64Decode(_appSecret);
        debugln("signatureRawData: " + signatureStringData);

        // sign the data, prepare AMX header
        String signature = hmacSha256(key, signatureData);
        String authorizationHeader = "amx " + String.format("%s:%s:%s:%s", _appId, signature, nonce, requestTimeStamp);
        debugln("authorizationHeader: " + authorizationHeader);

        // assemble new message (remove previous AMX headers if any, put new one in)
        List<String> headers = requestInfo.getHeaders();
        headers.removeIf(header -> null != header && header.toLowerCase().contains("authorization: amx"));
        headers.add("Authorization: " + authorizationHeader);

        byte[] message = BurpExtender.helpers.buildHttpMessage(headers, requestBody);
        currentRequest.setRequest(message);
    }

    private String hmacSha256(byte[] key, byte[] data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(key, "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return BurpExtender.helpers.base64Encode(sha256_HMAC.doFinal(data));
        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            BurpExtender.stderr.println("Unable to calculate HMAC SHA256 signature, no such algorithm or invalid key");
            return null;
        }
    }

    private byte[] md5sum(byte[] data) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            return md.digest(data);
        } catch (NoSuchAlgorithmException e) {
            BurpExtender.stderr.println("Unable to calculate MD5, no such algorithm");
            return null;
        }
    }
}
