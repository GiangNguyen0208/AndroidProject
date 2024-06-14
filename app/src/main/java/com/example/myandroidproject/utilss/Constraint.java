package com.example.myandroidproject.utilss;

public class Constraint {
    public static final String URL = "https://0df7-42-119-229-4.ngrok-free.app";
    public static final String URL_VEHICLE_LIST = URL + "/api/v1/product";
    public static final String URL_VEHICLE_DETAIL_BY_ID = URL + "/api/v1/product/detail?id=";
    public static final String URL_SIGN_IN = URL + "/api/v1/user/signin";
    public static final String URL_SIGN_UP = URL + "/api/v1/user/register";

    // PRODUCT
    public static final String URL_ADD_TO_JOURNEY = URL + "/api/v1/rental/add?idVehicle=";
    public static final String URL_FIND_BY_TYPE = URL +"/api/v1/product/type?type=";
    public static final String URL_SEARCH_VEHICLE = URL + "/api/v1/product/search?txtSearch=";

    // CART ITEM
    public static final String URL_CART_ITEM = URL + "/api/v1/rental/getAllCartItem";
    public static final String URL_GET_CART_ITEM_TO_PAY = URL + "/api/v1/rental/pay?cartItemId=";
    public static final String URL_SET_STATE_CART_ITEM = URL + "/api/v1/rental/setstate";
    public static final String URL_REMOVE_CART_ITEM = URL + "/api/v1/rental/delete";


    // ORDER ITEM
    public static final String URL_ADD_ORDER_ITEM = URL + "/api/v1/order/add";
    public static final String URL_USER_LIST = URL + "/api/v1/users";
    public static final String URL_USER_BY_ID = URL + "/api/v1/users/";
    public static final String URL_LIST_ORDER = URL + "/api/v1/listOrder";

    // USE


    // Variable
    public static final String ID_VEHICLE = "id_vehicle";
    public static final String CART_ITEM = "jsonobject";
    public static final String ID_CART_ITEM = "id_cart_item";
}
