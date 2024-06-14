package com.example.myandroidproject.utilss;

public class Constraint {
    public static final String URL = "https://driven-mutual-grubworm.ngrok-free.app";    // ADDRESS CHANGE BY NGROK
    public static final String URL_BE = "172.16.1.129"; // ADDRESS
    public static final String PORT_BE = "8080";    // PORT

    // SIGNIN VS SIGNUP
    public static final String URL_SIGN_IN = URL + "/api/v1/user/signin";
    public static final String URL_SIGN_UP = URL + "/api/v1/user/register";

    // PRODUCT
    public static final String URL_VEHICLE_LIST = URL + "/api/v1/product";
    public static final String URL_VEHICLE_DETAIL_BY_ID = URL + "/api/v1/product/detail?id=";
    public static final String URL_ADD_TO_JOURNEY = URL + "/api/v1/rental/add?idVehicle=";
    public static final String URL_FIND_BY_TYPE = URL +"/api/v1/product/type?type=";
    public static final String URL_SEARCH_VEHICLE = URL + "/api/v1/product/search?txtSearch=";

    // CART ITEM
    public static final String URL_CART_ITEM = URL + "/api/v1/rental/getAllCartItem";
    public static final String URL_GET_CART_ITEM_TO_PAY = URL + "/api/v1/rental/pay?cartItemId=";
    public static final String URL_SET_STATE_CART_ITEM = URL + "/api/v1/rental/setstate";
    public static final String URL_REMOVE_CART_ITEM = URL + "/api/v1/rental/delete";


    // ORDER ITEM
    public static final String URL_ORDER_ITEM_DETAIL_BY_ID = URL + "/api/v1/order/getDetail";
    public static final String URL_GET_HISTORY_ORDERITEM_COMPLETED = URL + "/api/v1/order/getHistoryOrderContinue";
    public static final String URL_GET_HISTORY_ORDERITEM_CONTINUE = URL + "/api/v1/order/getHistoryOrderContinue";
    public static final String URL_LIST_ORDER = URL + "/api/v1/listOrder";
    public static final String URL_READ_MESSAGE = URL + "/api/v1/message/read";
    public static final String URL_SEND_MESSAGE = URL + "/api/v1/message/send";
    public static final String URL_GET_ADMINS_MESSAGE = URL + "/api/v1/message/admins";
    public static final String URL_READ_ADMIN_NOT = URL + "/api/vi/notify/send";
    public static final String URL_SEND_NOT = URL + "/api/vi/notify/send";


    // ORDER ITEM
    public static final String URL_ADD_ORDER_ITEM = URL + "/api/v1/order/add";

    // USER
    public static final String URL_USER_BY_ID = URL + "/api/v1/users/";
    public static final String URL_USER_LIST = URL + "/api/v1/users";


    // Variable
    public static final String ID_VEHICLE = "id";
    public static final String CART_ITEM = "jsonobject";
    public static final String ID_ORDER_ITEM = "id_order_item";
    public static final String ID_CART_ITEM = "id_cart_item";

}
