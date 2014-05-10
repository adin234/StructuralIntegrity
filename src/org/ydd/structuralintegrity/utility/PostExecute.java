package org.ydd.structuralintegrity.utility;

import org.json.JSONException;

public interface PostExecute {
	public void doAfter(String string) throws JSONException;
}
