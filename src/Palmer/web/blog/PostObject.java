/**
 * 
 */
package Palmer.web.blog;

/**
 * @author Palmer
 *
 */
public class PostObject {

	private String title;

	/**
	 * 
	 */
	public PostObject() {
		super();
		title = "";
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return title;
	}

	public void setTitle(String string) {
		// TODO Auto-generated method stub
		this.title = string;
	}

}
