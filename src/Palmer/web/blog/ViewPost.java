/**
 * 
 */
package Palmer.web.blog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.json.JSONException;
import org.json.JSONObject;

import android.support.v4.app.Fragment;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

/**
 * @author Palmer
 *
 */
public class ViewPost extends android.support.v4.app.Fragment {

	private TextView tvPost;
	private String postLink;
	private String key;
	/**
	 * 
	 */
	public ViewPost() {
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see android.app.Fragment#onCreateView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		Intent launchingIntent = getActivity().getIntent();
		postLink = launchingIntent.getStringExtra("postLink");
		key = launchingIntent.getStringExtra("apiKey");
	 
	    tvPost = (TextView) inflater.inflate(R.layout.activity_post, container, false);
	    if (postLink.isEmpty()) {
			Toast tst = Toast.makeText(this.getActivity(), "Unable To Load Post", Toast.LENGTH_SHORT);
			tst.show();

		} else {
			try {
				ProcessPost(requestPost(postLink));
			} catch (Exception e) {

			}
		}
	     
	    return tvPost;
	}

	protected String requestPost(String selfLink) throws MalformedURLException,
	IOException {

// add the apiKey to the end of the url
String newFeed = selfLink + "?" + key;
StringBuilder response = new StringBuilder();
Log.v("gsearch", "gsearch url:" + newFeed);

// create the url
URL url = new URL(newFeed);

HttpURLConnection httpconn = (HttpURLConnection) url.openConnection();

if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK) {
	BufferedReader input = new BufferedReader(new InputStreamReader(
			httpconn.getInputStream()), 8192);
	String strLine = null;
	while ((strLine = input.readLine()) != null) {
		response.append(strLine);
	}
	input.close();
}
return response.toString();
}

@SuppressLint("ParserError")
protected void ProcessPost(String postResponse)
	throws IllegalStateException, IOException, JSONException,
	NoSuchAlgorithmException {
SpannableStringBuilder sb = new SpannableStringBuilder();

Log.v("spostData", "spostData result:" + postResponse);
JSONObject mResponseObject = new JSONObject(postResponse);
JSONObject replies = mResponseObject.getJSONObject("replies");

//Get the relevant post information (title, content, replies)
String postTitle = mResponseObject.getString("title");
postTitle = "<i>" + postTitle + "</i>";
String postContent = mResponseObject.getString("content");
postContent = "<p>" + postContent + "View Post</p>";
String postReplies = replies.getString("totalItems") + " Comments";
String postComments = "<a href=\"" + replies.getString("selfLink") + "\">" 
	+ postReplies +  "</a>";

//Set info to textView
sb.append(Html.fromHtml(postTitle));
sb.append("\n");
sb.append(Html.fromHtml(postContent));
sb.append("\n");
sb.append(Html.fromHtml(postComments));

tvPost.setText(sb);
}

}
