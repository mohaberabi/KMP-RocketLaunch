package com.jetbrains.spacetutorial

import com.jetbrains.spacetutorial.cache.Database
import com.jetbrains.spacetutorial.cache.DatabaseDriverFactory
import com.jetbrains.spacetutorial.network.SpaceXApi

class SpaceXSDK(
    databaseDriverFactory: DatabaseDriverFactory,
    val api: SpaceXApi,

    ) {

    private val database = Database(databaseDriverFactory)

    /**
     * All Kotlin exceptions are unchecked, while Swift has only checked errors
     * (see Interoperability with Swift/Objective-C for details). Thus, to make your Swift code
     * aware of expected exceptions, Kotlin functions called from
     * Swift should be marked with the @Throws annotation specifying a list of potential exception classes.
     */
    /**
     * Checked Exceptions
     * Definition: Checked exceptions are exceptions that are checked at
     * compile-time by the compiler. This means that if a method can throw a checked exception,
     * it must either handle the exception using a try-catch block or declare it using the throws keyword in the method signature.
     * Example (Java):
     * java
     * Copy code
     * public void readFile(String fileName) throws IOException {
     *     FileReader fileReader = new FileReader(fileName);
     *     // Additional code to read the file
     * }
     * In this example, IOException is a checked exception. The method readFile must declare that it throws
     * IOException or handle it within the method.
     */

    /**
     * Unchecked Exceptions
     * Definition: Unchecked exceptions are exceptions that are not checked at compile-time.
     * These are usually subclasses of RuntimeException and Error. They can occur anywhere in the
     * program and are not required to be declared or handled explicitly.
     * Example (Java):
     * java
     * Copy code
     * public void divide(int a, int b) {
     *     int result = a / b; // Can throw ArithmeticException
     * }
     * ArithmeticException is an unchecked exception. There is no requirement to declare or catch it.
     *
     */

    @Throws(Exception::class)
    suspend fun getLaunches(forceReload: Boolean): List<RocketLaunch> {
        val cached = database.getAllLaunches()
        return if (cached.isNotEmpty() && !forceReload) {
            cached
        } else {
            api.getAllLaunches().also {
                database.clearAndCreateLaunches(it)

            }
        }
    }
}