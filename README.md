# Secret Escapes Technical Challenge using  Java + Aurelia
This is my solution to the secret escapes technical challenge.  Been working on in when and where I can, while balancing the day job and other responsibilities

## Running the backend
1. Ensure you have maven installed, and enter the backend directory

2. Run the build and run script which will package the project into a jar, and run an instance of the server

  ```shell
  ./buildAndRun.sh
  ```

## Running the webapp
1. You'll need gulp to run the webapp. Install using:

  ```shell
  npm install -g gulp
  ```
2. You'll also need [jspm](http://jspm.io/). Install with:

  ```shell
  npm install -g jspm
  ```

3. To run the frontend, enter the webapp directory and run the following command:
  ```shell
  gulp watch
  ```
4. You'll find the front end running at [http://localhost:9000](http://localhost:9000).  Make sure you're running the backend too or it just plain won't work.

## Possible Extension work

- The front-end has several cases that aren't handled properly at the moment, such as if you still have "choose an option" as one of your selected objects

- More tests around the model objects and route consumers

- Put the "update" login onto the model objects, rather than on persistance objects, and turn the persistance managers into factories.
