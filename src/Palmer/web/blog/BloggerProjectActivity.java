package Palmer.web.blog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class BloggerProjectActivity extends Activity {
	TextView tv1;
	EditText edt1;
	Button btn1;
	static String url = "https://www.googleapis.com/blogger/v3/blogs/byurl?url=http://strandedhhj.blogspot.com/";
	String displayString;
	private static String apiKey = "&key=AIzaSyDZOJTqANF0snQb7kfThiqjXM9PAeQaqH4";
	private static String blogID;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		tv1 = (TextView) this.findViewById(R.id.display);
		displayString = this.getString(R.string.hello);
		try {
			ProcessResponse(RequestSite());
		} catch (Exception e) {
			Log.v("Exception google search", "Exception:" + e.getMessage());
		}
		tv1.setTextSize(getResources().getDimension(R.dimen.small));
		//Linkify.addLinks(tv1, Linkify.ALL);
		tv1.setMovementMethod(LinkMovementMethod.getInstance());
		
		
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
		SpannableStringBuilder sb = new SpannableStringBuilder();
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
		//sb.append(blogDes);
		URLSpan[] spans = sb.getSpans(0, sb.length(), URLSpan.class);
		// Add onClick listener for each of URLSpan object
		for (final URLSpan span : spans) {
		    int start = sb.getSpanStart(span);
		    int end = sb.getSpanEnd(span);
		 
		    sb.removeSpan(span);
		    sb.setSpan(new ClickableSpan()
		    {
		    @Override
		    public void onClick(View widget) {
		                Toast tst = Toast.makeText(getBaseContext(), "text", Toast.LENGTH_SHORT);
		                tst.show();
		    }      
		    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		tv1.setText(sb);
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

	@SuppressLint("ParserError")
	protected SpannableStringBuilder ProcessPosts(String postResponse)
			throws IllegalStateException, IOException, JSONException,
			NoSuchAlgorithmException {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		
		Log.v("postData", "postData result:" + postResponse);
		JSONObject mResponseObject = new JSONObject(postResponse);
		JSONArray array = mResponseObject.getJSONArray("items");
		for (int i = 0; i < array.length(); i++) {
			Log.v("result", i + "] " + array.get(i).toString());
			String title = array.getJSONObject(i).getString("title");
			title = "<i>" + title + "</i>";
			String urllink = array.getJSONObject(i).getString("url");
			urllink = "<a href=\"" + urllink + "\">View Post</a>";
			sb.append(Html.fromHtml(title));
			sb.append("\n");
			//int startSpan = sb.length();
			sb.append(Html.fromHtml(urllink));
			//int endSpan = sb.length();
			//sb.setSpan(clickspan, startSpan, endSpan, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
			sb.append("\n\n");
		}
		
		
		return sb;
	}

}