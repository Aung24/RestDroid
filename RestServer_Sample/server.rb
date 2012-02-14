require 'sinatra'
require 'data_mapper'

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
#  puts '[Params]'
#  p params
end

get '/' do
  erb :home
end

# index
get '/people' do
  @people = Person.all
  { :people => @people.to_json }.to_json
end

# create
post '/people' do
  @person = Person.new(params)
  if @person.save
    status 201
    { :people => @person.to_json }.to_json
  else
    status 400
   bob = "{\"errors\" : { \"fname\" : \"test 123\" } }"
   p bob
   bob
# { :errors => @person.errors.to_hash.to_json }.to_json
  end
end

# show
get '/people/:id' do
  @person = Person.get(params[:id])
  @person.to_json
end

# update
put '/people/:id' do
  @person = Person.get(params[:id])
  if @person.update(params)
    status 201
    @person.to_json
  else
    status 400
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
