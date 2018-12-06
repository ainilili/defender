package org.nico.defender.guarder;

public class Result {

	private boolean access;
	
	private Object error;
	
	private Result(boolean access, Object error) {
		this.access = access;
		this.error = error;
	}
	
	public static Result pass() {
		return new Result(true, null);
	}
	
	public static Result notpass(Object error) {
		return new Result(false, error);
	}

	public boolean isAccess() {
		return access;
	}

	public void setAccess(boolean access) {
		this.access = access;
	}

	public Object getError() {
		return error;
	}

	public void setError(Object error) {
		this.error = error;
	}
	
}
