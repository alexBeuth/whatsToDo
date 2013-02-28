package com.whatstodo.net;

import android.util.AndroidException;

public class SynchronizationException extends AndroidException{

	private static final long serialVersionUID = 4517537309340076393L;
	
    public SynchronizationException() {
    }

    public SynchronizationException(String name) {
        super(name);
    }

    public SynchronizationException(Exception cause) {
        super(cause);
    }

}
