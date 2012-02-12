require 'sinatra'
require 'data_mapper'

DataMapper::Logger.new($stdout, :debug)
DataMapper.setup(:default, "sqlite::memory:")

class Person
  include DataMapper::Resource
  property :id,         Serial
  property :fname,      String, :required => true
  property :lname,      String
  property :age,        Integer
  property :create_at,  DateTime
  property :updated_at, DateTime
end

DataMapper.auto_upgrade!

# for debugging
before do
  puts '[Params]'
  p params
end

get '/' do
  "Rest Test Server is running..."
end

# index
get '/people' do
  @people = Person.all
  @people.to_json
end

# create
post '/people' do
  @person = Person.new(params)
  if @person.save
    status 201
    @person.to_json
  else
    status 400
    @person.errors.each do |e|
      puts e
    end
  end
end

get '/people/:id' do
  #...
end

