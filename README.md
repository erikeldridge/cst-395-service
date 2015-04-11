# CST 395 Service

## Run

```
$ mvn clean compile jetty:run
```

## Integration tests

```
$ mvn test
```

## Staging

_https://vast-spire-6818.herokuapp.com_ is the staging host. See Heroku's [managing multiple environments documentation](https://devcenter.heroku.com/articles/multiple-environments) for setup instructions.

```
$ git push staging master
$ curl -v "https://vast-spire-6818.herokuapp.com/feature_switch_config?version=2.3&os=android&id=123"
```

## Production

_http://young-depths-7217.herokuapp.com_ is the production host.
