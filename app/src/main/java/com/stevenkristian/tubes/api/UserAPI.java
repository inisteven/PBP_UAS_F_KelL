package com.stevenkristian.tubes.api;

public class UserAPI {
    public static final String ROOT_URL   = "http://10.0.2.2:8000/";
    public static final String ROOT_API   = ROOT_URL+ "api/";

    public static final String URL_SELECT = ROOT_API+"user";
    public static final String URL_ADD = ROOT_API+"user";
    public static final String URL_UPDATE = ROOT_API+"user/";
    public static final String URL_DELETE = ROOT_API+"user/";
    public static final String URL_LOGIN = ROOT_API+"user/search/";
}
