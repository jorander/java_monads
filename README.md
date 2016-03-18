[![Build Status](https://travis-ci.org/enelson/java_monads.svg?branch=master)](https://travis-ci.org/enelson/java_monads)
[![Coverage Status](https://codecov.io/github/enelson/java_monads/coverage.png?branch=master)](https://codecov.io/github/enelson/java_monads?branch=master)

# Common Monads for Java

There is a great article [here](http://adit.io/posts/2013-06-10-three-useful-monads.html) that talks about 3 very common and useful monads: `Reader`, `Writer` and `State`. 

## Reader

The `Reader` monad allows a common context (ie. DAO object) to be threaded through to chained functions. A common use for this type of monad is dependency injection.

## Writer

The `Writer` monad is used to keep a running log of chained computations. For instance, if you have multiple arithmetic functions chained together, the writer class can append to the log each time a computation is run, and the final result will contain both the final value of the computations, as well as the complete log.

## State

The `State` monad can be seen as similar to the reader monad, except that the state can be modified throughout the call chain, whereas the reader's context can only be read. 
