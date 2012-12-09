package com.whatstodo;

import android.app.Application;
import android.content.Context;

public class WhatsToDo extends Application {

    private static WhatsToDo instance;

    public WhatsToDo() {
    	instance = this;
    }

    public static Context getContext() {
    	return instance;
    }
}
