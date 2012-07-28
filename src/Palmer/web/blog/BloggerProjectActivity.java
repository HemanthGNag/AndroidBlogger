package Palmer.web.blog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;

/**
 * 
 * @author Palmer
 *
 */
public class BloggerProjectActivity extends FragmentActivity {
	private FragmentManager fm;
	private SimpleAdapter postAdapter;
	private ArrayList<HashMap<String,String>> posts;
	private EditText edt1;
	private Button btn1;
	static String url = "https://www.googleapis.com/blogger/v3/blogs/byurl?url=http://strandedhhj.blogspot.com/";
	private String displayString;
	private static String apiKey = "&key=AIzaSyDZOJTqANF0snQb7kfThiqjXM9PAeQaqH4";
	private static String blogID;
	private String spostSL;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		fm = this.getSupportFragmentManager();
		Fragment frag = fm.findFragmentById(R.id.ListPostFragment);
		listpost lp = (listpost)frag;
		
		posts = new ArrayList<HashMap<String,String>>();
		postAdapter = new SimpleAdapter(this, posts, android.R.layout.two_line_list_item, 
				new String[]{"Posts","Descriptions"}, new int[]{android.R.id.text1, android.R.id.text2} );
		lp.setListAdapter(postAdapter);
		try {
			ProcessResponse(RequestSite());
		} catch (Exception e) {
			Log.v("Exception google search", "Exception:" + e.getMessage());
		}
		/*
		tv1.setTextSize(getResources().getDimension(R.dimen.small));
		//Linkify.addLinks(tv1, Linkify.ALL);
		tv1.setMovementMethod(LinkMovementMethod.getInstance());
		*/
		
	}

	/**
	 * ProcessReponse - Processes the response sent by the Google Server
	 * @param resp
	 * @throws IllegalStateException
	 * @throws IOException
	 * @throws JSONException
	 * @throws NoSuchAlgorithmException
	 */
	protected void ProcessResponse(String resp) throws IllegalStateException,
			IOException, JSONException, NoSuchAlgorithmException {
		
		Log.v("blogData", "blogData result:" + resp);
		JSONObject mResponseObject = new JSONObject(resp);
		
		//get the post data from the response
		JSONObject blogPosts = mResponseObject.getJSONObject("posts");
		
		String postSL = blogPosts.getString("selfLink");
		
		//retrieve the post response data
		ProcessPosts(requestPosts(postSL));
		/*
		URLSpan[] spans = sb.getSpans(0, sb.length(), URLSpan.class);
		// Add onClick listener for each of URLSpan object
		for (final URLSpan span : spans) {
		    int start = sb.getSpanStart(span);
		    int end = sb.getSpanEnd(span);
		    spostSL = span.getURL();
		 
		    //sb.removeSpan(span);
		    
		    sb.setSpan(new ClickableSpan()
		    {
		    @Override
		    public void onClick(View widget) {
		                Intent viewPost = new Intent(getBaseContext(), PostActivity.class);
		                //get the item that has been clicked
		                
		                //passing post information to viewPost activity
		                viewPost.putExtra("postLink", spostSL);
		                viewPost.putExtra("apiKey", apiKey);
		                startActivity(viewPost);
		    }      
		    }, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		*/
		
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

	/**
	 * requestPosts - Requests a list of posts from the Google Server
	 * @param selfLink
	 * @return
	 * @throws MalformedURLException
	 * @throws IOException
	 */
	protected String requestPosts(String selfLink)
			throws MalformedURLException, IOException {

		//add the apiKey to the end of the url
		String newFeed = selfLink + "?" + apiKey;
		StringBuilder response = new StringBuilder();
		Log.v("gsearch", "gsearch url:" + newFeed);
		
		//create the url
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
	protected void ProcessPosts(String postResponse)
			throws IllegalStateException, IOException, JSONException,
			NoSuchAlgorithmException {
	//	SpannableStringBuilder sb = new SpannableStringBuilder();
		HashMap<String, String> item;
		Log.v("postData", "postData result:" + postResponse);
		JSONObject mResponseObject = new JSONObject(postResponse);
		JSONArray array = mResponseObject.getJSONArray("items");
		
		for (int i = 0; i < array.length(); i++) {
			item = new HashMap<String, String>();
			JSONArray labels = array.getJSONObject(i).getJSONArray("labels");
			item.put("Posts", array.getJSONObject(i).getString("title"));
			item.put("Descriptions", Html.fromHtml(array.getJSONObject(i).getString("content")).toString().substring(0, 60).concat("..."));
			posts.add(item);
			postAdapter.notifyDataSetChanged();
		}
		
		
	}

}