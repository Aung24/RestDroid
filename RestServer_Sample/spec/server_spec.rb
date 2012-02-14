require 'spec_helper'

describe 'Rest Server' do
  include Rack::Test::Methods

  def app
    Sinatra::Application
  end

  before :each do
    @book = Person.new(:fname => "Title")
    @book.save
  end

  it "should be running" do
    get '/'
    last_response.should be_ok
    last_response.body.should include("is now running")
  end

  it "should list people" do
    get '/people'
    last_response.should be_ok
    last_response.body.should == "[]";
  end

#  if "should post something" do
#    post '/', params = { :str => 'Heff' }
#  end

end
