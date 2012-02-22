# RestDroid
### Simple Rest Library for Android

#### Example Usage:
`RestResponse response = RestDroid.Post(SERVER + "/people", 
    "person[fname]=Barbados", "person[lname]=Slim", "person[age]=42");`

#### More Examples
##### See `RestDroid_Sample` project

## Rest Server Sample
Uses Sinatra to provide a test rest endpoint.

### Preparing for use
install rvm   
browse to this directory in console   
install bundler if needed  
`bundle install`   

### Running the Server
`unicorn -p 3000`

### Running Tests
`autotest`
