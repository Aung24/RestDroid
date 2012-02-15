require 'sinatra'
require 'data_mapper'
require 'json'

#DataMapper::Logger.new($stdout, :debug)
DataMapper.setup(:default, "sqlite::memory:")

class Person
  include DataMapper::Resource
  property :id,         Serial
  property :fname,      String, :required => true
  property :lname,      String
  property :age,        Integer, :required => true
  property :create_at,  DateTime
  property :updated_at, DateTime
end

DataMapper.auto_upgrade!

# for debugging
before do
  content_type :json
#  p request
#  p params
end

# server status
get '/' do
  erb :home
end

# index
get '/people' do
  @people = Person.all
  peeps = Array.new
  @people.each { |person| peeps << { :person => person } }
  { :people => peeps }.to_json
end

# create
post '/people' do
  @person = Person.new(params[:person])
  if @person.save
    status 201
    { :person => @person }.to_json
  else
    status 400
    { :status => :failed, :errors => @person.errors.to_hash }.to_json
  end
end

# show
get '/people/:id' do
  @person = Person.get(params[:id])
  { :person => @person }.to_json
end

# update
put '/people/:id' do
  @person = Person.get(params[:id])
  if @person.update(params[:person])
    status 201
    { :person => @person }.to_json
  else
    status 400
    { :status => :failed, :errors => @person.errors.to_hash }.to_json
  end
end

#delete
delete '/people/:id' do
  Person.get(params[:id]).destroy
end

__END__

@@ home

<h1>RestServer Sample is now running...</h1>
<h3>endpoints respond to json</h3>
