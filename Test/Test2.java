/* Base64 Encoder
String encoding = Base64.getEncoder().encodeToString(("login:passwd").getBytes(‌"UTF‌​-8"​));
HttpURLConnection conn = (HttpURLConnection) url.openConnection();
conn.setRequestMethod("POST");
conn.setDoOutput(true);
conn.setRequestProperty("Authorization", "Basic " + encoding);

String encoding = Base64Encoder.encode ("login:passwd");
org.apache.http.client.methods.HttpPost httppost = new HttpPost(url);
httppost.setHeader("Authorization", "Basic " + encoding);
 */
