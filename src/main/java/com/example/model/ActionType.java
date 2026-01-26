package com.example.model;

import lombok.Getter;

@Getter
public enum ActionType {

    // --- ADMIN CITY ACTIONS ---
    ADMIN_CITY_CREATE_NOT_FOUND("admin_city_create_not_found"),
    ADMIN_CITY_CREATE_CITY_CREATED("admin_city_create_city_created"),
    ADMIN_CITY_DELETE_NOT_FOUND("admin_city_delete_not_found"),
    ADMIN_CITY_DELETE_DELETED("admin_city_delete_deleted"),
    ADMIN_CITY_UPDATE_CITY_UPDATED("admin_city_update_city_updated"),
    ADMIN_CITY_UPDATE_CITY_NAME_NOT_FOUND("admin_city_update_city_name_not_found"),

    // --- ADMIN USER ACTIONS ---
    ADMIN_USER_UPDATE_USER_NOT_FOUND("admin_user_update_user_not_found"),
    ADMIN_USER_UPDATE_USER_UPDATED("admin_user_update_user_updated"),
    ADMIN_USER_DELETE_USER_NOT_FOUND("admin_user_delete_user_not_found"),
    ADMIN_USER_DELETE_USER_DELETED("admin_user_delete_user_deleted"),

    // --- ADMIN TICKET ACTIONS ---
    ADMIN_TICKET_DELETE_TICKET_INCORRECT_ID("admin_ticket_delete_ticket_incorrect_id"),
    ADMIN_TICKET_DELETE_TICKET_DELETED("admin_ticket_delete_ticket_deleted"),

    // --- ADMIN TRIP ACTIONS ---
    ADMIN_TRIP_CREATE_TRIP_BUS_IS_BUSY("user_trip_create_trip_bus_is_busy"),
    ADMIN_TRIP_CREATE_TRIP_ROUTE_NOT_FOUND("user_trip_create_trip_route_not_found"),
    ADMIN_TRIP_CREATE_TRIP_CREATED("user_trip_create_trip_created"),
    ADMIN_TRIP_CREATE_TRIP_BUS_NOT_FOUND("user_trip_create_trip_bus_not_found"),
    ADMIN_TRIP_CREATE_TRIP_BUS_FOUND("user_trip_create_trip_bus_found"),

    // --- USER AUTH ACTIONS ---
    USER_AUTH_GET_DETAIL_USER_NOT_FOUND("user_auth_get_detail_user_not_found"),
    USER_AUTH_GET_DETAIL_USER_GET_DETAIL("user_auth_get_detail_user_get_detail"),
    USER_AUTH_LOGIN_INCORRECT_PASSWORD("user_auth_login_incorrect_password"),
    USER_AUTH_LOGIN_INCORRECT_LOGIN("user_auth_login_incorrect_login"),
    USER_AUTH_LOGIN_CORRECT("user_auth_login_correct"),
    USER_AUTH_UPDATE_USER_NOT_FOUND("user_auth_update_user_not_found"),
    USER_AUTH_UPDATE_USER_PASSWORD_UPDATE("user_auth_update_user_password_update"),

    // --- USER TICKET ACTIONS ---
    USER_TICKET_BUY_TICKET_TRIP_NOT_FOUND("user_ticket_buy_ticket_trip_not_found"),
    USER_TICKET_BUY_TICKET_INCORRECT_LOGIN("user_ticket_buy_ticket_incorrect_login"),
    USER_TICKET_BUY_TICKET_POINT_NOT_FOUND("user_ticket_buy_ticket_point_not_found"),
    USER_TICKET_BUY_TICKET_TICKET_NOT_CREATED("user_ticket_buy_ticket_ticket_not_created"),
    USER_TICKET_BUY_TICKET_CREATED("user_ticket_buy_ticket_created"),
    USER_TICKET_DELETE_TICKET_SEAT_NUMBER_NOT_FOUND("user_ticket_delete_ticket_seat_number_not_found"),
    USER_TICKET_DELETE_TICKET_TICKET_NOT_FOUND("user_ticket_delete_ticket_ticket_not_found"),
    USER_TICKET_DELETE_TICKET_SEAT_NUMBER_NOT_BELONG_USER("user_ticket_delete_ticket_seat_number_not_belong_user"),
    USER_TICKET_DELETE_TICKET_TICKET_DELETED("user_ticket_delete_ticket_ticket_deleted"),

    // --- USER TRIP ACTIONS ---
    USER_TRIP_SEARCH_TRIP_DATE_NOT_VALID_FORMAT("user_trip_search_trip_date_not_valid_format"),
    //USER_TRIP_SEARCH_TRIP_TRIPS_RETURNED("user_trip_search_trip_trips_returned"),

    // --- USER PROFILE ACTIONS ---
    USER_USER_PROFILE_UPDATE_PROFILES_INCORRECT_LOGIN("user_profile_update_incorrect_login"),
    USER_USER_PROFILE_UPDATE_PHONE_NUMBER_UPDATED("user_profile_update_phone_number_updated"),
    USER_USER_PROFILE_UPDATE_ADDRESS_UPDATED("user_profile_update_address_updated"),
    USER_USER_PROFILE_PROFILE_CITY_NUMBER_UPDATED("user_profile_update_city_updated"),
    USER_USER_PROFILE_PROFILE_BIO_UPDATED("user_profile_update_bio_updated");

    private final String code;

    ActionType(String code) {
        this.code = code;
    }
}