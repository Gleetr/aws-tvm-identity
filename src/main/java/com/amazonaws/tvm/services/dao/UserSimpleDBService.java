package com.amazonaws.tvm.services.dao;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.*;
import com.amazonaws.tvm.Configuration;
import com.amazonaws.tvm.Utilities;
import com.amazonaws.tvm.domain.User;
import com.amazonaws.tvm.domain.UserCreationRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Store and manage users using Amazon SimpleDB.
 */
public class UserSimpleDBService implements UserService {

    private final AmazonSimpleDBClient sdb;

    /**
     * Constant for the Domain name used to store the identities.
     */
    private final static String IDENTITY_DOMAIN = Configuration.USERS_DOMAIN;

    /**
     * Constant for the userid attribute.
     */
    private final static String USER_ID = "userid";

    /**
     * Constant for the hash of password attribute.
     */
    private final static String HASH_SALTED_PASSWORD = "hash_salted_password";

    /**
     * Constant for the enabled attribute.
     */
    private final static String IS_ENABLED = "enabled";

    /**
     * Constant select expression used to list all the identities stored in the Domain.
     */
    private final static String SELECT_USERS_EXPRESSION = "select * from `" + IDENTITY_DOMAIN + "`";

    /**
     * Constant select expression used to list a specific identity by UserID attribute stored in the Domain.
     */
    private final static String SELECT_USER_BY_ID_EXPRESSION_PREFIX = "select * from `" + IDENTITY_DOMAIN + "` where " + USER_ID + "=";

    /**
     * Constant select expression used to list a specific identity by ItemName stored in the Domain.
     */
    private final static String SELECT_USER_BY_NAME_EXPRESSION_PREFIX = "select * from `" + IDENTITY_DOMAIN + "` where itemName()=";

    public UserSimpleDBService() {
        this.sdb = new AmazonSimpleDBClient(new BasicAWSCredentials(Configuration.AWS_ACCESS_KEY_ID, Configuration.AWS_SECRET_KEY));
        this.sdb.setEndpoint(Configuration.SIMPLEDB_ENDPOINT);

        // FIXME
        /*
        if (!this.doesDomainExist(IDENTITY_DOMAIN)) {
            this.createIdentityDomain();
        }
        */
    }

    /**
     * Returns the list of usernames stored in the identity domain.
     *
     * @return list of existing usernames in SimpleDB domain
     */
    @Override
    public List<User> list() {
        List<User> users = new ArrayList<User>();
        SelectResult result = null;

        do {
            SelectRequest sr = new SelectRequest(SELECT_USERS_EXPRESSION, Boolean.TRUE);
            result = this.sdb.select(sr);

            for (Item item : result.getItems()) {
                User currUser = fillUserFromSimpleDBAttrs(item);
                users.add(currUser);
            }

        } while (result != null && result.getNextToken() != null);

        return users;
    }

    @Override
    public User findById(String id) {
        List<User> users = new ArrayList<User>();
        SelectResult result = null;

        do {
            String query = SELECT_USER_BY_ID_EXPRESSION_PREFIX + '\'' + id + '\'';
            SelectRequest sr = new SelectRequest(query, Boolean.TRUE);
            result = this.sdb.select(sr);

            for (Item item : result.getItems()) {
                User currUser = fillUserFromSimpleDBAttrs(item);
                users.add(currUser);
            }

        } while (result != null && result.getNextToken() != null);

        // return the found user, or check for errors
        if (users.size() == 1) {
            return users.get(0);

        } else if (users.isEmpty()) {
            return null;

        } else {
            // warn if the userid is not unique - should never happen
            // TODO add a warning if the userid is not unique?
            return null;
        }
    }

    @Override
    public User findByName(String name) {
        List<User> users = new ArrayList<User>();
        SelectResult result = null;

        do {
            String query = SELECT_USER_BY_NAME_EXPRESSION_PREFIX + '\'' + name + '\'';
            SelectRequest sr = new SelectRequest(query, Boolean.TRUE);
            result = this.sdb.select(sr);

            for (Item item : result.getItems()) {
                User currUser = fillUserFromSimpleDBAttrs(item);
                users.add(currUser);
            }

        } while (result != null && result.getNextToken() != null);

        // return the found user, or check for errors
        if (users.size() == 1) {
            return users.get(0);

        } else if (users.isEmpty()) {
            return null;

        } else {
            // warn if the userid is not unique - should never happen
            // TODO add a warning if the userid is not unique?
            return null;
        }
    }

    private static User fillUserFromSimpleDBAttrs(Item userItem) {
        String userName = userItem.getName();
        String userId = null;
        String hashedPassword = null;
        Boolean enabled = null;

        for (Attribute currAttribute : userItem.getAttributes()) {
            if (USER_ID.equals(currAttribute.getName())) {
                userId = currAttribute.getValue();

            } else if (HASH_SALTED_PASSWORD.equals(currAttribute.getName())) {
                hashedPassword = currAttribute.getValue();

            } else if (IS_ENABLED.equals(currAttribute.getName())) {
                enabled = Boolean.valueOf(currAttribute.getValue());
            }
        }

        User result = new User(userId, userName, hashedPassword, enabled);
        return result;
    }

    @Override
    public void deleteById(String id) {
        DeleteAttributesRequest deleteRequest = new DeleteAttributesRequest(IDENTITY_DOMAIN, id);
        this.sdb.deleteAttributes(deleteRequest);
    }

    @Override
    public void add(UserCreationRequest userRequested, String endpoint) {
        String hashedSaltedPassword = Utilities.getSaltedPassword(userRequested.getName(), endpoint, userRequested.getPassword());
        String userId = Utilities.generateRandomString();

        ReplaceableAttribute userIdAttr = new ReplaceableAttribute(USER_ID, userId, Boolean.TRUE);
        ReplaceableAttribute passwordAttr = new ReplaceableAttribute(HASH_SALTED_PASSWORD, hashedSaltedPassword, Boolean.TRUE);
        ReplaceableAttribute enableAttr = new ReplaceableAttribute(IS_ENABLED, "" + userRequested.isEnabled(), Boolean.TRUE);

        List<ReplaceableAttribute> attributes = new ArrayList<ReplaceableAttribute>(3);
        attributes.add(userIdAttr);
        attributes.add(passwordAttr);
        attributes.add(enableAttr);

        PutAttributesRequest par = new PutAttributesRequest(IDENTITY_DOMAIN, userRequested.getName(), attributes);
        this.sdb.putAttributes(par);
    }

}
