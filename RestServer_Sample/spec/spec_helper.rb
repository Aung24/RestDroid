require File.join(File.dirname(__FILE__), '../', 'server')

require 'rack/test'
require 'yaml'

set :environment, :test

# clear database transaction after every test case
RSpec.configure do |config|
  config.before(:each) do
    repository(:default) do
      transaction = DataMapper::Transaction.new(repository)
      transaction.begin
      repository.adapter.push_transaction(transaction)
    end 
  end

  config.after(:each) do
    repository(:default).adapter.pop_transaction.rollback
  end
end
