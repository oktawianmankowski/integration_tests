package edu.iis.mto.blog.api.request;

public class PostRequest {

	private String entry;

	public String getEntry() {
		return entry;
	}

	public void setEntry(String entry) {
		this.entry = entry;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof PostRequest) {
			return entry.equals(((PostRequest) obj).entry);
		}
		return false;
	}

	@Override
	public int hashCode() {
		return entry.hashCode();
	}
}
