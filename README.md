## Deadline and Submission

The submission deadline is **May 3rd 2023 (Wednesday of week 11) at 12 pm (noon)**. You must use Gitlab to submit this coursework and should follow department guidelines on how to do this. The final submission must be **on the main branch** and tagged with the **tag selfish** .

## Important sources of information

Before you start, you should read the following (in this order)

1. The manual
2. The UML diagram
3. The spec
4. The getting started guide (optional, but recommended)

Other sources of information:

* [The forum](https://online.manchester.ac.uk/webapps/discussionboard/do/forum?action=list_threads&course_id=_72765_1&nav=discussion_board&conf_id=_445540_1&forum_id=_635168_1)
* The labs
* The output of [Jenkins continuous testing](https://ci.cs.manchester.ac.uk/jenkins/)

## Running the code

This excercise you can implement a console or JavaFX application as you prefer. The console application will not be marked. The JavaFX application is optional and will not be marked unless all tests pass and you request a marking slot.

We have provided a (near-empty) console application in your repository, we refer to this in other documents as the "driver". To run the provided driver:

```
> cd src/main
> javac GameDriver.java
> java GameDriver
```


## Running the tests

There are two ways of testing your project.

1. Locally on your PC. In your locally cloned repository, run the script `./test.sh`.
 
2. Remotely on the [Jenkins continuous testing](https://ci.cs.manchester.ac.uk/jenkins/). Every time you push your current repository to GiLab, you will be able to see the outcome of the tests in Jenkins. This server runs the tests against the contents of your remote repository (i.e. only pushed commits).