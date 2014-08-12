Token Vending Machine (TVM) patched
------------

## What Amazon said in the original README
This is a sample reference application for running a service that distributes temporary credentials to client applications.
Currently, this sample code can be used in conjunctions with the Mobile SDKs for iOS and Android.  For more information
please visit:
  * http://aws.amazon.com/articles/SDKs/Android/4611615499399490
  * http://aws.amazon.com/sdkforios/
  * http://aws.amazon.com/sdkforandroid/

## What we changed
This is the base for the version we run at Gleetr, with the following changes:
  * Everything is backward-compatible, and we reused nearly the whole original Amazon TVM code
  * We added a REST API to do CRUD operations on users
  * We added a security layer to avoid leaving the admin operations open to anyone

## REST API
### List All Users
$ curl http://localhost:8080/management/user/list
 [{"id":"pierre","enabled":true}]

### Create a User
curl -XPUT http://localhost:8080/management/user/id/pierre

### Delete a User
curl -XDELETE http://localhost:8080/management/user/id/nicolas

### Fetch a User by ID (not very interesting)
curl http://admin:admin@localhost:8080/management/user/id/pierre
Does not work at the moment.

### TODO
Remaining operations?
- Enable/disable an user
curl http://admin:admin@localhost:8080/management/user/id/pierre/_enable
curl http://admin:admin@localhost:8080/management/user/id/pierre/_disable
