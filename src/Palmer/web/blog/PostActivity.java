package Palmer.web.blog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.app.NavUtils;

public class PostActivity extends Activity {

	private String postLink;
	TextView tvPost;
	private String key;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post);

		// read information passed to this activity
		Intent i = getIntent();
		postLink = i.getStringExtra("postLink");
		key = i.getStringExtra("apiKey");
		tvPost = (TextView)this.findViewById(R.id.viewPost);
		// if the information has been passed, send a request for the
		// specific post, if not display an error notification
		if (postLink.isEmpty()) {
			Toast.makeText(this, "Unable To Load Post", Toast.LENGTH_SHORT);

		} else {
			try {
				ProcessPost(requestPost(postLink));
			} catch (Exception e) {

			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_post, menu);
		return true;
	}

	/**
	 * requestPosts - Requests a list of posts from the Google Server
	 * 
	 * @param selfLink
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
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
