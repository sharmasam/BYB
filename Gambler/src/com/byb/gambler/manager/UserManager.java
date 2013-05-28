package com.byb.gambler.manager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import com.byb.gambler.models.UserInfo;
import com.byb.gambler.models.UserStatistics;
import com.byb.gambler.service.RestClient;
import com.byb.gambler.utility.BYBPreferences;

public class UserManager {

	private static UserManager mManager;
	private RestClient client;

	public static UserManager getInstance() {
		if (mManager == null) {
			mManager = new UserManager();
		}
		return mManager;
	}

	public boolean parseJson(JSONObject jsonData, Context context) {

		// A Simple JSONObject Creation
		try {
			UserInfo userInfo = UserInfo.getInstance();
			UserStatistics userStatistics = new UserStatistics();
			// A Simple JSONObject Parsing

			JSONObject jsonUser = new JSONObject(jsonData.getString("user"));
			userInfo.setAccessToken(jsonData.getString("access_token"));
			userInfo.setID(jsonUser.getString("id"));
			userInfo.setFullName(jsonUser.getString("fullName"));
			userInfo.setEmail(jsonUser.getString("email"));
			if(jsonUser.has("password")){
				userInfo.setPassword(jsonUser.getString("password"));
			}
			if(userInfo.getPassword()!= null){
				BYBPreferences.getInstance(context).setUserCredentials(userInfo.getEmail(), userInfo.getPassword());
			}
			if(jsonUser.has("thumbnailAvatarURL")){
				userInfo.setThumbNailURL(jsonUser.getString("thumbnailAvatarURL"));
			}
			userInfo.setBirthday(jsonUser.getString("birthday"));
			userInfo.setLastSeen(jsonUser.getInt("lastSeen"));
			userInfo.setPublicAccount(jsonUser.getBoolean("publicAccount"));
			userInfo.setNotifyWhenWonABet(jsonUser
					.getBoolean("notifyWhenWonABet"));
			userInfo.setNotifyWhenLostBet(jsonUser
					.getBoolean("notifyWhenLostABet"));
			userInfo.setNotifWhenAdded(jsonUser.getBoolean("notifyWhenAdded"));
			userInfo.setInsertTime(jsonUser.getInt("insertTime"));

			JSONObject statistics = jsonUser.getJSONObject("statistics");

			userStatistics.setBallsAvailable(statistics
					.getInt("ballsAvailable"));
			userStatistics.setBallsBet(statistics.getInt("ballsBet"));
			userStatistics.setBallsLost(statistics.getInt("ballsLost"));
			userStatistics.settipsMade(statistics.getInt("tipsMade"));
			userStatistics.setTipsWon(statistics.getInt("tipsWon"));
			userStatistics.setNumberOfBadges(statistics
					.getInt("numberOfBadges"));
			userStatistics.setBallsAwaitingGrab(statistics
					.getInt("ballsAwaitingGrab"));
			userStatistics.setBallsInAir(statistics.getInt("ballsInAir"));
			userInfo.setUserStatistics(userStatistics);

			return true;

		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}

	// public void registerViaEmail(final Context context){
	// new Thread(){
	// public void run() {
	//
	//
	// try {
	// UserInfo userInfo = UserInfo.getInstance();
	//
	// client = new RestClient("users.json");
	// client.AddHeader("Content-Type", "application/json");
	//
	// JSONObject method = new JSONObject();
	//
	// JSONObject mParams = new JSONObject();
	//
	// mParams.put("birthday", userInfo.getBirthday().toString());
	// mParams.put("email", userInfo.getEmail().toString());
	// mParams.put("fullName", userInfo.getFullName());
	// mParams.put("notifyWhenAdded", userInfo.isNotifWhenAdded());
	// mParams.put("notifyWhenLostABet", userInfo.isNotifyWhenLostBet());
	// mParams.put("notifyWhenWonABet", userInfo.isNotifyWhenWonABet());
	// mParams.put("password", userInfo.getPassword());
	// mParams.put("publicAccount", 0);
	//
	// method.put("action", "create");
	// method.put("user", mParams);
	// // client.AddParam("user", mParams.toString());
	// Log.d("tag", "req :"+ method.toString());
	// client.Execute(RequestMethod.POST,method);
	//
	// String response = client.getResponse();
	// parseJson(response);
	//
	// Log.d("tag", "Res : "+ response);
	// } catch (Exception e) {
	// e.printStackTrace();
	// }
	// };}.start();
	// }

	public String BitMapToString(Bitmap bitmap) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
		byte[] b = baos.toByteArray();
		String temp = null;
		try {
			System.gc();
			temp = Base64.encodeToString(b, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} catch (OutOfMemoryError e) {
			baos = new ByteArrayOutputStream();
			bitmap.compress(Bitmap.CompressFormat.PNG, 50, baos);
			b = baos.toByteArray();
			temp = Base64.encodeToString(b, Base64.DEFAULT);
		}
		return temp;
	}

	public void registerViaFB() {

	}

	public void forgetPassword(Context context, String emailAdd) {

	}

	public String callWS(String method, byte[] postData, boolean needAuthorization,String key) {

		String responseString = "";

		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}
			} };

			URL url = new URL("https://api.betyourballs.com.au/1/" + method);

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
			.setDefaultSSLSocketFactory(sc.getSocketFactory());

			HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();

			conn.setDoOutput(true);
			conn.setDoInput(true);
			conn.setUseCaches(false);

			conn.setRequestMethod("GET");
			conn.setRequestProperty("Content-Type", "application/json");

			if(needAuthorization)
				conn.setRequestProperty("Authorization", "Basic "+key);
			System.out.println("Key = "+key);

			OutputStream out = conn.getOutputStream();

			out.write(postData);

			if (conn.getResponseMessage().toLowerCase().equals("ok")) {
				InputStream is = conn.getInputStream();
				int ch;
				StringBuffer b = new StringBuffer();
				while ((ch = is.read()) != -1) {
					b.append((char) ch);
				}
				responseString = b.toString();
				// Log.d(tag,method + "','" + responseString);
				Log.e("response string  :", responseString);
			}
			out.close();

		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return responseString;
	}
	
	public String callWSforPost(String method, String postData, boolean needAuthorization,String key) {

		String responseString = "";
		OutputStreamWriter request = null;
		HttpsURLConnection conn = null ;

		try {
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}

				public void checkServerTrusted(
						java.security.cert.X509Certificate[] certs,
						String authType) {
				}
			} };

			URL url = new URL("https://api.betyourballs.com.au/1/" + method);

			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection
			.setDefaultSSLSocketFactory(sc.getSocketFactory());

			conn = (HttpsURLConnection) url.openConnection();

