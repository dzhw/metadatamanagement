package eu.dzhw.fdz.metadatamanagement.common.service;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import eu.dzhw.fdz.metadatamanagement.common.config.TweetProperties;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.json.JSONObject;
import org.reflections.util.Utils;
import org.slf4j.Logger;

import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * Tweet service to create Tweet on X (former Twitter) about released data.
 */
@Service
public class TweetService {

  public static final Logger log = Utils.findLogger(TweetService.class);

  private final String consumerKey;
  private final String consumerSecret;
  private final String oauthToken;
  private final String oauthSecretToken;
  private final String endpointUrl;
  private final String mediaEndpointUrl;
  private final String version = "1.0";
  private final String imagePath;

  /**
   * TweetService constructor.
   *
   * @param tweetProperties Tweet properties/ configurations
   * @throws MalformedURLException MalformedURLException
   */
  public TweetService(TweetProperties tweetProperties) throws MalformedURLException {
    this.consumerKey = tweetProperties.getConsumerkey();
    this.consumerSecret = tweetProperties.getConsumersecret();
    this.oauthToken = tweetProperties.getOauthtoken();
    this.oauthSecretToken = tweetProperties.getOauthtokensecret();
    this.endpointUrl = tweetProperties.getEndpointurl();
    this.mediaEndpointUrl = tweetProperties.getMediaendpointurl();
    this.imagePath = tweetProperties.getImagepath();
  }

