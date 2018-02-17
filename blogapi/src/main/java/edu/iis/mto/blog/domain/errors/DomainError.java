package edu.iis.mto.blog.domain.errors;

public class DomainError extends RuntimeException {

	public DomainError(String msg) {
		super(msg);
	}

	private static final long serialVersionUID = 1L;

}
