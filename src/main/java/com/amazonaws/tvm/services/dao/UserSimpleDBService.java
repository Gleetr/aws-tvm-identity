package com.amazonaws.tvm.services.dao;

import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.simpledb.AmazonSimpleDBClient;
import com.amazonaws.services.simpledb.model.Attribute;
import com.amazonaws.services.simpledb.model.Item;
import com.amazonaws.services.simpledb.model.SelectRequest;
import com.amazonaws.services.simpledb.model.SelectResult;
import com.amazonaws.tvm.Configuration;
import com.amazonaws.tvm.domain.User;

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

    private static User fillUserFromSimpleDBAttrs(Item userItem) {
        String userId = userItem.getName();
        String hashedPassword = null;
        Boolean enabled = null;

        for (Attribute currAttribute : userItem.getAttributes()) {
            if (HASH_SALTED_PASSWORD.equals(currAttribute.getName())) {
                hashedPassword = currAttribute.getValue();

            } else if (IS_ENABLED.equals(currAttribute.getName())) {
                enabled = Boolean.valueOf(currAttribute.getValue());
            }
        }

        User result = new User(userId, hashedPassword, enabled);
        return result;
    }

}
