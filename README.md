# Se
**Se**(lenium) is a cousin of [HyperSelenium](https://github.com/hydrogen2oxygen/hyperselenium). Both aims to enable a developer to write fast browser automations
for tests or other purposes. In contrast to Hyperselenium **Se** uses only Java without a script language.

## Download
### Maven Dependency via JitPack
Thanks to [JitPack](https://jitpack.io/) you can download **Se** as a dependency into your maven project.

```xml
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>
    
	<dependency>
	    <groupId>com.github.hydrogen2oxygen</groupId>
	    <artifactId>Se</artifactId>
	    <version>master-SNAPSHOT</version>
	</dependency>
```

## Features

### Simple automation creation
```java
public class MyBrowserAutomation extends AbstractBaseAutomation {

    private static Logger logger = LogManager.getLogger(MyBrowserAutomation.class);

    @Override
    public void checkPreconditions() throws PreconditionsException {
        // if false the "test" will be yellow, not red
        assertTrue(ping("github.com"));
    }

    @Override
    public void run() throws Exception {

        wd.openPage("https://github.com/hydrogen2oxygen/Se")
                .waitMillis(1000)
                .textByName("q", "Selenium")
                .sendReturnForElementByName("q")
                .screenshot();

        // do asserts
        // ...
    }

    @Override
    public void cleanUp() throws Exception {
        // clean up whatever should be cleaned up
    }
```

### Reusable Snippets
Every automation can be reused as a snippet inside other automations or tests.

### Groups
Groups are preconditions for many parallel running tasks, in order that they don't hinder each other in the execution.

### Parallel running multiple selenium docker instances

```java
            // load the environment
            Environment environment = Se.loadEnvironment();
            // add a group
            Group group1 = new Group(environment, "GitHub1");
            group1.add(new OpenGithubSearchSelenium());
            group1.add(new OpenGithubSearchHydrogen2oxygen());

            // and a second group
            Group group2 = new Group(environment, "GitHub2");
            group2.add(new OpenGithubSearchElectron());
            group2.add(new OpenGithubSearchSpringBoot());

            // run group 1 and 2 in parallel
            Parallel parallel = new Parallel("Parallel Selenium Run, prove of concept", environment);
            parallel.add(group1);
            parallel.add(group2);
            parallel.run();
```

## Planned features
- Generate HTML protocol for all tests
- Create a INDEX inside the protocol and a overview of all fails and success
- Online Server Application that (re)runs tests delivered inside a jar file (by syncing a git repo with JGit) 
