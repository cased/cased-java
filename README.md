# cased-java

A Java client for Cased, a web service that makes it easy to add audit trails to any
application in minutes.

## Overview

- [Installation](#installation)
- [Configuration](#configuration)
- [Usage](#usage)
  - [Publishing events to Cased](#publishing-events-to-cased)
  - [Retrieving events from a Cased Policy](#retrieving-events-from-a-cased-policy)
  - [Retrieving events from a Cased Policy containing variables](#retrieving-events-from-a-cased-policy-containing-variables)
  - [Pagination](#pagination)
  - [Masking & filtering sensitive information](#masking-and-filtering-sensitive-information)
  - [Disable publishing events](#disable-publishing-events)
  - [Testing](#testing)


## Installation

### Maven
Add the dependency to your POM file:

```xml
<dependency>
  <groupId>com.cased</groupId>
  <artifactId>cased-java</artifactId>
  <version>0.1.1-SNAPSHOT</version>
</dependency>
```

### Gradle
Add the dependency to your build file:

```groovy
implementation "com.cased:cased-java:0.1.1-SNAPSHOT"
```

## Configuration
`cased-java` uses minimalist, straight-forward global configuration.

The most important settings are your **publish_key** (the API key you use to publish events to Cased)
and your **policy_key** (the key you use to retrieve audit events from Cased via
a [policy](https://docs.cased.com/policies)).

Configure these settings like so (we strongly recommend using environmental variables to pass in your values):

```java
import com.cased.Config;

Config.publishKey = "publish_test_1g1JAUGDacgo20x7eNzl12VjlSA"
Config.policyKey = "policy_test_1g1JAXtGEEYDgnEinMIi2psppEo"
```


## Usage

### Publishing events to Cased

Publish a `Map` of `String` keys and any values. `cased-java` takes care of
JSON serialization and deserialization.

```java
import java.util.HashMap;
import java.util.Map;

import com.cased.Cased;

Cased cased = new Cased();

Map<String, Object> myEvent = new HashMap<String, Object>();
myEvent.put("action", "test.action");
myEvent.put("user", "Jane");

cased.Event.publish(myEvent);
```

### Retrieving events from a Cased Policy

List events from a policy with the `list()` method. This method returns an `ResultsList`
object.

```java
import com.cased.Cased

Cased cased = new Cased();

ResultsList events = cased.Event.list();
```

The `ResultList` is used like this to get the actual results:

```java
<List<Result>> results = events.getResults();
```

`Result` is a `Map`, and represents a JSON object. You can access its keys as you would any `Map`. Get the
value of `action` for the first `Result`:

```java
String action = results.get(0).get("action");
System.out.println(action);
```

```
> "test.action"
```

You can get metadata about the `ResultList`:

```java
int pageCount = events.getPageCount();

int totalCount = events.getTotalCount();
```

Fetch a single event with its event `id`. You'll get back the event as a
`Map`, with `String` keys. The `fetch()` method will automatically use
the policy key you set with `Cased.policyKey`.

```java
import com.cased.Cased;

Cased cased = new Cased();
Result result = cased.Event.fetch("event_1g1JAVt1sjDeNQ9okRHUTh8LtXN");
String action = result.get("action");
```

### Retrieving events from a Cased Policy containing variables

You can gain additional control over your results by providing _policy variables_ to the `list()` operation.
You do so by using a map of policy variables, which you add to your general `params` map.

```java
import com.cased.Cased

Cased cased = new Cased();

// Create a new map for your params
Map<String, Object>params = new HashMap<String, Object>();

// Create a new map for your policy variables
Map<String, String> policyVariables = new HashMap<String, String>();

// Add some policy variables to that map, and associate the map with the "variables" key
policyVariables.put("team_id", "team-123");
policyVariables.put("organization_id", "org-abc");
params.put("variables", policyVariables);

// Call list() with the params
ResultsList events = cased.Event.list(params);
```

### Pagination

You can control pagination via `params` to `list()`:

```java
import com.cased.Cased

Cased cased = new Cased();

// Create a new map for your params
Map<String, Object>params = new HashMap<String, Object>();

params.put("limit", 10); // limit 10 items per page
params.put("page", 2);   // return results from page 2

// Call list() with the params
ResultsList events = cased.Event.list(params);
```


### Masking & filtering sensitive information

You can mark patterns in your audit entries as _sensitive_ — this tells Cased to mask this data. You can use
this to hide things like personally-indentifiable information (PII).

To do so, you use a `SensitiveDataHandler`, and add it globally:

```java
import com.cased.data.SensitiveDataHandler;

SensitiveDataHandler handler = new SensitiveDataHandler("username", "@([A-Za-z0-9_]+)")
```

where the first argument is a descriptive `label` for your handler, and the second is a regular expression
used to match sensitive data.

Add it globally:

```java
import com.cased.Config

Config.addSensitiveDataHandler(handler);
```

Now any data you send that matches that pattern with will be marked as sensitive when sent to Cased.

### Disable publishing events

You may want to completely prevent events from being published (perhaps for testing purposes). To do so, just set:

```java
import com.cased.Config

Config.setDisablePublishing(true);
```

### Testing

Run tests with Maven:

```
mvn test
```