//			conn.setDoOutput(true);
//			conn.setDoInput(true);
			conn.setUseCaches(false);

			conn.setRequestMethod("POST");
			conn.setRequestProperty("Content-Type", "application/json");
			conn.setRequestProperty("Accept", "application/json");
			
			if(needAuthorization)
				conn.setRequestProperty("Authorization", "Basic "+key);
			System.out.println("Key = "+key);

			 request = new OutputStreamWriter(conn.getOutputStream());
			 if(postData != null)
             	request.write(postData);
             request.flush();
             request.close();            
             String line = "";               
             InputStreamReader isr = new InputStreamReader(conn.getInputStream());
             BufferedReader reader = new BufferedReader(isr);
             StringBuilder sb = new StringBuilder();
             while ((line = reader.readLine()) != null)
             {
                 sb.append(line + "\n");
             }
             // Response from server after login process will be stored in response variable.                
             responseString = sb.toString();
             // You can perform UI operations here
             isr.close();
             reader.close();
             
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			try {
				Log.d("tag", "Code : "+ conn.getResponseCode());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			try {
				Log.d("tag", "Code : "+ conn.getResponseCode());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			try {
				Log.d("tag", "Code : "+ conn.getResponseCode());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (ProtocolException e) {
			// TODO Auto-generated catch block
			try {
				Log.d("tag", "Code : "+ conn.getResponseCode());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			try {
				Log.d("tag", "Code : "+ conn.getResponseCode());
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			e.printStackTrace();
		}

		return responseString;
	}
}
