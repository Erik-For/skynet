class ExternalDatabase < ActiveRecord::Base
    self.abstract_class = true
    establish_connection :external_db
  end