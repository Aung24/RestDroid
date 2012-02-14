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
    last_response.should be_ok
    last_response.body.should include("is now running")
  end

  it "should list people" do
    get '/people'
    last_response.should be_ok
    body = JSON.parse(last_response.body)
    body.should include('people')
    people = JSON.parse(body['people'])
    person = JSON.parse(people[0].to_json)
    person.should have(6).items
    person["fname"].should == 'Abraham'
  end

  it "should create a new person" do
#    post '/people'
  end

  it "should not create a new person if no fname is given" do
#    post '/people'
#    last_response.should_not be_ok
#    body = JSON.parse(last_response.body)
#    p body
#    p body.class
#    body["errors"]["fname"].should == "Fname must not be blank"
  end

  it "should show a person" do
#    get "/people/#{ person.id }"

#    p last_response.body

  end

end
