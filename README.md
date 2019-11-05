## Torque

[![](https://jitpack.io/v/suransea/torque.svg)](https://jitpack.io/#suransea/torque)


##### Java 常用工具合集

#### Maven

Step 1. Add the JitPack repository to your build file

	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

Step 2. Add the dependency

	<dependency>
	    <groupId>com.github.suransea</groupId>
	    <artifactId>torque</artifactId>
	    <version>x.y.z</version>
	</dependency>



#### Gradle


Step 1. Add the JitPack repository to your build file


Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

Step 2. Add the dependency

	dependencies {
	        implementation 'com.github.suransea:torque:x.y.z'
	}
