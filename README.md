Token Vending Machine (TVM) patched
------------

Let's modestly call it the Next-Gen TVM :) More seriously though, this is a more production-friendly version of the original code.

## What Amazon said in the original README
This is a sample reference application for running a service that distributes temporary credentials to client applications.
Currently, this sample code can be used in conjunctions with the Mobile SDKs for iOS and Android.  For more information
please visit:

  * http://aws.amazon.com/articles/SDKs/Android/4611615499399490
  * http://aws.amazon.com/sdkforios/
  * http://aws.amazon.com/sdkforandroid/

## What we changed
This is the base for the version we run at Gleetr, with the following changes:

  * Everything is 100% backward-compatible, and we reused nearly the whole original Amazon TVM code for the token exchange part
  * We added a REST API to do CRUD operations on users
  * We added a security layer to avoid leaving the admin operations open to anyone
  * We used Maven instead of the custom ANT code

## Configuration
What you can/need to change is mostly exactly (see next paragraph for added config options) what you would need to change in the default Amazon code. As this is not always clear, here it is:

  * AWS Region: if you use a different region than the default `us-east-1` (we do), you'll need to edit the value of constant `SIMPLEDB_REGION` in file `src/main/java/com/amazonaws/tvm/Configuration.java` 
  * if you want to change the session duration, it is in the same file, in constant `SESSION_DURATION`
  * last but not least, you'll need to pass the Java options the original code requires, being `-DPARAM1=myapp -DAWS_ACCESS_KEY_ID=myaccesskey -DAWS_SECRET_KEY=mysecret` 

The only configuration we added is the security one. This is standard Apache Shiro, and you can find the config file at `src/main/resources/shiro.ini`. If you don't want to read the Shiro documentation, the hardcoded password to change is the line in the `[users]` section.

## Building
Just use Maven, classically:

    $ mvn clean package

## Deployment
Either use Amazon Beanstalk as explained in the provided original links, or you can deploy in any JEE container.

## REST API
Gentle reminder: use SSL. Please.

WARNING: you absolutely need to use the fully qualified server name you used in the clients to call the REST API. In the following examples, we'll be using
localhost for easy copy/paste but this will result in the passwords being hashed using 'localhost' in the seed... And when you'll run this in production,
authentication won't work with the users you created using this method. This is a feature/limitation in the original code.

### List All Users
	$ curl http://admin:admin@localhost:8080/management/user/list
	[{"id":"24f26695ecb6d15fb48ef8eed9fd8154","name":"nicolas","enabled":true},{"id":"ea89229f9b03883cb604d1d3555461ab","name":"pierre","enabled":true}]`

### Create a User
	$ curl -XPOST -H "Content-Type: application/json" http://admin:admin@localhost:8080/management/user -d 	'{"name":"pierre","password":"mypass","enabled":true}'

### Delete a User
	$ curl -XDELETE http://admin:admin@localhost:8080/management/user/id/nicolas

### Fetch a User by Name
	$ curl http://admin:admin@localhost:8080/management/user/name/pierre
	{"id":"ea89229f9b03883cb604d1d3555461ab","name":"pierre","enabled":true}

### Fetch a User by ID
	$ curl http://admin:admin@localhost:8080/management/user/id/24f26695ecb6d15fb48ef8eed9fd8154
	{"id":"24f26695ecb6d15fb48ef8eed9fd8154","name":"nicolas","enabled":true}

### Count Users
	$ curl http://admin:admin@localhost:8080/management/user/count
	2

### Debug API
#### Dump all user records
$ curl http://admin:admin@localhost:8080/management/debug/user
{Name: pierre,Attributes: [{Name: enabled,Value: true,}, {Name: userid,Value: 2d956dd0433e5bc00a3dbfd8e521d14d,}, {Name: hash_salted_password,Value: 9b884f64593947bda2cad125e415e1a76ba98893c8bc24166a33424e71eced32,}]}
{Name: nicolas,Attributes: [{Name: enabled,Value: true,}, {Name: userid,Value: 5bfc951ae93f2ed258d828f147448f7c,}, {Name: hash_salted_password,Value: c4144562c9451b881b1ebefd647b7fee7e38459db2e5de4cccf665c31a93d44b,}]}

#### Dump all device records
$ curl http://admin:admin@localhost:8080/management/debug/device
{Name: 9b0c122adfef83c9b8c2a9504f80e825,Attributes: [{Name: userid,Value: 5bfc951ae93f2ed258d828f147448f7c,}, {Name: key,Value: 349d8e3b6a7233949fd98bb94ae2a1a5,}]}
{Name: 13250eb2-afcf-4e6a-a88d-86cd17aba1a4,Attributes: [{Name: userid,Value: 2d956dd0433e5bc00a3dbfd8e521d14d,}, {Name: key,Value: 1f82407853fb620f54fae71f70ea908e,}]}

#### Dump all SimpleDB domains
$ curl http://admin:admin@localhost:8080/management/debug/domain
[TokenVendingMachine_myapp_DEVICES, TokenVendingMachine_myapp_USERS]

### TODO
Enable/disable an user:

    $ curl http://admin:admin@localhost:8080/management/user/id/24f26695ecb6d15fb48ef8eed9fd8154/_enable
    $ curl http://admin:admin@localhost:8080/management/user/name/pierre/_disable

If you need something else, let us know in the Issues section. Patches are optional but very welcome, of course!
