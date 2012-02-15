require 'spec_helper'
require 'json'

describe 'Rest Server' do
  include Rack::Test::Methods

  def app
    Sinatra::Application
  end

  before :each do
    @person = Person.new(:fname => 'Abraham', :age => 32 )
    @person.save
  end

  it "should be running" do
    get '/'
    last_response.status.should == 200
    last_response.body.should include("is now running")
  end

  it "should list people" do
    get '/people'
    last_response.status.should == 200
    body = JSON.parse(last_response.body)
    p body
    body.should include('people')
    body['people'][0]['person'].should have(6).items
    body['people'][0]['person']['fname'].should == @person.fname
  end

  it "should create a new person" do
    post '/people', { :person => { :fname => 'Billy', :age => 12 } }
    last_response.status.should == 201
    body = JSON.parse(last_response.body)
    body['person']['fname'].should == "Billy"
    body['person']['age'].should == 12
  end

  it "should not create a new person if no fname or age is given" do
    post '/people'
    last_response.status.should == 400
    body = JSON.parse(last_response.body)
    body['errors']['fname'][0].should == "Fname must not be blank"
    body['errors']['age'][0].should == "Age must not be blank"
  end

  it "should show a person" do
    get "/people/#{@person.id}"
    last_response.should be_ok
    body = JSON.parse(last_response.body)
    body['person']['fname'].should == "Abraham"
    body['person']['age'].should == 32
  end

  it "should allow an update for a person" do
    put "/people/#{@person.id}", { :person => { :fname => 'Chilly' } }
    last_response.status.should == 201
    body = JSON.parse(last_response.body)
    body['person']['fname'].should == "Chilly"
  end

  it "should delete a person" do
    delete "/people/#{@person.id}"
    last_response.should be_ok
    Person.get(@person.id).should be_nil
  end

end