  /**
   * Posts a tweet on X (formerly Twitter).
   *
   * For authorization parameter @see [Authorizing a request]
   * (https://developer.twitter.com/en/docs/authentication/oauth-1-0a/authorizing-a-request) and creating nonce and
   * signature @see source [TwitterOauthHeaderGenerator](https://github.com/smilep/twitter-play/blob/
   * 6fca6dec72387882318dc62fd98dbcf70ce1467b/src/main/java/com/smilep/twitter/helper/TwitterOauthHeaderGenerator.java)
   *
   * @param tweetTextInput Tweet text to be posted
   */
  public ResponseEntity<?> createTweet(String tweetTextInput) {
    try {
      // set nonce and timestamp, needed to create authentication parameter
      String nonce = getNonce();
      String timestamp = String.valueOf(Instant.now().getEpochSecond());

      // get image id
      String imageId = getImageId(nonce, timestamp);

      // post a tweet using the POST /2/tweets endpoint and reference the uploaded media file
      OkHttpClient client = new OkHttpClient().newBuilder().build();
      MediaType mediaType = MediaType.parse("application/json");
      String content;
      if (imageId != null) {
        content = "{\"text\": \"" + tweetTextInput + "\"," + "\"media\": {\"media_ids\": [\"" + imageId + "\"]}}";
      } else {
        content = "{\"text\": \"" + tweetTextInput + "\"}";
      }
      RequestBody body = RequestBody.create(mediaType, content);
      String baseSignatureString = generateSignatureBaseString(HttpMethod.POST.name(), endpointUrl, nonce, timestamp,
          new HashMap<>());
      String signature = encryptUsingHmacSha1(baseSignatureString);
      Request request = new Request.Builder()
          .url(endpointUrl)
          .method("POST", body)
          .addHeader("Content-Type", "application/json")
          .addHeader("Authorization", "OAuth oauth_consumer_key=\"" + consumerKey + "\","
            + "oauth_token=\"" + oauthToken + "\","
            + "oauth_signature_method=\"HMAC-SHA1\","
            + "oauth_timestamp=\"" + timestamp + "\","
            + "oauth_nonce=\"" + nonce + "\","
            + "oauth_version=\"" + version + "\","
            + "oauth_signature=\"" + signature + "\"")
          .build();
      Response response = client.newCall(request).execute(); // current free access plan allows 50 requests/24h
      if (response.isSuccessful()) {
        log.error("Tweet successfully created " + response.code());
        return ResponseEntity.status(response.code()).body("Tweet successfully created " + response.code());
      } else {
        log.error("Error occurred for Tweet " + response.code());
        return ResponseEntity.status(response.code()).body("Error occurred for Tweet " + response.code());
      }
    } catch (Exception e) {
      log.error("Error occurred for Tweet " + e);
      return new ResponseEntity<>("Error occurred for Tweet ", HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

  /**
   * Uploads image file to X so that it can be referenced and posted when creating a tweet.
   *
   * @param nonce     Nonce
   * @param timestamp Timestamp
   * @return Media ID of the uploaded image file
   */
  private String getImageId(String nonce, String timestamp) {
    String mediaId = null;

    Map<String, String> requestParams = new HashMap<>();
    requestParams.put("media_category", "tweet_image");
    String baseSignatureString = generateSignatureBaseString(HttpMethod.POST.name(), mediaEndpointUrl, nonce,
        timestamp, requestParams);
    String signature = encryptUsingHmacSha1(baseSignatureString);

    OkHttpClient client = new OkHttpClient().newBuilder().build();
    File file = new File(imagePath);
    RequestBody body = new MultipartBody.Builder().setType(MultipartBody.FORM)
        .addFormDataPart("media", file.getName(), RequestBody.create(MediaType.parse("application/octet-stream"),
          file))
        .build();
    Request request = new Request.Builder()
        .url(mediaEndpointUrl + "?media_category=tweet_image")
        .method("POST", body)
        .addHeader("Authorization", "OAuth oauth_consumer_key=\"" + consumerKey + "\","
          + "oauth_token=\"" + oauthToken + "\","
          + "oauth_signature_method=\"HMAC-SHA1\","
          + "oauth_timestamp=\"" + timestamp + "\","
          + "oauth_nonce=\"" + nonce + "\","
          + "oauth_version=\"" + version + "\","
          + "oauth_signature=\"" + signature + "\"")
        .build();
    try {
      Response response = client.newCall(request).execute();
      if (response.isSuccessful() && response.body() != null) {
        JSONObject json = new JSONObject(response.body().string());
        mediaId = json.getString("media_id_string");
      } else {
        log.error("Could not upload media. Request resulted in error code. " + response.code());
      }
    } catch (IOException e) {
      log.error("Could not upload media " + e);
    }

    log.info("Media ID of the uploaded image " + file.getPath() + ": " + mediaId);
    return mediaId;
  }

  /**
   * Generate base string to generate the value for oauth signature.
   *
   * @param httpMethod    HTTP Method, e.g. "POST"
   * @param url           Endpoint URL
   * @param nonce         Nonce
   * @param timestamp     Timestamp
   * @param requestParams Request parameter
   * @return Signature string
   */
  private String generateSignatureBaseString(String httpMethod, String url, String nonce, String timestamp,
                                             Map<String, String> requestParams) {
    Map<String, String> params = new HashMap<>();
    requestParams.forEach((key, value) -> params.put(encode(key), encode(value)));
    params.put(encode("oauth_consumer_key"), encode(consumerKey));
    params.put(encode("oauth_nonce"), encode(nonce));
    params.put(encode("oauth_signature_method"), encode("HMAC-SHA1"));
    params.put(encode("oauth_timestamp"), encode(timestamp));
    params.put(encode("oauth_token"), encode(oauthToken));
    params.put(encode("oauth_version"), encode(version));
    Map<String, String> sortedParams = params.entrySet().stream().sorted(Map.Entry.comparingByKey())
        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (oldValue, newValue) -> oldValue,
        LinkedHashMap::new));
    StringBuilder base = new StringBuilder();
    sortedParams.forEach((key, value) -> base.append(key).append("=").append(value).append("&"));
    base.deleteCharAt(base.length() - 1);

    return httpMethod.toUpperCase(Locale.ENGLISH) + "&" + encode(url) + "&" + encode(base.toString());
  }

  /**
   * Encrypts input string through HmacSHA1 algorithm.
   *
   * @param input Input string to be encrypted
   * @return Encrypted string
   */
  private String encryptUsingHmacSha1(String input) {
    String secret = encode(consumerSecret) + "&" + encode(oauthSecretToken);
    byte[] keyBytes = secret.getBytes(StandardCharsets.UTF_8);
    SecretKey key = new SecretKeySpec(keyBytes, "HmacSHA1");
    Mac mac;
    try {
      mac = Mac.getInstance("HmacSHA1");
      mac.init(key);
    } catch (NoSuchAlgorithmException | InvalidKeyException e) {
      e.printStackTrace();
      return null;
    }
    byte[] signatureBytes = mac.doFinal(input.getBytes(StandardCharsets.UTF_8));
    String encoded = new String(java.util.Base64.getEncoder().encode(signatureBytes), StandardCharsets.UTF_8);

    return URLEncoder.encode(encoded, StandardCharsets.UTF_8);
  }

  /**
   * Percentage encode String as per RFC 3986, Section 2.1.
   *
   * @param value Value to be encoded
   * @return Encoded value
   */
  private String encode(String value) {
    String encoded = "";
    try {
      encoded = URLEncoder.encode(value, StandardCharsets.UTF_8);
    } catch (Exception e) {
      e.printStackTrace();
    }
    StringBuilder sb = new StringBuilder();
    char focus;
    for (int i = 0; i < encoded.length(); i++) {
      focus = encoded.charAt(i);
      if (focus == '*') {
        sb.append("%2A");
      } else if (focus == '+') {
        sb.append("%20");
      } else if (focus == '%' && i + 1 < encoded.length() && encoded.charAt(i + 1) == '7'
          && encoded.charAt(i + 2) == 'E') {
        sb.append('~');
        i += 2;
      } else {
        sb.append(focus);
      }
    }

    return sb.toString();
  }

  /**
   * Creates nonce ("number used once") from random data, needed for authentication.
   *
   * @return Nonce string.
   */
  private String getNonce() {
    int leftLimit = 48; // numeral '0'
    int rightLimit = 122; // letter 'z'
    int targetStringLength = 10;
    Random random = new Random();

    return random.ints(leftLimit, rightLimit + 1).filter(i -> (i <= 57 || i >= 65)
      && (i <= 90 || i >= 97)).limit(targetStringLength)
      .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append).toString();
  }
}
