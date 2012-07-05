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

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.util.Linkify;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class BloggerProjectActivity extends Activity {
	TextView tv1;
	EditText edt1;
	Button btn1;
	static String url = "https://www.googleapis.com/blogger/v3/blogs/byurl?url=http://strandedhhj.blogspot.com/";
	private static String apiKey = "&key=AIzaSyDZOJTqANF0snQb7kfThiqjXM9PAeQaqH4";
	private static String blogID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tv1 = (TextView) this.findViewById(R.id.display);

		try {
			ProcessResponse(RequestSite());
		} catch (Exception e) {
			Log.v("Exception google search", "Exception:" + e.getMessage());
		}
		tv1.setTextSize(getResources().getDimension(R.dimen.small));
		tv1.setMovementMethod(LinkMovementMethod.getInstance());
		Linkify.addLinks(tv1, Linkify.WEB_URLS);
	}

	/**
	 * 
	 * @param resp
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws JSONException
	 * @throws NoSuchAlgorithmException
	 */
	protected void ProcessResponse(String resp) throws IllegalStateException,
			IOException, JSONException, NoSuchAlgorithmException {
		StringBuilder sb = new StringBuilder();
		Log.v("blogData", "blogData result:" + resp);
		JSONObject mResponseObject = new JSONObject(resp);
		String blogTitle = mResponseObject.getString("name");
		blogID = mResponseObject.getString("id");
		String blogDes = mResponseObject.getString("description");
		String blogURL = mResponseObject.getString("url");
		JSONObject blogPosts = mResponseObject.getJSONObject("posts");
		String numPosts = blogPosts.getString("totalItems");
		String postSL = blogPosts.getString("selfLink");
		sb.append(blogTitle);
		sb.append("\n");
		sb.append("Number of Posts: " + numPosts + "\n");
		sb.append(ProcessPosts(requestPosts(postSL)));
		sb.append(blogDes);
		tv1.setText(sb.toString());
		tv1.setLinksClickable(true);
	}

	/**
	 * 
	 * @param searchString
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected String RequestSite() throws MalformedURLException, IOException {

		// TODO Auto-generated method stub
		String newFeed = url + apiKey;
		StringBuilder response = new StringBuilder();
		Log.v("gsearch", "gsearch url:" + newFeed);
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

	protected String requestPosts(String selfLink)
			throws MalformedURLException, IOException {

		String newFeed = selfLink + "?" + apiKey;
		StringBuilder response = new StringBuilder();
		Log.v("gsearch", "gsearch url:" + newFeed);
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

	protected String ProcessPosts(String postResponse)
			throws IllegalStateException, IOException, JSONException,
			NoSuchAlgorithmException {
		StringBuilder sb = new StringBuilder();
		Log.v("postData", "postData result:" + postResponse);
		JSONObject mResponseObject = new JSONObject(postResponse);
		JSONArray array = mResponseObject.getJSONArray("items");
		for (int i = 0; i < array.length(); i++) {
			Log.v("result", i + "] " + array.get(i).toString());
			String title = array.getJSONObject(i).getString("title");
			title = "<i>" + title + "</i>";
			String urllink = array.getJSONObject(i).getString("url");

			sb.append(Html.fromHtml(title));
			sb.append("\n");
			sb.append(urllink);
			sb.append("\n\n");
		}
		return sb.toString();
	}

}